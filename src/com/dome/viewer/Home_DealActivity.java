package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dome.db.DatabaseHelper;

/**
 * ��ҳ�ڶ�������
 * 
 * @author ����ԭ
 * 
 */
public class Home_DealActivity extends Activity {

	private Button btn_back;
	private ListView list;
	// ���ݿ�
	private SQLiteDatabase database;
	private DatabaseHelper db;

	// �����id
	public static String[] ITEM_ID = new String[] {};

	// ��������
	public static String[] ITEM_NAMES = new String[] { "����Ӫ״����һ����ת",
			"������ҵ�������˹���÷�չ", "������ҵ�ڸ�����չ�����������", "�������ǡ��߳�ȥ����������", "���� ͦ����Ҽ���",
			"�����������ٷŻ����Ͼ��û�����", "�й�����ҵ������һ�ֵ�����", "֪ͨ��Ϣ", "֪ͨ��Ϣ", "֪ͨ��Ϣ",
			"֪ͨ��Ϣ", "֪ͨ��Ϣ", "֪ͨ��Ϣ", "֪ͨ��Ϣ", "֪ͨ��Ϣ", };

	// ��Ŀ��ʱ��
	public static String[] ITEM_TIME = new String[] { "��2013-7-17��",
			"��2013-7-16��", "��2013-7-15��", "��2013-7-14��", "��2013-7-13��",
			"��2013-7-12��", "��2013-7-17��", "��2013-7-17��", "��2013-7-17��",
			"��2013-7-17��", "��2013-7-17��", "��2013-7-17��", "��2013-7-17��",
			"��2013-7-17��", "��2013-7-17��", "��2013-7-17��", "��2013-7-17��",
			"��2013-7-17��", "��2013-7-17��",

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_dealactivity);

		getListInfo();

		loadingButtons();

	}

	/**
	 * �����ݿ��ȡ��������Ϣ ---�б������
	 * 
	 * @author Ternence
	 */
	private void getListInfo() {
		db = new DatabaseHelper(this);
		database = db.openDatabase();

		Cursor c = database
				.rawQuery(
						"select Pub_Id,Topic,Pub_Time from  System_PubInfo where Type_ID=4",
						null);
		ITEM_ID = null;
		ITEM_NAMES = null;
		ITEM_TIME = null;
		ITEM_ID = new String[c.getCount()];
		ITEM_NAMES = new String[c.getCount()];
		ITEM_TIME = new String[c.getCount()];

		int i = 0;
		while (c.moveToNext()) {
			ITEM_ID[i] = c.getString(c.getColumnIndex("Pub_Id"));
			ITEM_NAMES[i] = c.getString(c.getColumnIndex("Topic"));
			ITEM_TIME[i] = c.getString(c.getColumnIndex("Pub_Time"));
			System.out.println("id + " + ITEM_ID[i] + "name " + ITEM_NAMES[i]
					+ "time" + ITEM_TIME[i]);
			i++;
		}
		db.closeDatabase();
	}

	/**
	 * ���ؿؼ�
	 */
	private void loadingButtons() {
		btn_back = (Button) findViewById(R.id.btn_home_left);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Home_DealActivity.this.finish();
			}
		});

		list = (ListView) findViewById(R.id.list_home_row2);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.umeng_xp_banner, new String[] { "text", "time" },
				new int[] { R.id.umeng_xp_name0, R.id.umeng_xp_content0, });

		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("id", ITEM_ID[arg2]);
				intent.putExtras(bundle);
				intent.setClass(Home_DealActivity.this, DealInfoActivity.class);
				startActivity(intent);

			}
		});
	}

	/**
	 * ����������
	 * 
	 * @return
	 */
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < ITEM_NAMES.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("text", ITEM_NAMES[i]);
			map.put("time", ITEM_TIME[i]);
			list.add(map);
		}

		return list;
	}

}
