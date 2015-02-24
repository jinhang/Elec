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
 * 设置
 * 
 * @author 马超
 *        修改 - 张藤原跳转        金航 - 修改跳转
 * 
 */
public class FiveActivity extends Activity {
	public static long time;
	private boolean is_show_dailog = true;
	public static Boolean ctrl = true;//控制Toast
	int REQUESTNUM = 1;
	private String APIKEY;
	public static final int PROGREESS_INCREMENT = 110/DataReceiver.REQUESTNUM+1;// 进度条的增长进度
	protected static final int SpeedMax = 110;
	protected static final int SpeedDefault = 20;
	protected static final int ProgressBarWidth = 50;
	private static PopupWindow popupWindow;
	private static PopupWindow version_popupWindow;
	private SoapObject resultObject;
	private View view;
	private TableRow truncateData = null;// 清空数据
	private TableRow idea_back = null;
	private TableRow check_update = null;// 检测更新
	private TableRow gunayu = null;// guanyu jinhang
	private Button updateData;
	private Button cancleData;
	private Button check_version;
	private Button cancle_check;
	private TextView update_content_textview;
	private String update_content;// 版本更新的内容
	private String apk_url;// 下载url
	private String new_version;// 最近版本
	public static ProgressDialog progressDialog;
	private ProgressBar progressBar;
	private Toast toast;
	private Toast mToast;
	private Toast update_toast;
	private int sleepTime = 4000;
	private SoapHeader[] headers = new SoapHeader[REQUESTNUM];
//	private FiveReceiver receiver;
	private Boolean isRigisted = false;
	private Timer timer;// 控制toast显示时间
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.five);
		/**
		 * 清空数据
		 */
		truncateData = (TableRow) findViewById(R.id.more_page_delete);
		truncateData.setOnClickListener(new More_Listener());
		/**
		 * 意见反馈
		 */
		idea_back = (TableRow) findViewById(R.id.more_page_row5);
		idea_back.setOnClickListener(new More_Listener());
		/**
		 * 检测更新
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
			case R.id.more_page_delete:// 清除数据
				showWindow(new View(getApplicationContext()));
				break;
			case R.id.more_page_row5: // 意见反馈
				Intent intent = new Intent(getApplicationContext(),
						IdeaBack.class);
				startActivity(intent);
				break;
			case R.id.more_page_row6:// 检测更新
				if (checkSDCard()) {
//					useBroadcast();//注册广播
					progressBar = new ProgressBar(getApplicationContext(),
							null, android.R.attr.progressBarStyleLarge);
					progressBar.setMax(SpeedMax);
					progressBar.setProgress(SpeedDefault);
					progressBar.setLayoutParams(new LinearLayout.LayoutParams(
							200, 200));
					// if(toast == null){
					toast = Toast.makeText(getApplicationContext(),
							"正在检测版本...", Toast.LENGTH_LONG);
					// }
					// else {
					toast.setText("正在检测版本...");
					toast.setDuration(10000);
					// }
					toast.setGravity(Gravity.CENTER, 0, 0);
					LinearLayout toastView = (LinearLayout) toast.getView();
					toastView.setGravity(Gravity.CENTER);
					// 请注意这里传1是必要的，因为他的textView已经在创建的时候添加进去了，如果你传0
					// 那么上面会先显示进度条 下面再显示文字，如果你需要的话可以这么做，但我现在需要
					// 文字在上方 进度条在下面 so 传1
					toastView.addView(progressBar, 0);
				    toast.show();
					/*
					 * if (toast != null) { toast.cancel(); }
					 */
					if (isNetworkConnected(getApplicationContext())) {

						Resources rs = getApplicationContext().getResources();
						// 更新新闻内容
						APIKEY = rs.getString(R.string.APIKEY);// 从配置文件中找到apikey
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
											showToast("已收录，感谢您的反馈");
											cancelToast();
											FiveActivity.this.finish();
										} else {
											showToast("反馈失败");
											cancelToast();
										}
									}
								});
						oneHeaderRun(headers[0]);

					} else {
						showToast("请检查网络连接");
						cancelToast();
					}

				} else {
					showToast("请检测SDCard是否存在");
					cancelToast();
				}
				;
				break;
			default:
				break;
			}
		}
	}

	// 控制 Toast显示时间
	private void execToast() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(ctrl){
					update_toast.show();
				}
				
				System.out.println("显示toast");
			}

		}, 10);
	}


	private void showWindow(View parent) {

		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = layoutInflater.inflate(R.layout.truncate_data, null);
			updateData = (Button) view.findViewById(R.id.update_data);
			cancleData = (Button) view.findViewById(R.id.cancle_data);
			// 创建一个PopuWidow对象
			popupWindow = new PopupWindow(view, getWindowManager()
					.getDefaultDisplay().getWidth(), 330);
		}

		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置弹出动画
		// popupWindow.setAnimationStyle(R.anim.push_bottom_out);
		popupWindow.setAnimationStyle(R.style.mystyle);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
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
					popupWindow.dismiss();// 先取消，然后再弹出bar
					progressDialog = new ProgressDialog(FiveActivity.this); 
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条  
					progressDialog.setTitle("提示");//设置标题  
					progressDialog.setMessage("正在更新，请稍等...");  
					progressDialog.setIndeterminate(false);//设置进度条是否为不明确  
					progressDialog.setCancelable(false);//设置进度条是否可以按退回键取消 
					progressDialog.show(); 
					// 清空所有表
					DBUtil dbUtil = new DBUtil(getApplicationContext());
					dbUtil.deleteAllTables();// 删除所有表
					//更新数据
					DataReceiver.show_dailog();
				DataReceiver.is_show_dailog = true;
					time = System.currentTimeMillis();
					// 打开广播
					Intent intent = new Intent();
					intent.setAction("com.dome.update.DataReceiver");
					sendBroadcast(intent);
					System.out.println("Five打开广播");
//					ctrl = false;
				} else {
					Toast.makeText(getApplicationContext(), "请检查网络连接", 1000)
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

	// 检测更新
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
			// 创建一个PopuWidow对象
			version_popupWindow = new PopupWindow(view, getWindowManager()
					.getDefaultDisplay().getWidth(), 330);
		}
		// 使其聚集
		version_popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		version_popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		version_popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置弹出动画
		// popupWindow.setAnimationStyle(R.anim.push_bottom_out);
		version_popupWindow.setAnimationStyle(R.style.mystyle);
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
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
		update_content_textview.setText("最近版本:" + new_version + "\n"
				+ update_content);
		// 检测更新，下载软件
		check_version.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				version_popupWindow.dismiss();
				// version_popupWindow = null;
				Toast.makeText(getApplicationContext(), apk_url, 1000).show();
				// 更新版本
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
	 * 定义返回键
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
	 * 返回当前程序版本名
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
	 * 判断网络是否可用
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
	 * 检查是否存在SDCard
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
	 *            资源
	 * @param configId
	 *            配置文件地址
	 * @param list
	 *            请求参数
	 * @param insert
	 *            插入语句的执行
	 * 
	 */
	private SoapHeader getAndPut(Resources rs, int configId,
			List<Map<String, String>> list, parserAndInsert insert) {

		// 获取配置信息
		String[] request = rs.getStringArray(configId);

		// 添加插入数据库的动作
		SoapHeader header = new SoapHeader(insert);

		// 设置请求的头信息
		header.setUrl(request[0]);
		header.setNamespace(request[1]);
		header.setMethod(request[2]);
		header.setSoapAction(request[3]);

		// 设置动态的参数
		if (list == null) {
			;
		} else {

			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = list.get(i);
				header.addPropertities("in" + i, map.get("in" + i));
			}
		}
		// 返回请求
		return header;
	}

	private Map<String, String> addParams(String a, String b) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(a, b);
		return map;

	}

	/**
	 * 对每个请求进行处理
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
			// 一个for循环，一用于添加参数
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
				System.out.println("500错误！！！");
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
				check_version_showWindow(new View(getApplicationContext()));// 弹出窗口
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
				showToast("当前已经是最新版本");
//				cancelToast();
			}
			System.out.println("09999999999999" + resultObject.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 创建和取消toast
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
				System.out.println("**************************************结束");
			}
		}
	}

	*//**
	 * 注册广播使用
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
