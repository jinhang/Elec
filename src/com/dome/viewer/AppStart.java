package com.dome.viewer;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AppStart extends Activity {


	
	
    private boolean HASDOWN=false;
	// 判断是否第一次启动
	private boolean firstTime;
	// 数据库文件目录
	public static final String DB_NAME = "db_dome.db";
	public static final String PACKAGE_NAME = "com.dome.viewer";
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // 在手机里存放数据库的位置

	public static final int DURING_TIME_INCREMENT = 1200;// 开机画面停留的时间数


	

	private boolean isRigisted = false;



	// 广播接收

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.appstart, null);
		
		setContentView(view);

	

		

		

		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);

		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			
				//i = System.currentTimeMillis();
				// 动画开始的时候进行发送广播，进行数据的更新
				Intent intent = new Intent();
				intent.setAction("com.dome.update.DataReceiver");
				sendBroadcast(intent);
			}
		});
	}

	

	private void redirectTo() {
		// 此处可以导航栏
		// Intent intent = new Intent(this, Whatsnew.class);

		Intent intent = new Intent(this, TabHostActivity.class);
		startActivity(intent);
		finish();
	}

	
	

	

}
