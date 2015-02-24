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
 * 主页第二栏内容
 * 
 * @author 张藤原
 * 
 */
public class Home_DealActivity extends Activity {

	private Button btn_back;
	private ListView list;
	// 数据库
	private SQLiteDatabase database;
	private DatabaseHelper db;

	// 标题的id
	public static String[] ITEM_ID = new String[] {};

	// 标题名字
	public static String[] ITEM_NAMES = new String[] { "国企经营状况进一步好转",
			"国有企业引领俄罗斯经济发展", "国有企业在各国发展中起过的作用", "国企仍是“走出去”的主力军", "创新 挺起国家脊梁",
			"国企利润增速放缓符合经济基本面", "中国风电产业步入新一轮调整期", "通知信息", "通知信息", "通知信息",
			"通知信息", "通知信息", "通知信息", "通知信息", "通知信息", };

	// 条目的时间
	public static String[] ITEM_TIME = new String[] { "【2013-7-17】",
			"【2013-7-16】", "【2013-7-15】", "【2013-7-14】", "【2013-7-13】",
			"【2013-7-12】", "【2013-7-17】", "【2013-7-17】", "【2013-7-17】",
			"【2013-7-17】", "【2013-7-17】", "【2013-7-17】", "【2013-7-17】",
			"【2013-7-17】", "【2013-7-17】", "【2013-7-17】", "【2013-7-17】",
			"【2013-7-17】", "【2013-7-17】",

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home_dealactivity);

		getListInfo();

		loadingButtons();

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
	 * 加载控件
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
