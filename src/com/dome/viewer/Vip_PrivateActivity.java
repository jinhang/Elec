/**
 * 
 */
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.dome.db.DatabaseHelper;

/**
 * @author  张藤原 功能：获取电量信息
 * 
 */
public class Vip_PrivateActivity extends Activity {

	public Resources rs;
	private String[] ITEM_NAMES;

	private ExpandableListView lvShow1;
	private TextView title;
	// 返回
	private Button btn_back;

	private Bundle bundlePri;
	// 根据selectnum 选择要获取的数据
	// 数据库
	SQLiteDatabase database;
	DatabaseHelper db;
	private static int SELECTNUM;
	private static String POWERNAME;
	private static String POWERYear;
	double[] MONTHS = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
	double[] ELEC = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	double[] ELEC_PLAN = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private Map<String, Object> childonedateone;
	// 选择的列表id 用于查看详细月电量
	private String Month_Id = "01";
	private static String[][] ElecTpyeId = new String[][] {};
	private static String[][] TpyeElec = new String[][] {};
	private String[] ElecTpyeId_temp;
	private String[] TpyeElec_temp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rs = getResources();
		ITEM_NAMES = rs.getStringArray(R.array.tablemodel);
		setContentView(R.layout.after_login_pr_tableshow);
		title = (TextView) findViewById(R.id.vip_every_title);
		System.out.println("这是priact");
		// 取得绑定编号
		Intent myintent = this.getIntent();
		Bundle bundlePri = myintent.getExtras();
		SELECTNUM = bundlePri.getInt("sqlSelect");
		POWERNAME = bundlePri.getString("PowerName");
		POWERYear = bundlePri.getString("PowerYear");
		System.out.println("选择的列表-->" + SELECTNUM + " 电厂名字：--->" + POWERNAME
				+ "年份 ：---〉" + POWERYear);

		lvShow1 = (ExpandableListView) findViewById(R.id.lv_table_1);

		// lvShow1.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// Month_Id = "0" + arg2 + 1;
		// System.out.println("查看的是--->" + Month_Id);
		// initData();
		// }
		// });

		// 调用数据库信息
		getSqlInfo();

		// 设配信息
		initData();

		// 返回
		btn_back = (Button) findViewById(R.id.btn_back_pub);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Vip_PrivateActivity.this.finish();
			}
		});

	}

	private void getSqlInfo() {
		switch (SELECTNUM) {
		case 0:
			title.setText("上网电量");
			db = new DatabaseHelper(this);
			database = db.openDatabase();
			System.out.println("已打开数据库");
			System.out.println("用户选择------>" + POWERNAME);
			System.out.println("用户选择------>" + POWERYear);

			Cursor c2 = database
					.rawQuery(
							"select distinct strftime('%m',t2.mkt_date) as month,t2.energy as energy from MktAdmin_BidUnits t1,MktSbs_Result_Type t2 where t1.bidunit_shortname='"
									+ POWERNAME
									+ "' and t1.bidunit_id= t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date)='"
									+ POWERYear
									+ "' and tradetype_id=0 order by month",
							null);
			MONTHS = null;
			ELEC = null;
			MONTHS = new double[c2.getCount()];
			ELEC = new double[c2.getCount()];

			int l = 0;
			System.out.println("准备赋值MONTHS");
			if (null != c2)
				while (c2.moveToNext()) {
					System.out.println("MONTHS赋值中。。。");
					MONTHS[l] = c2.getDouble(c2.getColumnIndex("month"));
					System.out.println("得到MONTHS---->" + MONTHS[l]);
					System.out.println("ELEC赋值中。。。");
					ELEC[l] = c2.getDouble(c2.getColumnIndex("energy"));
					String afa = c2.getString(c2.getColumnIndex("energy"))
							+ " ";
					System.out.println(afa);
					System.out.println("得到---->" + ELEC[l]);
					l++;
				}
			if (null != c2) {
				c2.close();
				c2 = null;
			}
			db.closeDatabase();
			System.out.println("已关闭数据库");
			System.out.println("已得到绘图属性");

			break;

		case 1:
			title.setText("月度电量计划");
			// db = new DatabaseHelper(this);
			// database = db.openDatabase();
			// System.out.println("已打开数据库");
			// System.out.println("用户选择------>" + POWERNAME);
			// System.out.println("用户选择------>" + POWERYear);
			//
			// Cursor c21 = database
			// .rawQuery(
			// "select  strftime('%m',t2.mkt_date) as month,t2.net_energy as energy from MktAdmin_BidUnits t1,MktPlan_Gen_DecomEnergy_Item t2 where  t1.bidunit_id= t2.bidunit_id and t1.bidunit_shortname='"
			// + POWERNAME
			// + "' and strftime('%Y',t2.mkt_date)='"
			// + POWERYear
			// + "'and t2.decom_type=2 and t2.Trade_Id='100007000000194'",
			// null);
			// MONTHS = null;
			// ELEC = null;
			// MONTHS = new double[c21.getCount()];
			// ELEC = new double[c21.getCount()];
			// int l1 = 0;
			// System.out.println("准备赋值MONTHS");
			// if (null != c21)
			// while (c21.moveToNext()) {
			// System.out.println("MONTHS赋值中。。。");
			// MONTHS[l1] = c21.getDouble(c21.getColumnIndex("month"));
			// System.out.println("得到---->" + MONTHS[l1]);
			//
			// System.out.println("ELEC赋值中。。。");
			// ELEC[l1] = c21.getDouble(c21.getColumnIndex("energy"));
			// System.out.println("得到---->" + ELEC[l1]);
			// l1++;
			// }
			// if (null != c21) {
			// c21.close();
			// c21 = null;
			// }
			// db.closeDatabase();
			// System.out.println("已关闭数据库");
			// System.out.println("开始进行月度电量计划按月份排序！");
			//
			// for (int i = 0; i < MONTHS.length - 1; i++) {
			// for (int j = i + 1; j < MONTHS.length; j++) {
			// if (MONTHS[i] > MONTHS[j]) {
			// double temp = MONTHS[i];
			// MONTHS[i] = MONTHS[j];
			// MONTHS[j] = temp;
			// temp = ELEC[i];
			// ELEC[i] = ELEC[j];
			// ELEC[j] = temp;
			// }
			// }
			// }
			// for (int i = 0; i < MONTHS.length; i++) {
			// System.out.println(MONTHS[i] + "  " + ELEC[i]);
			// }

			break;

		default:
			break;
		}

	}

	/**
	 * 获得各个分月电量数据
	 */
	private void getEveryMonthElec() {

		db = new DatabaseHelper(this);
		System.out.println("已打开数据库");

		ElecTpyeId = new String[13][10];
		TpyeElec = new String[13][10];
		// MONTHS.length
		for (int i = 1; i <= 12; i++) {

			String sql = null;

			if (SELECTNUM == 0) {
				if (i >= 10) {
					sql = "select t3.tradetype_name,t2.energy from mktadmin_bidunits t1,mktsbs_result_type t2, mktadmin_tradetype t3 where t1.bidunit_shortname='"
							+ POWERNAME
							+ "' and t1.bidunit_id=t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ POWERYear
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ i
							+ "' and t2.Tradetype_Id= t3.tradetype_id and t2.period=0";

				} else {
					sql = "select t3.tradetype_name,t2.energy from mktadmin_bidunits t1,mktsbs_result_type t2, mktadmin_tradetype t3 where t1.bidunit_shortname='"
							+ POWERNAME
							+ "' and t1.bidunit_id=t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ POWERYear
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ "0"
							+ i
							+ "' and t2.Tradetype_Id= t3.tradetype_id and t2.period=0";

				}
			} else if (SELECTNUM == 1) {
				if (i >= 10) {
					sql = "select t3.tradetype_name,t2.net_energy from mktadmin_bidunits t1,mktplan_gen_decomenergy_item t2,mktadmin_tradetype t3 where t2.bidunit_id=t1.bidunit_id and t1.bidunit_id= t2.bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ POWERYear
							+ "' and t1.bidunit_shortname='"
							+ POWERNAME
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ i
							+ "' and t2.decom_type=2  and t2.TradeType_Id=t3.tradetype_id";

				} else {
					sql = "select t3.tradetype_name,t2.net_energy from mktadmin_bidunits t1,mktplan_gen_decomenergy_item t2,mktadmin_tradetype t3 where t2.bidunit_id=t1.bidunit_id and t1.bidunit_id= t2.bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ POWERYear
							+ "' and t1.bidunit_shortname='"
							+ POWERNAME
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ "0"
							+ i
							+ "' and t2.decom_type=2  and t2.TradeType_Id=t3.tradetype_id";

				}
			}

			database = db.openDatabase();
			System.out.println("已打开数据库");
			Cursor c = database.rawQuery(sql, null);

			ElecTpyeId_temp = null;
			TpyeElec_temp = null;
			ElecTpyeId_temp = new String[c.getCount()];
			TpyeElec_temp = new String[c.getCount()];

			int i1 = 0;
			if (SELECTNUM == 0) {
				if (null != c)
					while (c.moveToNext()) {
						ElecTpyeId_temp[i1] = c.getString(c
								.getColumnIndex("TradeType_Name"));
						TpyeElec_temp[i1] = c.getString(c
								.getColumnIndex("Energy"));
						System.out.println("type--->" + ElecTpyeId_temp[i1]);
						System.out.println("energy--->" + TpyeElec_temp[i1]);
						i1++;
					}
				if (null != c) {
					c.close();
					c = null;
				}
			} else if (SELECTNUM == 1) {
				if (null != c)
					while (c.moveToNext()) {
						ElecTpyeId_temp[i1] = c.getString(c
								.getColumnIndex("TradeType_Name"));
						TpyeElec_temp[i1] = c.getString(c
								.getColumnIndex("Net_Energy"));
						System.out.println("type--->" + ElecTpyeId_temp[i1]);
						System.out.println("energy--->" + TpyeElec_temp[i1]);
						i1++;
					}
				if (null != c) {
					c.close();
					c = null;
				}
			}
			db.closeDatabase();
			System.out.println("已关闭数据库");

			// 赋值
			// 需更改**************
			for (int item = 0; item < ElecTpyeId_temp.length; item++) {

				ElecTpyeId[i][item] = ElecTpyeId_temp[item];
				TpyeElec[i][item] = TpyeElec_temp[item];
				System.out.println(ElecTpyeId[i][item]);
			}

			if (SELECTNUM == 1) {
				double elec_every_sum = 0;
				for (int i11 = 0; i11 < ElecTpyeId_temp.length; i11++) {
					elec_every_sum = elec_every_sum
							+ Double.parseDouble(TpyeElec_temp[i11]);
				}
				ELEC[i - 1] = elec_every_sum;
				elec_every_sum = 0;

			}

		}
	}

	private void initData() {
		getEveryMonthElec();
		// 为一级条目提供数据
		double SUM = 0;
		for (int i = 0; i < ELEC.length; i++) {
			SUM = SUM + ELEC[i];
		}
		System.out.println("SUM --->" + SUM);
		// Groups 分组
		List<Map<String, Object>> Groups = new ArrayList<Map<String, Object>>();
		Map<String, Object> groupone_ = new HashMap<String, Object>();
		groupone_.put("key", "总计");
		groupone_.put("data", SUM + "");
		Groups.add(groupone_);
		for (int i = 0; i < MONTHS.length; i++) {
			System.out.println(" MONTHS" + MONTHS[i]);
			Map<String, Object> groupone = new HashMap<String, Object>();
			groupone.put("key", (int) MONTHS[i] + "月");
			groupone.put("data", ELEC[i] + "");
			Groups.add(groupone);

		}

		// 定义两个List<Map<String,Object>> childone和childtwo
		// 为二级条目提供数据
		// childone
		// Childs

		boolean isnull = false;// isInitData
								// 表示是否加入数据，因为有的时候获取来的月份缺失（如缺失，3，4月）
		List<List<Map<String, Object>>> Childs = new ArrayList<List<Map<String, Object>>>();
		int length_ = MONTHS.length + 1;
		// 检验是否全部为空
		int isAllNull = 0;

		int m = 0;
		int haveDataMonths[] = new int[13];
		for (int i1 = 0; i1 < 13; i1++) {
			if (ElecTpyeId[i1][0] != null) {
				haveDataMonths[m] = 0;
				isAllNull++;
				haveDataMonths[m] = i1;
				System.out.println("此月有数据-->" + haveDataMonths[m]);
				m++;
			}
		}
		for (int i = 0; i < 13; i++) {
			List<Map<String, Object>> childone = new ArrayList<Map<String, Object>>();
			// 加载9个child 动态加载 加载前判断是否有数据，无则不显示
			for (int j = 0; j < ElecTpyeId[j].length; j++) {
				// i不等于0 表示不需要改变综合 第一栏的加入数据
				System.out.println("ElecTpyeId[" + i + "][" + j + "]"
						+ ElecTpyeId[i][j]);
				System.out.println("TpyeElec[" + i + "][" + j + "]"
						+ TpyeElec[i][j]);
				if ((ElecTpyeId[i][j] != null)) {
					Map<String, Object> childonedateone = new HashMap<String, Object>();
					childonedateone.put("ID", ElecTpyeId[i][j]);
					childonedateone.put("Elec", TpyeElec[i][j]);
					childone.add(childonedateone);
				}
				if (ElecTpyeId[i][0] == null && (i != 0)) {
					isnull = true;
					break;
				}
			}
			if (isnull == false) {
				Childs.add(childone);
			} else {
				// if (isAllNull == 0) {
				List<Map<String, Object>> childone_ = new ArrayList<Map<String, Object>>();
				Childs.add(childone_);
				isnull = false;
				// } else {
				// // int month_have = 0;
				// for (int i2 = 0; i2 < haveDataMonths.length; i2++) {
				// if (i == haveDataMonths[i2]) {
				// for (int j = 0; j < ElecTpyeId[j].length; j++) {
				// if ((ElecTpyeId[i][j] != null)) {
				// Map<String, Object> childonedateone = new HashMap<String,
				// Object>();
				// childonedateone.put("ID", ElecTpyeId[i][j]);
				// childonedateone.put("Elec", TpyeElec[i][j]);
				// childone.add(childonedateone);
				// }
				// }
				// // month_have = haveDataMonths[i2];
				// }
				// }
				// Childs.add(childone);
				// isnull = false;
				// }
				// isnull = false;
			}
		}
		/**
		 * 使用SimpleExpandableListAdapter显示ExpandableListView
		 */
		SimpleExpandableListAdapter simpleExpandListAdapter = new SimpleExpandableListAdapter(
				Vip_PrivateActivity.this, Groups,
				R.layout.after_login_pr_table_item, new String[] { "key",
						"data" }, new int[] { R.id.tv_key, R.id.tv_data },
				Childs, R.layout.child, new String[] { "ID", "Elec" },
				new int[] { R.id.id, R.id.name });

		lvShow1.setAdapter(simpleExpandListAdapter);
		lvShow1.setGroupIndicator(null);
	}
}
