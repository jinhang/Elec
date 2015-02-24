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
 * 首页显示
 * 
 * @author 张藤原
 * 
 */
public class OneActivity extends Activity {

	// *****tablerow1-4******
	/**
	 * 通知信息
	 */
	private TableRow tablerow1;
	/**
	 * 交易信息
	 */
	private TableRow tablerow2;
	private TableRow tablerow4;
	private TableRow homeRules;
	private TableRow tablerow6;
	/**
	 * 年报阅览
	 */
	private TableRow homeRead;

	// 登陆******
	private Button btn_login;

	// *********************滑动图片****************************************
	private FrameLayout viewpagerpic;
	private ViewPager viewPager; // android-support-v4中的滑动组件
	private List<ImageView> imageViews; // 滑动的图片集合

	private String[] titles; // 图片标题
	private int[] imageResId; // 图片ID
	private List<View> dots; // 图片标题正文的那些点

	private TextView tv_title;
	private int currentItem = 0; // 当前图片的索引号

	private Intent intent;

	String[] messageType;

	Resources rs;

	// An ExecutorService that can schedule commands to run after a given delay,
	// or to execute periodically.
	private ScheduledExecutorService scheduledExecutorService;

	// 切换当前显示的图片
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
		};
	};

	// *********************滑动图片****************************************

	// 获取imei
	TelephonyManager myTelephonyManager;
	// IMEI
	private String IMEI = null;
	// database
	SQLiteDatabase database;
	DatabaseHelper db = new DatabaseHelper(this);

	// 返回的Imei 数组
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
		slidePic(); // 滑动图片 main

	}

	/**
	 * 弹出对话框显示用户是否注册
	 */
	private void showPersonlyWeclcom() {
		database = db.openDatabase();
		System.out.println("已经打开数据库连接，开始核对id");
		String sql = "SELECT participant_shortname as name ,t1.Participant_Id FROM  LogIn t1,mktadmin_participants t2 where t1.participant_id=t2.participant_id";
		Cursor c = database.rawQuery(sql, null);
		while (c.moveToNext()) {
			id = c.getString(c.getColumnIndex("Participant_Id"));
			short_name = c.getString(c.getColumnIndex("name"));
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
		// 判断用户身份
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
			return new DialogBulder(this).setTitle("标题").setMessage("内容")
					.create();
		case 1:
			return new DialogBulder(this)
					.setTitle("温馨提示")
					.setMessage("您还没有注册！")
					.setButtons("确定", "取消",
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
					.setTitle("温馨提示")
					.setMessage(short_name + "\n" + "欢迎您！")
					.setButtons("确定", "取消",
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
		titles[0] = "图片展示";
		titles[1] = "图片展示";
		titles[2] = "图片展示";
		titles[3] = "图片展示";
		titles[4] = "图片展示";

		imageViews = new ArrayList<ImageView>();

		// 初始化图片资源
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
		viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	@Override
	protected void onStart() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
				TimeUnit.SECONDS);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	/**
	 * 换行切换任务
	 * 
	 * 
	 */
	private class ScrollTask implements Runnable {

		public void run() {
			synchronized (viewPager) {
				// System.out.println("currentItem: " + currentItem);
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}

	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
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
	 * 填充ViewPager页面的适配器
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
	 * 获取IMEI
	 * 
	 * @return
	 */
	private String getInfoIMEI() {
		myTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String imei = myTelephonyManager.getDeviceId();
		return imei;
	}

	/**
	 * 验证imei
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
