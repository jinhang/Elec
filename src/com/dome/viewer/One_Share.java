
package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;

import com.dome.db.DatabaseHelper;

/**
 * @author jinhang 功能：显示一般的类型用户的合同
 * 
 */
public class One_Share extends Activity {

	public Resources rs;
	SQLiteDatabase database;
	DatabaseHelper db;
	String content[];
	private String[] ITEM_NAMES={"Contract_Id","period","Vendee_Energy","Vendee_Price","Sale_Energy","Sale_Price","Interval_Index","Reg_Settle_Price","Prov_Trans_Price","Reg_Trans_Price","Exist_Contract","Saved_Coal","band","CAP_PRICE","TEST_PRICE","TESTRUN_PRICE1","TESTRUN_PRICE2","VENDEE_NETENERGY","SALE_NETENERGY","Version"};
	private List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
	private ExpandableListView lvShow1;
    String contractid;
	// 返回键
	private Button btn_back;
	List<Map<String, Object>> Groups = new ArrayList<Map<String, Object>>();
	List<Map<String, Object>> childone = new ArrayList<Map<String, Object>>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rs = getResources();
		
		setContentView(R.layout.after_login_pr_tableshow_more_mode);
		
		lvShow1 = (ExpandableListView) findViewById(R.id.expandableListView_pri);
		lvShow1.setGroupIndicator(null);
        //得到传递数据，用 传来的Contract_id得到详细合同
		Intent aa = this.getIntent();
		Bundle r = aa.getExtras();
		contractid= r.getString("contract_id");
        System.out.println("接收到"+contractid);
		// Groups 分组
		Map<String, Object> groupone = new HashMap<String, Object>();
		groupone.put("groupname", contractid);
		Groups.add(groupone);
	   // 定义两个List<Map<String,Object>> childone和childtwo
		// 为二级条目提供数据
		// childone
		havedata();
		// 二级Listview
		List<List<Map<String, Object>>> Childs = new ArrayList<List<Map<String, Object>>>();
		Childs.add(childone);
		SimpleExpandableListAdapter simpleExpandListAdapter = new SimpleExpandableListAdapter(
				One_Share.this, Groups,
				R.layout.private_elistview_group, new String[] { "groupname" },
				new int[] { R.id.pri_group }, Childs,
				R.layout.private_elistview_child,
				new String[] { "ID", "name" }, new int[] { R.id.pri_tv_key,
						R.id.pri_tv_data });

		lvShow1.setAdapter(simpleExpandListAdapter);
        btn_back = (Button) findViewById(R.id.btn_back_pri_only);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				One_Share.this.finish();
			}
		});

	}
/**
 * 从数据库获取数据
 */
	private void havedata() {
		// TODO Auto-generated method stub
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("已打开数据库");
		
		String sql3="select * from mkttrade_contract_energyitems where contract_id='"+contractid+"'";
		Cursor s = database.rawQuery(sql3,null);
	    System.out.println("准备赋值");
		int m=0;
		while (s.moveToNext()) {
			System.out.println("赋值中。。。");
			
				Map<String, Object> map1 = new HashMap<String, Object>();
				map1.put("ID", "Contract_Id");
				map1.put("name",s.getString(s.getColumnIndex("Contract_Id")));
				childone.add(map1);
				
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("ID", "Period");
				map2.put("name",s.getString(s.getColumnIndex("Period")));
				childone.add(map2);
				Map<String, Object> map3 = new HashMap<String, Object>();
				map3.put("ID", "Vendee_Energy");
				map3.put("name",s.getString(s.getColumnIndex("Vendee_Energy")));
				childone.add(map3);
				Map<String, Object> map4 = new HashMap<String, Object>();
				map4.put("ID", "Vendee_Price");
				map4.put("name",s.getString(s.getColumnIndex("Vendee_Price")));
				childone.add(map4);
				Map<String, Object> map5 = new HashMap<String, Object>();
				map5.put("ID", "Sale_Energy");
				map5.put("name",s.getString(s.getColumnIndex("Sale_Energy")));
				childone.add(map5);
				Map<String, Object> map6 = new HashMap<String, Object>();
				map6.put("ID", "Sale_Price");
				map6.put("name",s.getString(s.getColumnIndex("Sale_Price")));
				childone.add(map6);
				Map<String, Object> map7 = new HashMap<String, Object>();
				map7.put("ID", "Interval_Index");
				map7.put("name",s.getString(s.getColumnIndex("Interval_Index")));
				childone.add(map7);
				Map<String, Object> map8 = new HashMap<String, Object>();
				map8.put("ID", "Reg_Settle_Price");
				map8.put("name",s.getString(s.getColumnIndex("Reg_Settle_Price")));
				childone.add(map8);
				Map<String, Object> map9 = new HashMap<String, Object>();
				map9.put("ID", "Prov_Trans_Price");
				map9.put("name",s.getString(s.getColumnIndex("Prov_Trans_Price")));
				childone.add(map9);
				Map<String, Object> map10 = new HashMap<String, Object>();
				map10.put("ID", "Reg_Trans_Price");
				map10.put("name",s.getString(s.getColumnIndex("Reg_Trans_Price")));
				childone.add(map10);
				Map<String, Object> map11= new HashMap<String, Object>();
				map11.put("ID", "Exist_Contract");
				map11.put("name",s.getString(s.getColumnIndex("Exist_Contract")));
				childone.add(map11);
				Map<String, Object> map12= new HashMap<String, Object>();
				map12.put("ID", "Saved_Coal");
				map12.put("name",s.getString(s.getColumnIndex("Saved_Coal")));
				childone.add(map12);
				Map<String, Object> map13= new HashMap<String, Object>();
				map13.put("ID", "band");
				map13.put("name",s.getString(s.getColumnIndex("band")));
				childone.add(map13);
				Map<String, Object> map14= new HashMap<String, Object>();
				map14.put("ID", "CAP_PRICE");
				map14.put("name",s.getString(s.getColumnIndex("CAP_PRICE")));
				childone.add(map14);
				Map<String, Object> map15= new HashMap<String, Object>();
				map15.put("ID", "TEST_PRICE");
				map15.put("name",s.getString(s.getColumnIndex("TEST_PRICE")));
				childone.add(map15);
				Map<String, Object> map16= new HashMap<String, Object>();
				map16.put("ID", "TESTRUN_PRICE1");
				map16.put("name",s.getString(s.getColumnIndex("TESTRUN_PRICE1")));
				childone.add(map16);
				Map<String, Object> map17= new HashMap<String, Object>();
				map17.put("ID", "TESTRUN_PRICE2");
				map17.put("name",s.getString(s.getColumnIndex("TESTRUN_PRICE2")));
				childone.add(map17);
				Map<String, Object> map18= new HashMap<String, Object>();
				map18.put("ID", "VENDEE_NETENERGY");
				map18.put("name",s.getString(s.getColumnIndex("VENDEE_NETENERGY")));
				childone.add(map18);
				Map<String, Object> map19= new HashMap<String, Object>();
				map19.put("ID", "SALE_NETENERGY");
				map19.put("name",s.getString(s.getColumnIndex("SALE_NETENERGY")));
				childone.add(map19);
				Map<String, Object> map20= new HashMap<String, Object>();
				map20.put("ID", "Version");
				map20.put("name",s.getString(s.getColumnIndex("Version")));
				childone.add(map20);
				m++;
	    }
	    db.closeDatabase();
		System.out.println("已关闭数据库");
	}
}
