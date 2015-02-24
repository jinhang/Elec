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
 * @author ��Ȼ
 * 
 * 
 * @author Ternence �Ķ�-----��ȡ������Ϣ����
 */
public class HuanbaoLookInfoActivity extends Activity {

	private TextView tvTitle;
	private TextView tvTime;
	private TextView tvContent;
	private TextView tvATTname;
	private TextView tvPublisher;
	private TextView tvChecker;

	private Button btnReturn;
//	private static SystemPubInfo info =HomeTableRowHuanbao.info_temp;

	// ��ȡ���Ľ�����Ϣ��id
	private String ID = "";
	// ���ݿ�
	private SQLiteDatabase database;
	private DatabaseHelper db;
	private String Topic;
	private String Pub_Time;
	private String Pub_Info;
	private String Publisher;
	private String Reviewer;

	// ��������
	private TextView title_top;
	private String Type_Name;
	private int Choose_ID;
	private String Pub_ID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.info_layout);
		System.out.println("��ת������  ---huanbaolookinfo");

		// intent ��ȡbundle ������idֵ
		Intent myintent = this.getIntent();
		Bundle mybundle = myintent.getExtras();
		Type_Name = mybundle.getString("TypeName");
		System.out.println("type��id:" + Type_Name);
		Choose_ID = mybundle.getInt("ChooseListId");
		System.out.println("Choose_ID��id:" + Choose_ID);
		Pub_ID = mybundle.getString("pubid");
		System.out.println("Pub_ID��id:" + Pub_ID);

		// ��������
		title_top = (TextView) findViewById(R.id.title_top);
		title_top.setText("������Ϣ");
		// ��ȡ�����ؼ�
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvATTname = (TextView) findViewById(R.id.tv_attname);
		tvPublisher = (TextView) findViewById(R.id.tv_publisher);
		tvChecker = (TextView) findViewById(R.id.tv_checker);

		// getDealDataFromDatabase();
		// tvTitle.setText(Topic);
		// tvTime.setText(Pub_Time);
		// tvPublisher.setText(Publisher);
		// tvChecker.setText(Reviewer);
		// tvContent.setText(Pub_Info);

		getDealDataFromDatabase();
		
		tvPublisher.setText(Publisher);
		tvChecker.setText(Reviewer);
		tvTitle.setText(Topic);
		tvTime.setText(Pub_Time);
		tvContent.setText(Pub_Info);
//		tvATTname.setText(info.getFile_Name());
		btnReturn = (Button) findViewById(R.id.btn_home_left);
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	/**
	 * ���ݿ��ȡ������Ϣ�ľ�������
	 */
	private void getDealDataFromDatabase() {
		db = new DatabaseHelper(this);
		database = db.openDatabase();

		String sql = null;
		// ����ѡ���tab ����sql
		if (Type_Name.equals("1")) {
			System.out.println("---1");
			sql = "select Topic,Pub_Time,Pub_Info,Publisher,Reviewer from  System_PubInfo where Pub_Id='"
					+ Pub_ID + "' and Type_ID=8";
		} else if (Type_Name.equals("2")) {
			System.out.println("---2");
			sql = "select Topic,Pub_Time,Pub_Info,Publisher,Reviewer from  System_PubInfo where Pub_Id='"
					+ Pub_ID + "' and Type_ID=7";
		} else if (Type_Name.equals("3")) {
			System.out.println("---3");
			sql = "select Topic,Pub_Time,Pub_Info,Publisher,Reviewer from  System_PubInfo where Pub_Id='"
					+ Pub_ID + "' and Type_ID=9";
		} else if (Type_Name.equals("4")) {
			System.out.println("---4");
			sql = "select Topic,Pub_Time,Pub_Info,Publisher,Reviewer from  System_PubInfo where Pub_Id='"
					+ Pub_ID + "' and Type_ID=10";
		}

		Cursor c = database.rawQuery(sql, null);
		Topic = null;
		Pub_Time = null;
		Pub_Info = null;
		Publisher = null;
		Reviewer = null;

		while (c.moveToNext()) {
			System.out.println("ִ��sql");
			Topic = c.getString(c.getColumnIndex("Topic"));
			Pub_Time = c.getString(c.getColumnIndex("Pub_Time"));
			Pub_Info = c.getString(c.getColumnIndex("Pub_Info"));
			Publisher = c.getString(c.getColumnIndex("Publisher"));
			Reviewer = c.getString(c.getColumnIndex("Reviewer"));
			System.out.println("topic" + Topic + "pubtime" + Pub_Time
					+ "pubinfo" + Pub_Info);
		}
		c.close();
		c = null;
		db.closeDatabase();
	}

	// һ�����������ص�ʱ����н������ĸ��µ��첽���ص���
	class DownLoader extends AsyncTask<URL, Integer, Long> {
		String temp = null;

		@Override
		protected Long doInBackground(URL... arg0) {

			return null;
		}

	}

}
