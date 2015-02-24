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
 * ���ܣ���ʾ����ά�޼ƻ�
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
		// �����ޱ���
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
	 * ����:��ʾά�޼ƻ���ϸ����
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
		System.out.println("�Ѵ����ݿ�");
		String sql="select distinct * from MktPlan_Repair_Plan  where repair_id='"+repair_id+"'";
		Cursor c2 = database.rawQuery(sql,null);
		System.out.println("׼����ֵ");
		while(c2.moveToNext())
		{

			HashMap<String, String> map1 = new HashMap<String, String>();
			map1.put("key", "������");
			map1.put("data", c2.getString(c2.getColumnIndex("Generator_Id")));
			datalist.add(map1);

			HashMap<String, String> map2 = new HashMap<String, String>();
			map2.put("key", "ά�ޱ��");
			map2.put("data", c2.getString(c2.getColumnIndex("Repair_Id")));
			datalist.add(map2);
			HashMap<String, String> map3= new HashMap<String, String>();
			map3.put("key", "�豸����");
			map3.put("data", c2.getString(c2.getColumnIndex("Device_Type")));
			datalist.add(map3);

			HashMap<String, String> map4= new HashMap<String, String>();
			map4.put("key", "ά������");
			map4.put("data", c2.getString(c2.getColumnIndex("Repair_Type")));
			datalist.add(map4);

			HashMap<String, String> map5= new HashMap<String, String>();
			map5.put("key", "��������");
			map5.put("data", c2.getString(c2.getColumnIndex("Reduce_Capacity")));
			datalist.add(map5);

			HashMap<String, String> map6= new HashMap<String, String>();
			map6.put("key", "��ʼʱ��");
			map6.put("data", c2.getString(c2.getColumnIndex("Plan_Begin_Time")));

			datalist.add(map6);
			HashMap<String, String> map7= new HashMap<String, String>();
			map7.put("key", "�ս�ʱ��");
			map7.put("data", c2.getString(c2.getColumnIndex("Plan_End_Time")));
			datalist.add(map7);

			HashMap<String, String> map8= new HashMap<String, String>();
			map8.put("key", "�ƻ�����");
			map8.put("data", c2.getString(c2.getColumnIndex("Plan_Type")));
			datalist.add(map8);

			HashMap<String, String> map9= new HashMap<String, String>();
			map9.put("key", "�ƻ�����");
			map9.put("data", c2.getString(c2.getColumnIndex("Plan_Date")));
			datalist.add(map9);

			HashMap<String, String> map10= new HashMap<String, String>();
			map10.put("key", "ά��ԭ��");
			map10.put("data", c2.getString(c2.getColumnIndex("Repair_Reason")));
			datalist.add(map10);
			HashMap<String, String> map11= new HashMap<String, String>();
			map11.put("key", "����");
			map11.put("data", c2.getString(c2.getColumnIndex("Content")));
			datalist.add(map11);
			HashMap<String, String> map12= new HashMap<String, String>();
			map12.put("key", "����");
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
