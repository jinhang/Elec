package com.dome.viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dome.common.util.ExitApplication;
import com.dome.common.util.UIHelper;
import com.dome.common.util.UIHelper.Something;
import com.dome.db.DBUtil;
import com.dome.soap.SoapHeader;
import com.dome.soap.SoapHeader.parserAndInsert;
import com.dome.update.DataReceiver;
import com.dome.update.VersionUpdate;


/**
 * ����
 * 
 * @author ��
 *        �޸� - ����ԭ��ת        �� - �޸���ת
 * 
 */
public class FiveActivity extends Activity {
	public static long time;
	private boolean is_show_dailog = true;
	public static Boolean ctrl = true;//����Toast
	int REQUESTNUM = 1;
	private String APIKEY;
	public static final int PROGREESS_INCREMENT = 110/DataReceiver.REQUESTNUM+1;// ����������������
	protected static final int SpeedMax = 110;
	protected static final int SpeedDefault = 20;
	protected static final int ProgressBarWidth = 50;
	private static PopupWindow popupWindow;
	private static PopupWindow version_popupWindow;
	private SoapObject resultObject;
	private View view;
	private TableRow truncateData = null;// �������
	private TableRow idea_back = null;
	private TableRow check_update = null;// ������
	private TableRow gunayu = null;// guanyu jinhang
	private Button updateData;
	private Button cancleData;
	private Button check_version;
	private Button cancle_check;
	private TextView update_content_textview;
	private String update_content;// �汾���µ�����
	private String apk_url;// ����url
	private String new_version;// ����汾
	public static ProgressDialog progressDialog;
	private ProgressBar progressBar;
	private Toast toast;
	private Toast mToast;
	private Toast update_toast;
	private int sleepTime = 4000;
	private SoapHeader[] headers = new SoapHeader[REQUESTNUM];
//	private FiveReceiver receiver;
	private Boolean isRigisted = false;
	private Timer timer;// ����toast��ʾʱ��
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.five);
		/**
		 * �������
		 */
		truncateData = (TableRow) findViewById(R.id.more_page_delete);
		truncateData.setOnClickListener(new More_Listener());
		/**
		 * �������
		 */
		idea_back = (TableRow) findViewById(R.id.more_page_row5);
		idea_back.setOnClickListener(new More_Listener());
		/**
		 * ������
		 */
		check_update = (TableRow) findViewById(R.id.more_page_row6);
		check_update.setOnClickListener(new More_Listener());
		gunayu = (TableRow) findViewById(R.id.more_page_row7);
		gunayu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						GuanYu.class);
				startActivity(intent);
			}
		});

	}

	class More_Listener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.more_page_delete:// �������
				showWindow(new View(getApplicationContext()));
				break;
			case R.id.more_page_row5: // �������
				Intent intent = new Intent(getApplicationContext(),
						IdeaBack.class);
				startActivity(intent);
				break;
			case R.id.more_page_row6:// ������
				if (checkSDCard()) {
//					useBroadcast();//ע��㲥
					progressBar = new ProgressBar(getApplicationContext(),
							null, android.R.attr.progressBarStyleLarge);
					progressBar.setMax(SpeedMax);
					progressBar.setProgress(SpeedDefault);
					progressBar.setLayoutParams(new LinearLayout.LayoutParams(
							200, 200));
					// if(toast == null){
					toast = Toast.makeText(getApplicationContext(),
							"���ڼ��汾...", Toast.LENGTH_LONG);
					// }
					// else {
					toast.setText("���ڼ��汾...");
					toast.setDuration(10000);
					// }
					toast.setGravity(Gravity.CENTER, 0, 0);
					LinearLayout toastView = (LinearLayout) toast.getView();
					toastView.setGravity(Gravity.CENTER);
					// ��ע�����ﴫ1�Ǳ�Ҫ�ģ���Ϊ����textView�Ѿ��ڴ�����ʱ����ӽ�ȥ�ˣ�����㴫0
					// ��ô���������ʾ������ ��������ʾ���֣��������Ҫ�Ļ�������ô��������������Ҫ
					// �������Ϸ� ������������ so ��1
					toastView.addView(progressBar, 0);
				    toast.show();
					/*
					 * if (toast != null) { toast.cancel(); }
					 */
					if (isNetworkConnected(getApplicationContext())) {

						Resources rs = getApplicationContext().getResources();
						// ������������
						APIKEY = rs.getString(R.string.APIKEY);// �������ļ����ҵ�apikey
						List<Map<String, String>> list1 = new ArrayList<Map<String, String>>();
						list1.add(addParams("in0",
								getAppVersionName(getApplicationContext())));
						list1.add(addParams("in1", APIKEY));
						headers[0] = getAndPut(rs, R.array.url_apk_version,
								list1, new parserAndInsert() {
									public void afterDown(SoapObject object) {

										SoapObject pubinfos = (SoapObject) object
												.getProperty(0);
										String out = pubinfos
												.getPropertyAsString("is_Ok");
										System.out
												.println("9999999999999999999999999999999999999"
														+ out);
										if (out.equals("ok")) {
											showToast("����¼����л���ķ���");
											cancelToast();
											FiveActivity.this.finish();
										} else {
											showToast("����ʧ��");
											cancelToast();
										}
									}
								});
						oneHeaderRun(headers[0]);

					} else {
						showToast("������������");
						cancelToast();
					}

				} else {
					showToast("����SDCard�Ƿ����");
					cancelToast();
				}
				;
				break;
			default:
				break;
			}
		}
	}

	// ���� Toast��ʾʱ��
	private void execToast() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(ctrl){
					update_toast.show();
				}
				
				System.out.println("��ʾtoast");
			}

		}, 10);
	}


	private void showWindow(View parent) {

		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = layoutInflater.inflate(R.layout.truncate_data, null);
			updateData = (Button) view.findViewById(R.id.update_data);
			cancleData = (Button) view.findViewById(R.id.cancle_data);
			// ����һ��PopuWidow����
			popupWindow = new PopupWindow(view, getWindowManager()
					.getDefaultDisplay().getWidth(), 330);
		}

		// ʹ��ۼ�
		popupWindow.setFocusable(true);
		// ����������������ʧ
		popupWindow.setOutsideTouchable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ���õ�������
		// popupWindow.setAnimationStyle(R.anim.push_bottom_out);
		popupWindow.setAnimationStyle(R.style.mystyle);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// ��ʾ��λ��Ϊ:��Ļ�Ŀ�ȵ�һ��-PopupWindow�ĸ߶ȵ�һ��
		/*
		 * int xPos = windowManager.getDefaultDisplay().getWidth() / 2 -
		 * popupWindow.getWidth() / 2;
		 */
		int xPos = windowManager.getDefaultDisplay().getHeight() / 2
				- popupWindow.getHeight() / 2;
		Log.i("coder", "xPos:" + xPos);

		popupWindow
				.showAsDropDown(parent, windowManager.getDefaultDisplay()
						.getHeight() - popupWindow.getHeight(),
						windowManager.getDefaultDisplay().getHeight()
								- popupWindow.getHeight() - 74);
		updateData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isNetworkConnected(getApplicationContext())) {
//					useBroadcast();
					popupWindow.dismiss();// ��ȡ����Ȼ���ٵ���bar
					progressDialog = new ProgressDialog(FiveActivity.this); 
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//���÷��ΪԲ�ν�����  
					progressDialog.setTitle("��ʾ");//���ñ���  
					progressDialog.setMessage("���ڸ��£����Ե�...");  
					progressDialog.setIndeterminate(false);//���ý������Ƿ�Ϊ����ȷ  
					progressDialog.setCancelable(false);//���ý������Ƿ���԰��˻ؼ�ȡ�� 
					progressDialog.show(); 
					// ������б�
					DBUtil dbUtil = new DBUtil(getApplicationContext());
					dbUtil.deleteAllTables();// ɾ�����б�
					//��������
					DataReceiver.show_dailog();
				DataReceiver.is_show_dailog = true;
					time = System.currentTimeMillis();
					// �򿪹㲥
					Intent intent = new Intent();
					intent.setAction("com.dome.update.DataReceiver");
					sendBroadcast(intent);
					System.out.println("Five�򿪹㲥");
//					ctrl = false;
				} else {
					Toast.makeText(getApplicationContext(), "������������", 1000)
							.show();
				}

			}
		});
		cancleData.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
	}

	// ������
	private static String check_Is_Update() {
		String is_Update = "no";

		return is_Update;
	}

	private void check_version_showWindow(View parent) {

		if (version_popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = layoutInflater.inflate(R.layout.check_apk_version, null);
			update_content_textview = (TextView) view
					.findViewById(R.id.update_content);
			check_version = (Button) view.findViewById(R.id.check_version);
			cancle_check = (Button) view.findViewById(R.id.cancle_check);
			// ����һ��PopuWidow����
			version_popupWindow = new PopupWindow(view, getWindowManager()
					.getDefaultDisplay().getWidth(), 330);
		}
		// ʹ��ۼ�
		version_popupWindow.setFocusable(true);
		// ����������������ʧ
		version_popupWindow.setOutsideTouchable(true);
		// �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı���
		version_popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// ���õ�������
		// popupWindow.setAnimationStyle(R.anim.push_bottom_out);
		version_popupWindow.setAnimationStyle(R.style.mystyle);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// ��ʾ��λ��Ϊ:��Ļ�Ŀ�ȵ�һ��-PopupWindow�ĸ߶ȵ�һ��
		/*
		 * int xPos = windowManager.getDefaultDisplay().getWidth() / 2 -
		 * popupWindow.getWidth() / 2;
		 */
		int xPos = windowManager.getDefaultDisplay().getHeight() / 2
				- version_popupWindow.getHeight() / 2;
		Log.i("coder", "xPos:" + xPos);

		version_popupWindow.showAsDropDown(parent,
				windowManager.getDefaultDisplay().getHeight()
						- version_popupWindow.getHeight(),
				windowManager.getDefaultDisplay().getHeight()
						- version_popupWindow.getHeight() - 74);
		update_content_textview.setText("����汾:" + new_version + "\n"
				+ update_content);
		// �����£��������
		check_version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				version_popupWindow.dismiss();
				// version_popupWindow = null;
				Toast.makeText(getApplicationContext(), apk_url, 1000).show();
				// ���°汾
				VersionUpdate versionUpdate = new VersionUpdate(
						FiveActivity.this);
				versionUpdate.apkUrl = apk_url;
				versionUpdate.checkUpdateInfo();
			}
		});
		cancle_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				version_popupWindow.dismiss();
				// version_popupWindow = null;
			}
		});
	}

	/**
	 * ���巵�ؼ�
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			UIHelper.exit(FiveActivity.this, new Something() {

				@SuppressWarnings("deprecation")
				@Override
				public void doFinish() {
					ExitApplication.getInstance().exit();
				}
			});
			break;

		default:
			break;
		}

		return true;

	}

	/**
	 * ���ص�ǰ����汾��
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * �ж������Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * ����Ƿ����SDCard
	 * 
	 * @return
	 */
	private boolean checkSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
	 * @param rs
	 *            ��Դ
	 * @param configId
	 *            �����ļ���ַ
	 * @param list
	 *            �������
	 * @param insert
	 *            ��������ִ��
	 * 
	 */
	private SoapHeader getAndPut(Resources rs, int configId,
			List<Map<String, String>> list, parserAndInsert insert) {

		// ��ȡ������Ϣ
		String[] request = rs.getStringArray(configId);

		// ��Ӳ������ݿ�Ķ���
		SoapHeader header = new SoapHeader(insert);

		// ���������ͷ��Ϣ
		header.setUrl(request[0]);
		header.setNamespace(request[1]);
		header.setMethod(request[2]);
		header.setSoapAction(request[3]);

		// ���ö�̬�Ĳ���
		if (list == null) {
			;
		} else {

			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				header.addPropertities("in" + i, map.get("in" + i));
			}
		}
		// ��������
		return header;
	}

	private Map<String, String> addParams(String a, String b) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(a, b);
		return map;

	}

	/**
	 * ��ÿ��������д���
	 * 
	 * @param header
	 */
	private void oneHeaderRun(SoapHeader header) {
		try {
			SoapObject rpc = new SoapObject(header.getNamespace(),
					header.getMethod());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.encodingStyle = "UTF-8";
			// һ��forѭ����һ������Ӳ���
			List<HashMap<String, String>> list = header.getPropertities();
			int lenth = list.size();

			for (HashMap<String, String> temp : list) {
				Set<String> parameters = temp.keySet();
				for (String string : parameters) {
					rpc.addProperty(string, temp.get(string));
				}
			}

			envelope.bodyOut = rpc;
			envelope.dotNet = false;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE ht = new HttpTransportSE(header.getUrl());
			ht.debug = true;
			try {
				ht.call(header.getSoapAction(), envelope);
			} catch (IOException e) {
				System.out.println("500���󣡣���");
				return;
			}
			resultObject = (SoapObject) envelope.bodyIn;
			String is_OK = resultObject.getProperty("out").toString();
			if (!is_OK.equals("no")) {
				String[] content = is_OK.split("\\|");
				update_content = content[2];
				apk_url = content[0];
				new_version = content[1];
				if (toast != null) {
					toast.cancel();
				}
				check_version_showWindow(new View(getApplicationContext()));// ��������
				// toast.setGravity(Gravity.TOP, -1, 1);

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						toast.show();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();

			} else {
				/*if (toast != null) {
					toast.cancel();
				}*/
				showToast("��ǰ�Ѿ������°汾");
//				cancelToast();
			}
			System.out.println("09999999999999" + resultObject.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ������ȡ��toast
	public void showToast(String text) {
		if (mToast == null) {
			mToast = Toast
					.makeText(FiveActivity.this, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_LONG);
		}
		mToast.show();
	}

	public void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	/*public class FiveReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			progressBar.incrementProgressBy(PROGREESS_INCREMENT);
			if(timer != null && progressBar.getProgress() >= 106){
				timer.cancel();
				update_toast.cancel();
				System.out.println("**************************************����");
			}
		}
	}

	*//**
	 * ע��㲥ʹ��
	 *//*
	private void useBroadcast() {
		if (isRigisted == false) {
			receiver = new FiveReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.dome.viewer.FiveAvtivityReceiver");
			registerReceiver(receiver, filter);
			isRigisted = true;
		}

	}

	@Override
	protected void onDestroy() {

		if (receiver != null) {

			if (isRigisted == true) {
				unregisterReceiver(receiver);
			}
		}
		super.onDestroy();
	}*/
}
