/**
 * 
 */
package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dome.autoview.AutoScrollView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 显示小时数
 * @author 金航 数据库没内容 未增加数据
 *
 */

public class Vip_Genutilizehour extends Activity {

	public Resources rs;
	private String[] ITEM_NAMES={"2013-2012","2012-2011","2011-2010","2010-2009","2009-2008","2008-2007","2006-2005","2005-2004","2004-2003"};
	private List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
	private ListView lvShow;
	// 自动上滑
	private com.dome.autoview.AutoScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rs = getResources();
		
		setContentView(R.layout.vip_showhours);
		
		
		lvShow = (ListView) findViewById(R.id.lv_table1);
		initData(ITEM_NAMES);
		Button back = (Button)findViewById(R.id.btn_back_hour);
		back.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Vip_Genutilizehour.this.finish();
			}
		});

	}


	/**
	 * 加载数据
	 */
	private void initData(String[] items) {
		for (int i = 0; i < items.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			
			map.put("key", items[i]);
			map.put("data", (123+i)*60 + "");
			datalist.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, datalist,
				R.layout.after_login_pr_table_item, new String[] { "key",
						"data" }, new int[] { R.id.tv_key, R.id.tv_data });
		lvShow.setAdapter(adapter);

	}
	/**
	 * jh 得到数据库表 MktPlan_Repair_Plan 中  Plan_Begin_Time Plan_Begin_TimePlan_End_Time Plan_End_TimePlan_Type
	 * @return 
	 */
	private String getdata(String table,String name){
		return null;

	}

}
