package com.dome.viewer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dome.common.util.ExitApplication;
import com.dome.common.util.UIHelper;
import com.dome.common.util.UIHelper.Something;
import com.dome.db.DatabaseHelper;
import com.dome.dialog.DialogBulder;
import com.dome.dialog.DialogBulder.OnDialogItemClickListener;

/**
 * ��ҳ��ʾ
 * 
 * @author ����ԭ
 * 
 */
public class OneActivity extends Activity {

	// *****tablerow1-4******
	/**
	 * ֪ͨ��Ϣ
	 */
	private TableRow tablerow1;
	/**
	 * ������Ϣ
	 */
	private TableRow tablerow2;
	private TableRow tablerow4;
	private TableRow homeRules;
	private TableRow tablerow6;
	/**
	 * �걨����
	 */
	private TableRow homeRead;

	// ��½******
	private Button btn_login;

	// *********************����ͼƬ****************************************
	private FrameLayout viewpagerpic;
	private ViewPager viewPager; // android-support-v4�еĻ������
	private List<ImageView> imageViews; // ������ͼƬ����

	private String[] titles; // ͼƬ����
	private int[] imageResId; // ͼƬID
	private List<View> dots; // ͼƬ�������ĵ���Щ��

	private TextView tv_title;
	private int currentItem = 0; // ��ǰͼƬ��������

	private Intent intent;

	String[] messageType;

	Resources rs;

	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

	// �л���ǰ��ʾ��ͼƬ
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// �л���ǰ��ʾ��ͼƬ
		};
	};

	// *********************����ͼƬ****************************************

	// ��ȡimei
	TelephonyManager myTelephonyManager;
	// IMEI
	private String IMEI = null;
	// database
	SQLiteDatabase database;
	DatabaseHelper db = new DatabaseHelper(this);

	// ���ص�Imei ����
	private String[] HAVEIMEIS;

	private String HAVENAME;

	private String[] HAVEIDS;

	private int result = -1;

	private String id = " ";

	private String short_name = " ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rs = getResources();
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.one);

		showPersonlyWeclcom();

		messageType = rs.getStringArray(R.array.message_type);
		loadingButtons();
		slidePic(); // ����ͼƬ main

	}

	/**
	 * �����Ի�����ʾ�û��Ƿ�ע��
	 */
	private void showPersonlyWeclcom() {
		database = db.openDatabase();
		System.out.println("�Ѿ������ݿ����ӣ���ʼ�˶�id");
		String sql = "SELECT participant_shortname as name ,t1.Participant_Id FROM  LogIn t1,mktadmin_participants t2 where t1.participant_id=t2.participant_id";
		Cursor c = database.rawQuery(sql, null);
		while (c.moveToNext()) {
			id = c.getString(c.getColumnIndex("Participant_Id"));
			short_name = c.getString(c.getColumnIndex("name"));
		}
		db.closeDatabase();
		System.out.println("�ѹر����ݿ�");
		// �ж��û����
		if(id.equals(" ")){
			id = "0";
		}
		if (id.equals("0")) {
			showDialog(1);
		} else {
			showDialog(2);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case 0:
			return new DialogBulder(this).setTitle("����").setMessage("����")
					.create();
		case 1:
			return new DialogBulder(this)
					.setTitle("��ܰ��ʾ")
					.setMessage("����û��ע�ᣡ")
					.setButtons("ȷ��", "ȡ��",
							new DialogBulder.OnDialogButtonClickListener() {
								public void onDialogButtonClick(
										Context context, DialogBulder builder,
										Dialog dialog, int dialogId, int which) {
									if (which == BUTTON_LEFT) {
									}
								}
							}).create();
		case 2:
			return new DialogBulder(this)
					.setTitle("��ܰ��ʾ")
					.setMessage(short_name + "\n" + "��ӭ����")
					.setButtons("ȷ��", "ȡ��",
							new DialogBulder.OnDialogButtonClickListener() {
								public void onDialogButtonClick(
										Context context, DialogBulder builder,
										Dialog dialog, int dialogId, int which) {
									if (which == BUTTON_LEFT) {
									}
								}
							}).create();
		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	private void loadingButtons() {
		btn_login = (Button) findViewById(R.id.btn_rihgt);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("com.dome.tabhost.change.receiver");
				intent.putExtra("type", 3);
				OneActivity.this.sendBroadcast(intent);

		    }
		});

		tablerow1 = (TableRow) findViewById(R.id.more_page_row1);
		tablerow1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(OneActivity.this, SysInfoTabsActivity.class);
				intent.putExtra("message_type", messageType[0]);
				startActivity(intent);
			}
		});

		tablerow2 = (TableRow) findViewById(R.id.more_page_row2);
		tablerow2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(OneActivity.this, Home_DealActivity.class);

				startActivity(intent);
			}
		});

		tablerow4 = (TableRow) findViewById(R.id.more_page_row4);
		tablerow4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(OneActivity.this, HomeTableRow4.class));
			}
		});

		homeRead = (TableRow) findViewById(R.id.home_read);
		homeRead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(OneActivity.this, ThreeActivity.class));
			}
		});

		homeRules = (TableRow) findViewById(R.id.home_rules);
		homeRules.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(OneActivity.this,
						Home_RulesActivity.class));
			}
		});

		tablerow6 = (TableRow) findViewById(R.id.more_page_row6);
		tablerow6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent(OneActivity.this, HomeTableRowHuanbao.class);

				startActivity(intent);
			}
		});

	}

	private void slidePic() {
		imageResId = new int[] { R.drawable.show1, R.drawable.show2,
				R.drawable.show3, R.drawable.show4, R.drawable.show5 };
		titles = new String[imageResId.length];
		titles[0] = "ͼƬչʾ";
		titles[1] = "ͼƬչʾ";
		titles[2] = "ͼƬչʾ";
		titles[3] = "ͼƬչʾ";
		titles[4] = "ͼƬչʾ";

		imageViews = new ArrayList<ImageView>();

		// ��ʼ��ͼƬ��Դ
		for (int i = 0; i < imageResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imageResId[i]);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
		}

		dots = new ArrayList<View>();
		dots.add(findViewById(R.id.v_dot0));
		dots.add(findViewById(R.id.v_dot1));
		dots.add(findViewById(R.id.v_dot2));
		dots.add(findViewById(R.id.v_dot3));
		dots.add(findViewById(R.id.v_dot4));

		viewpagerpic = (FrameLayout) findViewById(R.id.viewpager_homepic);

		tv_title = (TextView) findViewById(R.id.text_title);
		tv_title.setText(titles[0]);//

		viewPager = (ViewPager) findViewById(R.id.vp);
		viewPager.setAdapter(new MyAdapter());// �������ViewPagerҳ���������
		// ����һ������������ViewPager�е�ҳ��ı�ʱ����
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// ��Activity���ɼ���ʱ��ֹͣ�л�
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/**
	 * �����л�����
	 * 
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				// System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
			}
		}

	}

	/**
	 * ��ViewPager��ҳ���״̬�����ı�ʱ����
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		private int oldPosition = 0;

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			currentItem = position;
			tv_title.setText(titles[position]);
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * ���ViewPagerҳ���������
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageResId.length;
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imageViews.get(arg1));
			return imageViews.get(arg1);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
     UIHelper.exit(OneActivity.this,new Something() {
				
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
	 * ��ȡIMEI
	 * 
	 * @return
	 */
	private String getInfoIMEI() {
		myTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String imei = myTelephonyManager.getDeviceId();
		return imei;
	}

	/**
	 * ��֤imei
	 * 
	 * @param array
	 * @param value
	 * @return
	 */
	private int verificationIMEI(String[] array, String value) {
		int returnnum = -1;
		for (int i = 0; i < array.length; i++) {
			if (value.equals(array[i])) {
				returnnum = i;
			}
		}
		return returnnum;
	}
}
