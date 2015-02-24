package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dome.db.DatabaseHelper;

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
/**
 * 功能：法律法规
 * @author 张藤原 
 *
 */
public class Home_RulesActivity extends Activity {

	//布局
	private Button btn_back;
	private ListView list_content;
	//数据库操作使用
	private DatabaseHelper db;
	private SQLiteDatabase database;
	//获取的数据
	private String[] ITEM_ID = new String[] {};
	private String[] ITEM_NAMES = new String[] {};
	private String[] ITEM_TIME = new String[] {};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_rulectivity);

		getListInfo();
		init();
	}

	/**
	 * 加载控件
	 * 
	 * @author Ternence
	 * 
	 */
	private void init() {
		btn_back = (Button) findViewById(R.id.btn_home_left_rule);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Home_RulesActivity.this.finish();
			}
		});

		list_content = (ListView) findViewById(R.id.list_home_rule);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.rule_banner, new String[] { "text", "time" },
				new int[] { R.id.umeng_xp_name0, R.id.umeng_xp_content0, });

		list_content.setAdapter(adapter);

		list_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("id", ITEM_ID[arg2]);
				intent.putExtras(bundle);
				intent.setClass(Home_RulesActivity.this, RuleInfoActivity.class);
				startActivity(intent);
			}

		});
	}

	/**
	 * 从数据库获取到交易信息 ---列表的数据
	 * 
	 * @author Ternence
	 */
	private void getListInfo() {
		db = new DatabaseHelper(this);
		database = db.openDatabase();

		Cursor c = database
				.rawQuery(
						"select Pub_Id,Topic,Pub_Time from  System_PubInfo where Type_ID=5",
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

		for (int i1 = 0; i1 < ITEM_ID.length; i1++) {
			System.out.println("ITEM_ID[i]---->" + ITEM_ID[i1]);
		}
		for (int i1 = 0; i1 < ITEM_NAMES.length; i1++) {
			System.out.println("ITEM_NAMES[i]---->" + ITEM_NAMES[i1]);
		}
		for (int i1 = 0; i1 < ITEM_TIME.length; i1++) {
			System.out.println("ITEM_TIME[i]---->" + ITEM_TIME[i1]);
		}
	}

	/**
	 * 加载适配器
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
