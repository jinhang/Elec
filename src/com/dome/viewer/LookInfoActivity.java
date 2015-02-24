/**
 * 
 */
package com.dome.viewer;

import java.net.URL;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dome.db.DatabaseHelper;
import com.dome.httpdao.SystemPubInfo;

/**
 * @author 浩然
 * 
 * 
 * @author Ternence 改动-----获取交易信息内容
 */
public class LookInfoActivity extends Activity {

	private TextView tvTitle;
	private TextView tvTime;
	private TextView tvContent;
	private TextView tvATTname;
	private TextView tvPublisher;
	private TextView tvChecker;

	private Button btnReturn;
	private SystemPubInfo info = SysInfoTabsActivity.info_temp;

	// 获取到的交易信息的id
	private String ID = "";
	// 数据库
	private SQLiteDatabase database;
	private DatabaseHelper db;
	private String Topic;
	private String Pub_Time;
	private String Pub_Info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_layout);

		// // intent 获取bundle 传来的id值
		// Intent myintent = this.getIntent();
		// Bundle mybundle = myintent.getExtras();
		// ID = mybundle.getString("id");
		// System.out.println("从环保信息传来的id:" + ID);

		// 获取各个控件
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvATTname = (TextView) findViewById(R.id.tv_attname);
		tvPublisher = (TextView) findViewById(R.id.tv_publisher);
		tvChecker = (TextView) findViewById(R.id.tv_checker);

		tvPublisher.setText(info.getPublisher());
		tvChecker.setText(info.getReviewer());
		tvTitle.setText(info.getTopic());
		tvTime.setText(info.getPub_Time());
		tvContent.setText(info.getPub_Info());
		tvATTname.setText(info.getFile_Name());

		btnReturn = (Button) findViewById(R.id.btn_home_left);
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	/**
	 * 数据库获取交易信息的具体内容
	 */
	private void getDealDataFromDatabase() {
		db = new DatabaseHelper(this);
		database = db.openDatabase();

		Cursor c = database
				.rawQuery(
						"select Topic,Pub_Time,Pub_Info,Publisher,Reviewer from  System_PubInfo where Pub_Id='"
								+ ID + "' and Type_ID=4", null);
		Topic = null;
		Pub_Time = null;
		Pub_Info = null;

		while (c.moveToNext()) {
			Topic = c.getString(c.getColumnIndex("Topic"));
			Pub_Time = c.getString(c.getColumnIndex("Pub_Time"));
			Pub_Info = c.getString(c.getColumnIndex("Pub_Info"));
			System.out.println("topic" + Topic + "pubtime" + Pub_Time
					+ "pubinfo" + Pub_Info);
		}
		c.close();
		c = null;
		database = db.openDatabase();
	}

	// 一个用于在下载的时候进行进度条的更新的异步加载的类
	class DownLoader extends AsyncTask<URL, Integer, Long> {
		String temp = null;

		@Override
		protected Long doInBackground(URL... arg0) {

			return null;
		}

	}

}
