package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dome.autoview.AutoScrollView;
import com.dome.db.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 功能：显示机组维修计划
 * @author JINHANG 
 **/
public class Vip_RepairePlan extends Activity{
	SQLiteDatabase database;
	DatabaseHelper db;
	private String[] ITEM_NAMES={"Generator_Id","Repair_Id","Device_Type","Repair_Type","Reduce_Capacity","Plan_Begin_Time","Plan_End_Time","Plan_Type","Plan_Date","Rrpair_Reason","Content","Update_Time","Version"};
	String content[]=new String[20];
	private List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
	private ListView lvShow;
	Button back;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vip_showrepaireplan);
		lvShow = (ListView) findViewById(R.id.lv_plan);
		initData(ITEM_NAMES);
		back = (Button)findViewById(R.id.btn_back_plan);
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			   	Vip_RepairePlan.this.finish();
			}
		});
	}
	/**
	 * 功能:显示维修计划详细内容
	 * @param iTEM
	 */
	private void initData(String[] iTEM) {
		// TODO Auto-generated method stub
		Intent myintent = this.getIntent();
		Bundle bundlePri = myintent.getExtras();
		String repair_id = bundlePri.getString("repair_id");
		System.out.println(repair_id);
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("已打开数据库");
		String sql="select distinct * from MktPlan_Repair_Plan  where repair_id='"+repair_id+"'";
		Cursor c2 = database.rawQuery(sql,null);
		System.out.println("准备赋值");
		while(c2.moveToNext())
		{

			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("key", "机组编号");
			map1.put("data", c2.getString(c2.getColumnIndex("Generator_Id")));
			datalist.add(map1);

			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("key", "维修编号");
			map2.put("data", c2.getString(c2.getColumnIndex("Repair_Id")));
			datalist.add(map2);
			HashMap<String, String> map3= new HashMap<String, String>();
			map3.put("key", "设备类型");
			map3.put("data", c2.getString(c2.getColumnIndex("Device_Type")));
			datalist.add(map3);

			HashMap<String, String> map4= new HashMap<String, String>();
			map4.put("key", "维修类型");
			map4.put("data", c2.getString(c2.getColumnIndex("Repair_Type")));
			datalist.add(map4);

			HashMap<String, String> map5= new HashMap<String, String>();
			map5.put("key", "减少容量");
			map5.put("data", c2.getString(c2.getColumnIndex("Reduce_Capacity")));
			datalist.add(map5);

			HashMap<String, String> map6= new HashMap<String, String>();
			map6.put("key", "开始时间");
			map6.put("data", c2.getString(c2.getColumnIndex("Plan_Begin_Time")));

			datalist.add(map6);
			HashMap<String, String> map7= new HashMap<String, String>();
			map7.put("key", "终结时间");
			map7.put("data", c2.getString(c2.getColumnIndex("Plan_End_Time")));
			datalist.add(map7);

			HashMap<String, String> map8= new HashMap<String, String>();
			map8.put("key", "计划类型");
			map8.put("data", c2.getString(c2.getColumnIndex("Plan_Type")));
			datalist.add(map8);

			HashMap<String, String> map9= new HashMap<String, String>();
			map9.put("key", "计划日期");
			map9.put("data", c2.getString(c2.getColumnIndex("Plan_Date")));
			datalist.add(map9);

			HashMap<String, String> map10= new HashMap<String, String>();
			map10.put("key", "维修原因");
			map10.put("data", c2.getString(c2.getColumnIndex("Repair_Reason")));
			datalist.add(map10);
			HashMap<String, String> map11= new HashMap<String, String>();
			map11.put("key", "内容");
			map11.put("data", c2.getString(c2.getColumnIndex("Content")));
			datalist.add(map11);
			HashMap<String, String> map12= new HashMap<String, String>();
			map12.put("key", "日期");
			map12.put("data", c2.getString(c2.getColumnIndex("Update_Time")));
			datalist.add(map12);

		}
		db.closeDatabase();
		SimpleAdapter adapter = new SimpleAdapter(this, datalist,
				R.layout.after_login_pr_table_item, new String[] { "key",
		"data" }, new int[] { R.id.tv_key, R.id.tv_data });
		lvShow.setAdapter(adapter);
	}
}
