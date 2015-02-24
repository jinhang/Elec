/**
 * 公共信息检索 信息暂时搁在 xml里
 */
package com.dome.viewer;

import java.util.ArrayList;
import java.util.List;

import kankan.wheel.widget.ArrayWheelAdapter;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dome.chartdemo.SalesBarChart;
import com.dome.chartdemo.SalesStackedBarChart;
import com.dome.chartdemo.SalesStackedBarChartDecom;
import com.dome.chartdemo.SalesStackedBarChartMonthColors;
import com.dome.db.DatabaseHelper;

/**
 * @author 张藤原   成员 listview 一到四跳转
 *         jinhang  成员 五到八跳转
 */
public class Vip_Info extends Activity {

	// 标题text
	private TextView title;
	private TextView textView;// 机组名称
	// 顶部文字
	private TextView textChoose;
	private TextView textChoose_right;
	private TextView yuechoose;
	// 确定按键
	private Button button;
	// 设置3个滚轮选择器
	private WheelView powersC;
	private WheelView powersC_right;
	private WheelView timeC;
	private WheelView itemC;
	private WheelView jizu;
	private WheelView yue;
	// 滚轮控件内容数组
	String POWERS[] = new String[] { "大唐电力电厂", "中国华北电力集团", "华电集团公司", "咸阳市区电力局",
			"国电集团公司和电力投资集团公司", "新乡华源电力集团有限公司", "河南送变电建设公司", "郑州祥和集团有限公司",
			"湖南省电力公司", "华能集团公司", };
	String TIME[] = new String[] { "2013", "2012", "2011", "2010", "2009",
			"2008", "2007", "2006", "2005", "2004", };
	String ITEM[] = new String[] { "年度", "季度", "月度", };

	String jizu_name[] = new String[] { "大唐I", "大唐II", "龙天II", "神剑I", "高通II",
			"高通III", "联合科I", "联发科学II", "力核III", "力核II" };
	String yuefen[] = new String[] { "1月", "2月", "3月", "4月", "5月", "6月", "7月",
			"8月", "9月", "10月", "11月", "12月" };

	private RadioGroup radiogroup;
	private RadioButton radiobtn_chat;
	private RadioButton radiobtn_table;

	// 传递进来的选择序号,0-20为信息列表列，100为private列
	private int choose_id = 0;
	// 传递bundle
	private Bundle bundle;
	// radio choose //1 chart图表 2 table 表格
	private int radioChoose = 1;
	// 跳过radiochoose 的选择
	private int unuseRadio = 0;

	// *********************显示图表
	// 试图选择
	protected SalesStackedBarChart barChart;// 此为柱状图
	protected SalesBarChart doubleBarChart;
	protected static List<double[]> d2;
	protected static List<double[]> d3;
	protected static List<String[]> tags2;
	protected static String title2;
	private Button btn_back;

	// 数据库
	SQLiteDatabase database;
	DatabaseHelper db = new DatabaseHelper(this);
	private String pow_year; // 轮子现在选择的电厂
	private String pow_name; // 轮子现在选择的年
	private String pow_month = "01";
	// 视图最大Y
	protected int MAX_Y = 70000;
	// 设置的图表信息
	double[] MONTHS = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };; // 数据库中得到的月份数组
	double[] ELEC = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // 相对应的月份的发电量
	double[] ELEC_PLAN = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	double[] ELEC_TRUE = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	static String[] DC_NAME_result; // 得到竞价单元滚轮的数据
	static String[] TIME_result; // 时间滚轮数据
	private int sqlSelect = 0; // 选择的选项
	private String[] jizu_result;
	protected String pow_jizu;
	protected List<String[]> tags;
	protected String title_;
	private String imei;
	private double[] MONTHS_temp;
	private String[][] ElecTpyeId;
	private double[][] TpyeElec;
	private String[] ElecTpyeId_temp;
	private double[] TpyeElec_temp;

	private static String[] True_MONTHS = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12" };

	private static double[] plan_a = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static double[] plan_b = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private double[][] pic_elec;// 传递的转至后二维数组

	private double[] getDividedElec_yearBaseElec_1;
	private double[] getDividedElec_yearTrade_2;
	private double[] getDividedElec_yearSell_3;
	private double[] getDividedElec_yearTieline_4;
	private double[] getDividedElec_yearQuota_5;
	private double[] getDividedElec_Region_6;
	private double[] getDividedElec_Prov_7;
	private double[] getDividedElec_Other_8;
	private double[] getDividedElec_Tieline_9;
	int MONTH_ID = 0;// new
	private String[][] TpyeElec_2;
	private String[] TpyeElec_2_temp;
	private double[] ELEC_2 = new double[13];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.after_login_jianxiujihua);

		title = (TextView) findViewById(R.id.vipchoose_title);// 顶端信息名称
		textChoose = (TextView) findViewById(R.id.text_choose);// 第一个
		radiogroup = (RadioGroup) findViewById(R.id.radioGroup_choose_from);
		radiobtn_chat = (RadioButton) findViewById(R.id.radio1);
		radiobtn_table = (RadioButton) findViewById(R.id.radio2);
		loadingPowersChoose();
		getChoose();

		loadingOKbtn();

		// 返回
		btn_back = (Button) findViewById(R.id.btn_back_pub_choose);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Vip_Info.this.finish();
			}
		});
	}

	/**
	 * 得到选择，列表的哪一项
	 */
	private void getChoose() {
		bundle = this.getIntent().getExtras();
		choose_id = bundle.getInt("chooseId");
		System.out.println("choose_-----> " + choose_id);

		radiobtn_chat.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				radioChoose = 2;
				if (unuseRadio == 5) {
					radioChoose = 5;
				}
			}
		});

		radiobtn_table
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						radioChoose = 1;
						if (unuseRadio == 5) {
							radioChoose = 0;
							System.out.println("choose" + 0);
						}
					}
				});

		switch (choose_id) {
		case 0:
			// 取数据，一定要设置sqlselect
			sqlSelect = 0;
			powersC_right.setVisibility(0);
			title = (TextView) findViewById(R.id.vipchoose_title);
			title.setText("上网电量");
			textChoose.setText("请选择竞价单元");
			textChoose_right.setVisibility(0);
			textChoose_right.setText("年份");

			getData0Wheel();
			// 给左边轮子赋值；
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));
			POWERS = DC_NAME_result;
			System.out.println("已成功更改滑轮1数据");

			getData0Year();
			// 给右边轮子赋值；
			powersC_right
					.setAdapter(new ArrayWheelAdapter<String>(TIME_result));
			System.out.println("已成功更改滑轮2数据");

			// 电厂名称滑轮设置连同
			powersC.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					getData0Year();
					// 动态加载右边年份
					powersC_right.setAdapter(new ArrayWheelAdapter<String>(
							TIME_result));
					powersC_right.setCurrentItem(0);

					// // 设置滚动后图表数据
					// getData0YearAndData();
					//
					// // 更改绘图属性
					// // 根据获得数组最大值动态改变y轴最大值,存入全局变量
					// getDataMax();
					//
					// d2 = new ArrayList<double[]>();
					// if (MONTHS == null
					// || (MONTHS != null && MONTHS.length == 0)) {
					// MONTHS = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
					// 11, 12 };
					// }
					// if (ELEC == null || (ELEC != null && ELEC.length == 0)) {
					// ELEC = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					// 0 };
					// }
					// d2.add(MONTHS);
					// d2.add(ELEC);
					// tags2 = new ArrayList<String[]>();
					// tags2.add(new String[] { "竞价单元年度发电量信息", "月", "发电量" });
					// title2 = "竞价单元年度发电量信息";

				}

			});

			// 年度滑轮增加监听
			powersC_right.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// 设置滚动后图表数据
					// getData0YearAndData();
					// // 根据获得数组最大值动态改变y轴最大值,存入全局变量
					// getDataMax();
					// d2 = new ArrayList<double[]>();
					// if (MONTHS == null
					// || (MONTHS != null && MONTHS.length == 0)) {
					// MONTHS = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
					// 11, 12 };
					// }
					// if (ELEC == null || (ELEC != null && ELEC.length == 0)) {
					// ELEC = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					// 0 };
					// }
					// d2.add(MONTHS);
					// d2.add(ELEC);
					// tags2 = new ArrayList<String[]>();
					// tags2.add(new String[] { "竞价单元年度发电量信息", "月", "发电量" });
					// title2 = "竞价单元年度发电量信息";

				}

			});

			// getData0YearAndData();
			// // 根据获得数组最大值动态改变y轴最大值,存入全局变量
			// getDataMax();
			// d2 = new ArrayList<double[]>();
			// if (MONTHS == null || (MONTHS != null && MONTHS.length == 0)) {
			// MONTHS = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
			// }
			// if (ELEC == null || (ELEC != null && ELEC.length == 0)) {
			// ELEC = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			// }
			// d2.add(MONTHS);
			// d2.add(ELEC);
			// tags2 = new ArrayList<String[]>();
			// tags2.add(new String[] { "竞价单元年度发电量信息", "月", "发电量" });
			// title2 = "竞价单元年度发电量信息";

			break;
		case 1:
			title.setText("月度电量计划");
			powersC_right.setVisibility(0);
			textChoose_right.setVisibility(0);
			textChoose_right.setText("年份");
			textChoose.setText("请选择竞价单元");
			// 取数据，一定要设置sqlselect
			sqlSelect = 1;

			getData1Wheel();
			// 给左边轮子赋值；
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));
			POWERS = DC_NAME_result;
			System.out.println("已成功更改滑轮1数据");

			getData1Year();
			// 给右边轮子赋值；
			powersC_right
					.setAdapter(new ArrayWheelAdapter<String>(TIME_result));
			System.out.println("已成功更改滑轮2数据");

			// 电厂名称滑轮设置连同
			powersC.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
					// 动态加载右边年份
					getData1Year();
					powersC_right.setAdapter(new ArrayWheelAdapter<String>(
							TIME_result));
					powersC_right.setCurrentItem(0);
					// getData1YearAndData();
					// // 更改绘图属性
					// // 根据获得数组最大值动态改变y轴最大值,存入全局变量
					// getDataMax();
					// d2 = new ArrayList<double[]>();
					// if (MONTHS.length == 0 || ELEC.length == 0) {
					// System.out.println("没有获取到绘图数据");
					// } else {
					// d2.add(MONTHS);
					// d2.add(ELEC);
					// }
					// tags2 = new ArrayList<String[]>();
					// tags2.add(new String[] { "竞价单元年度发电量信息", "月", "发电量" });
					// title2 = "竞价单元年度发电量信息";

				}
			});

			// 年度滑轮增加监听
			powersC_right.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// 1、增加年份监听
					// 设置滚动后图表数据
					//
					// getData1YearAndData();
					//
					// // 更改绘图属性
					// // 根据获得数组最大值动态改变y轴最大值,存入全局变量
					// getDataMax();
					// d2 = new ArrayList<double[]>();
					// if (MONTHS.length == 0 || ELEC.length == 0) {
					// System.out.println("没有获取到绘图数据");
					// } else {
					// d2.add(MONTHS);
					// d2.add(ELEC);
					// }
					// tags2 = new ArrayList<String[]>();
					// tags2.add(new String[] { "竞价单元年度发电量信息", "月", "发电量" });
					// title2 = "竞价单元年度发电量信息";

				}
			});

			// getData1YearAndData();
			//
			// // 更改绘图属性
			// // 根据获得数组最大值动态改变y轴最大值,存入全局变量
			// getDataMax();
			// d2 = new ArrayList<double[]>();
			// if (MONTHS.length == 0 || ELEC.length == 0) {
			// System.out.println("没有获取到绘图数据");
			// } else {
			// d2.add(MONTHS);
			// d2.add(ELEC);
			// }
			// tags2 = new ArrayList<String[]>();
			// tags2.add(new String[] { "竞价单元年度发电量信息", "月", "发电量" });
			// title2 = "竞价单元年度发电量信息";

			break;
		case 2:
			radioChoose = 0;
			title.setText("计划执行情况");
			powersC_right.setVisibility(0);
			textChoose_right.setVisibility(0);
			textChoose_right.setText("年份");
			textChoose.setText("请选择竞价单元");
			// 取数据，一定要设置sqlselect
			sqlSelect = 2;

			getData2Wheel();
			// 给左边轮子赋值；
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));
			POWERS = DC_NAME_result;
			System.out.println("已成功更改滑轮1数据");

			getData2Year();
			// 给右边轮子赋值；
			powersC_right
					.setAdapter(new ArrayWheelAdapter<String>(TIME_result));
			System.out.println("已成功更改滑轮2数据");

			// 电厂名称滑轮设置连同
			powersC.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {

					getData2Year();
					// 动态加载右边年份
					powersC_right.setAdapter(new ArrayWheelAdapter<String>(
							TIME_result));
					powersC_right.setCurrentItem(0);
					// 设置滚动后图表数据

					getData2YearAndData();
					// 更改绘图属性
					// 根据获得数组最大值动态改变y轴最大值,存入全局变量
					getDataMaxComplete();
				}
			});

			// 年度滑轮增加监听
			powersC_right.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// 1、增加年份监听
					getData2YearAndData();
					// 更改绘图属性
					// 根据获得数组最大值动态改变y轴最大值,存入全局变量
					getDataMaxComplete();
					unuseRadio = 5;
				}
			});
			// 设置图形的数据
			getData2YearAndData();
			// 更改绘图属性
			// 根据获得数组最大值动态改变y轴最大值,存入全局变量
			getDataMaxComplete();
			unuseRadio = 5;

			break;
		case 3:
			radioChoose = 0;
			title.setText("合同执行情况");
			powersC_right.setVisibility(0);
			textChoose_right.setVisibility(0);
			textChoose_right.setText("年份");
			textChoose.setText("请选择竞价单元");
			// 取数据，一定要设置sqlselect
			sqlSelect = 3;

			getData3Wheel();
			// 给左边轮子赋值；
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));
			POWERS = DC_NAME_result;
			System.out.println("已成功更改滑轮1数据");

			getData3Year();
			// 给右边轮子赋值；
			powersC_right
					.setAdapter(new ArrayWheelAdapter<String>(TIME_result));
			System.out.println("已成功更改滑轮2数据");

			// 电厂名称滑轮设置连同
			powersC.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {

					getData3Year();
					// 动态加载右边年份
					powersC_right.setAdapter(new ArrayWheelAdapter<String>(
							TIME_result));
					powersC_right.setCurrentItem(0);
					// 设置滚动后图表数据
					getData3YearAndData();
				}
			});

			// 年度滑轮增加监听
			powersC_right.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// 1、增加年份监听
					getData3YearAndData();

					// 更改绘图属性
					// 根据获得数组最大值动态改变y轴最大值,存入全局变量
					// if (ELEC_TRUE.length != 0) {
					// float tempN = (float) ELEC_TRUE[0];
					// for (int i = 0; i < ELEC_TRUE.length; i++) {
					// if (tempN <= ELEC_TRUE[i]) {
					// tempN = (float) ELEC_TRUE[i];
					// }
					// }
					// System.out.println("图表最大y-----〉" + tempN);
					// MAX_Y = (int) ((int) tempN * 1.5);
					// // d2 = new ArrayList<double[]>();
					// // d2.add(MONTHS);
					// // // d2.add(ELEC_PLAN);
					// // // d2.add(ELEC_TRUE);
					// // tags2 = new ArrayList<String[]>();
					// // tags2.add(new String[] { "竞价单元计划执行情况", "月", "发电量"
					// // });
					// // title2 = "竞价单元计划执行情况";
					// // title.setText("竞价单元计划执行情况");
					// unuseRadio = 5;
					// }

				}
			});

			// 设置图形的数据
			getData3YearAndData();

			// 更改绘图属性
			// 根据获得数组最大值动态改变y轴最大值,存入全局变量
			// if (ELEC_TRUE.length != 0) {
			// double tempN3 = (double) ELEC_TRUE[0];
			// for (int i = 0; i < ELEC_TRUE.length; i++) {
			// if (tempN3 <= ELEC_TRUE[i]) {
			// tempN3 = (double) ELEC_TRUE[i];
			// }
			// }
			// // System.out.println("图表最大y-----〉" + tempN3);
			// // MAX_Y = (int) ((int) tempN3 * 1.5);
			// // d2 = new ArrayList<double[]>();
			// // d2.add(MONTHS);
			// // // d2.add(ELEC_PLAN);
			// // // d2.add(ELEC_TRUE);
			// // tags2 = new ArrayList<String[]>();
			// // tags2.add(new String[] { "竞价单元计划执行情况", "月", "发电量" });
			// // title2 = "竞价单元计划执行情况";
			// // title.setText("竞价单元计划执行情况");
			// unuseRadio = 5;
			// }
			unuseRadio = 5;

			break;
		case 4:

			title.setText("结算信息");
			textChoose.setText("请选择竞价单元");
			powersC_right.setVisibility(0);
			textChoose_right.setVisibility(0);
			textChoose_right.setText("年份");
			yuechoose.setText("月份");
			yuechoose.setVisibility(0);
			yue.setVisibility(0);
			radiogroup.setVisibility(8);
			radiobtn_chat.setVisibility(8);
			radiobtn_table.setVisibility(8);

			sqlSelect = 4;

			getData4Wheel();
			// 给左边轮子赋值；
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));
			POWERS = DC_NAME_result;
			System.out.println("已成功更改滑轮1数据");

			getData4Year();
			// 给右边轮子赋值；
			powersC_right
					.setAdapter(new ArrayWheelAdapter<String>(TIME_result));
			System.out.println("已成功更改滑轮2数据");

			System.out.println(yue.getCurrentItem() + "--------");
			pow_month = True_MONTHS[yue.getCurrentItem()];
			// 电厂名称滑轮设置连同
			powersC.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
					// 动态加载右边年份
					getData4Year();
					powersC_right.setAdapter(new ArrayWheelAdapter<String>(
							TIME_result));
					powersC_right.setCurrentItem(0);
					pow_month = True_MONTHS[yue.getCurrentItem()];
					// getData4YearAndData();
				}
			});

			// 年度滑轮增加监听
			powersC_right.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					pow_month = True_MONTHS[yue.getCurrentItem()];
					// getData4YearAndData();
				}
			});

			yue.addScrollingListener(new OnWheelScrollListener() {

				@Override
				public void onScrollingStarted(WheelView wheel) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					System.out.println(yue.getCurrentItem() + "--------");
					pow_month = True_MONTHS[yue.getCurrentItem()];
					System.out.println(pow_month);

				}
			});
			// getData4YearAndData();
			pow_month = True_MONTHS[yue.getCurrentItem()];
			radioChoose = 6;
			break;
		case 5:
			title.setText("明显差异情况说明");
			break;

		case 6:
			/**
			 * jh 机组设备年利用小时数
			 */
			sqlSelect = 6;
			textView = (TextView) findViewById(R.id.text_choose2);
			title.setText("年利用小时数");
			textChoose.setText("市场成员");
			textView.setText("机组名称");
			radiogroup.setVisibility(8);
			radiobtn_chat.setVisibility(8);
			radiobtn_table.setVisibility(8);
			jizu.setVisibility(0);

			sqlSelect = 6;

			db = new DatabaseHelper(this);
			database = db.openDatabase();
			System.out.println("已打开数据库");
			String sql1 = "select distinct t1.Participant_ShortName from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.Generator_Id";
			Cursor c12 = database.rawQuery(sql1, null);
			DC_NAME_result = null;
			DC_NAME_result = new String[c12.getCount()];
			int j11 = 0;
			System.out.println("准备赋值");
			while (c12.moveToNext()) {
				System.out.println("赋值中。。。");
				DC_NAME_result[j11] = c12.getString(c12
						.getColumnIndex("Participant_ShortName"));
				System.out.println("得到---->" + DC_NAME_result[j11]);
				j11++;
			}
			if (null != c12) {
				c12.close();
				c12 = null;
			}
			db.closeDatabase();
			System.out.println("已关闭数据库");
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));

			System.out.println("已成功更改滑轮1数据");

			// 更改轮子机组名称
			database = db.openDatabase();
			System.out.println("已打开数据库");
			// 获取滚轮选择的名字 ，改变sql语句，获取机组名称数据
			System.out.println(powersC.getCurrentItem());

			pow_name = DC_NAME_result[powersC.getCurrentItem()];// 当前选择的市场成员

			System.out.println("用户选择------>" + pow_name);
			String sql2 = "select distinct  t2.PHYUNIT_NAME as name from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.generator_id and t1.Participant_shortname='"
					+ pow_name + "'";
			Cursor c71 = database.rawQuery(sql2, null);

			jizu_result = null;
			jizu_result = new String[c71.getCount()];
			int k11 = 0;
			while (c71.moveToNext()) {
				jizu_result[k11] = c71.getString(c71.getColumnIndex("name"));
				System.out.println("机组---->" + jizu_result[k11]);
				k11++;
			}
			if (null != c71) {
				c71.close();
				c71 = null;
			}
			db.closeDatabase();
			System.out.println("已关闭数据库");
			// 给右边轮子赋值；
			jizu.setAdapter(new ArrayWheelAdapter<String>(jizu_result));
			powersC.addScrollingListener(new OnWheelScrollListener() {

				@Override
				public void onScrollingStarted(WheelView wheel) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// TODO Auto-generated method stub
					// 更改轮子机组名称
					database = db.openDatabase();
					System.out.println("已打开数据库");
					// 获取滚轮选择的名字 ，改变sql语句，获取机组名称数据
					System.out.println(powersC.getCurrentItem());

					pow_name = DC_NAME_result[powersC.getCurrentItem()];// 当前选择的市场成员

					System.out.println("用户选择------>" + pow_name);
					String sql2 = "select distinct  t2.PHYUNIT_NAME as name from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.generator_id and t1.Participant_shortname='"
							+ pow_name + "'";
					Cursor c71 = database.rawQuery(sql2, null);

					jizu_result = null;
					jizu_result = new String[c71.getCount()];
					int k11 = 0;
					while (c71.moveToNext()) {
						jizu_result[k11] = c71.getString(c71
								.getColumnIndex("name"));
						System.out.println("机组---->" + jizu_result[k11]);
						k11++;
					}
					if (null != c71) {
						c71.close();
						c71 = null;
					}
					db.closeDatabase();
					System.out.println("已关闭数据库");
					// 给右边轮子赋值；
					jizu.setAdapter(new ArrayWheelAdapter<String>(jizu_result));
				}
			});
			radioChoose = 4;
			// 得到 Generator_Id 加载在 String jizu_name[] 里
			break;

		case 7:
			/**
			 * jh 机组年度检修计划数据
			 */

			textView = (TextView) findViewById(R.id.text_choose2);
			title.setText("年度检修计划数据");
			textChoose.setText("市场成员");
			textView.setText("机组名称");
			radiogroup.setVisibility(8);
			radiobtn_chat.setVisibility(8);
			radiobtn_table.setVisibility(8);
			jizu.setVisibility(0);
			// 更改轮子竞价单元

			sqlSelect = 7;

			db = new DatabaseHelper(this);
			database = db.openDatabase();
			System.out.println("已打开数据库");
			String sql71 = "select distinct t1.Participant_ShortName from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.Generator_Id";
			Cursor c17 = database.rawQuery(sql71, null);
			DC_NAME_result = null;
			DC_NAME_result = new String[c17.getCount()];
			int j17 = 0;
			System.out.println("准备赋值");
			while (c17.moveToNext()) {
				System.out.println("赋值中。。。");
				DC_NAME_result[j17] = c17.getString(c17
						.getColumnIndex("Participant_ShortName"));
				System.out.println("得到---->" + DC_NAME_result[j17]);
				j17++;
			}
			if (null != c17) {
				c17.close();
				c17 = null;
			}
			db.closeDatabase();
			System.out.println("已关闭数据库");
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));

			System.out.println("已成功更改滑轮1数据");

			// 更改轮子机组名称
			database = db.openDatabase();
			System.out.println("已打开数据库");
			// 获取滚轮选择的名字 ，改变sql语句，获取机组名称数据
			System.out.println(powersC.getCurrentItem());

			pow_name = DC_NAME_result[powersC.getCurrentItem()];// 当前选择的市场成员

			System.out.println("用户选择------>" + pow_name);
			String sql6 = "select distinct  t2.PHYUNIT_NAME as name from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.generator_id and t1.Participant_shortname='"
					+ pow_name + "'";
			Cursor c16 = database.rawQuery(sql6, null);

			jizu_result = null;
			jizu_result = new String[c16.getCount()];
			int k16 = 0;
			while (c16.moveToNext()) {
				jizu_result[k16] = c16.getString(c16.getColumnIndex("name"));
				System.out.println("机组---->" + jizu_result[k16]);
				k16++;
			}
			if (null != c16) {
				c16.close();
				c16 = null;
			}
			db.closeDatabase();
			System.out.println("已关闭数据库");
			// 给右边轮子赋值；
			jizu.setAdapter(new ArrayWheelAdapter<String>(jizu_result));

			// 电厂名称滑轮设置连同
			powersC.addScrollingListener(new OnWheelScrollListener() {

				@Override
				public void onScrollingStarted(WheelView wheel) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					// TODO Auto-generated method stub
					// 更改轮子机组名称
					database = db.openDatabase();
					System.out.println("已打开数据库");
					// 获取滚轮选择的名字 ，改变sql语句，获取机组名称数据
					System.out.println(powersC.getCurrentItem());

					pow_name = DC_NAME_result[powersC.getCurrentItem()];// 当前选择的市场成员

					System.out.println("用户选择------>" + pow_name);
					String sql2 = "select distinct  t2.PHYUNIT_NAME as name from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.generator_id and t1.Participant_shortname='"
							+ pow_name + "'";
					Cursor c71 = database.rawQuery(sql2, null);

					jizu_result = null;
					jizu_result = new String[c71.getCount()];
					int k11 = 0;
					while (c71.moveToNext()) {
						jizu_result[k11] = c71.getString(c71
								.getColumnIndex("name"));
						System.out.println("机组---->" + jizu_result[k11]);
						k11++;
					}
					if (null != c71) {
						c71.close();
						c71 = null;
					}
					db.closeDatabase();
					System.out.println("已关闭数据库");
					// 给右边轮子赋值；
					jizu.setAdapter(new ArrayWheelAdapter<String>(jizu_result));
				}
			});

			// 机组名称滑轮增加监听
			jizu.addScrollingListener(new OnWheelScrollListener() {
				@Override
				public void onScrollingStarted(WheelView wheel) {
				}

				@Override
				public void onScrollingFinished(WheelView wheel) {
					pow_jizu = jizu_result[jizu.getCurrentItem()];
					System.out.println("要传的是" + pow_jizu);
				}
			});

			radioChoose = 3;// 跳到listview维修计划
			break;

		case 100:
			sqlSelect = 100;

			powersC.setAdapter(new ArrayWheelAdapter<String>(TIME));
			title.setText("竞价单元每笔合同数据");
			// 0,4,8 vis ivis gone
			radiogroup.setVisibility(8);
			radiobtn_chat.setVisibility(8);
			radiobtn_table.setVisibility(8);
			textChoose.setText("选择市场成员：");

			// 由IEMI得到市场成员
			// 市场成员得到竞价单元
			// 得到合同信息
			FourActivity ii = new FourActivity();
			imei = ii.getInfoIMEI();
			// String []name2=null;

			String sql11 = "Select participant_Id from LogIn where IMEI="
					+ imei + "";
			db = new DatabaseHelper(this);
			database = db.openDatabase();
			System.out.println("已打开数据库");
			Cursor cd = database.rawQuery(sql11, null);

			int jt = 0;
			System.out.println("准备赋值");

			while (cd.moveToNext()) {
				System.out.println("赋值中。。。");
				DC_NAME_result[jt] = cd.getString(cd
						.getColumnIndex("Participant_ShortName"));
				// 市场成员
				System.out.println("得到---->" + DC_NAME_result[jt]);
				jt++;
			}
			if (null != cd) {
				cd.close();
				cd = null;
			}
			db.closeDatabase();
			System.out.println("已关闭数据库");
			// 获得市场成员
			powersC.setAdapter(new ArrayWheelAdapter<String>(DC_NAME_result));
			POWERS = null;
			POWERS = DC_NAME_result;

			radioChoose = 7;
			break;

		default:
			break;
		}

	}

	protected void getData4YearAndData() {
		// 设置滚动后图表数据
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		pow_year = TIME_result[powersC_right.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c2 = database
				.rawQuery(
						"Select * from MktSbs_Result_Type t1,mktadmin_bidunits t2 where t1.sbs_bidunit_id=t2.bidunit_id and tradetype_id='0' and strftime('%Y',t1.Mkt_Date)='"
								+ pow_year
								+ "' and t2.bidunit_shortname='"
								+ pow_name + "' order by t1.mkt_date", null);
		MONTHS = null;
		ELEC = null;
		MONTHS = new double[c2.getCount()];
		ELEC = new double[c2.getCount()];
		int l = 0;
		System.out.println("准备赋值MONTHS");
		while (c2.moveToNext()) {
			System.out.println("MONTHS赋值中。。。");
			MONTHS[l] = Double
					.valueOf(c2.getString(c2.getColumnIndex("month")));
			System.out.println("得到---->" + MONTHS[l]);

			System.out.println("ELEC赋值中。。。");
			ELEC[l] = c2.getDouble(c2.getColumnIndex("energy"));
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
	}

	private void getData4Year() {
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c111 = database
				.rawQuery(
						"Select distinct strftime('%Y',t1.Mkt_Date) as year from MktSbs_Result_Type t1,mktadmin_bidunits t2 where t1.sbs_Bidunit_id=t2.bidunit_id and t2.bidunit_shortname='"
								+ pow_name + "' order by year", null);
		TIME_result = null;
		TIME_result = new String[c111.getCount()];
		int k1 = 0;
		System.out.println("准备赋值time");
		while (c111.moveToNext()) {
			System.out.println("time赋值中。。。");
			TIME_result[k1] = c111.getString(c111.getColumnIndex("year"));
			System.out.println("得到---->" + TIME_result[k1]);
			k1++;
		}
		if (null != c111) {
			c111.close();
			c111 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	private void getData4Wheel() {
		database = db.openDatabase();
		System.out.println("已打开数据库");
		Cursor c11 = database
				.rawQuery(
						"select distinct t2.bidunit_shortname as name from mktsbs_result_type t1,mktadmin_bidunits t2 where t2.sched_type in(0,1,2) and t1.sbs_bidunit_id=t2.bidunit_id order by name",
						null);
		DC_NAME_result = null;
		DC_NAME_result = new String[c11.getCount()];
		int j1 = 0;
		System.out.println("准备赋值");
		while (c11.moveToNext()) {
			System.out.println("赋值中。。。");
			DC_NAME_result[j1] = c11.getString(c11.getColumnIndex("name"));
			System.out.println("得到---->" + DC_NAME_result[j1]);
			j1++;
		}
		if (null != c11) {
			c11.close();
			c11 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	protected void getData3YearAndData() {
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，实际发电量
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		pow_year = TIME_result[powersC_right.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c2 = database
				.rawQuery(
						"select  strftime('%m',t2.mkt_date) as month ,t2.energy as energy from MktAdmin_BidUnits t1,MktSbs_Result_Type t2 where t1.bidunit_shortname='"
								+ pow_name
								+ "' and t1.bidunit_id= t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date) = '"
								+ pow_year
								+ "' and t2.Tradetype_Id=0 order by month",
						null);
		MONTHS = null;
		ELEC_TRUE = null;
		MONTHS = new double[c2.getCount()];
		ELEC_TRUE = new double[c2.getCount()];
		int l = 0;
		System.out.println("准备赋值MONTHS");
		while (c2.moveToNext()) {
			System.out.println("MONTHS赋值中。。。");
			MONTHS[l] = Double
					.valueOf(c2.getString(c2.getColumnIndex("month")));
			System.out.println("得到---->" + MONTHS[l]);

			System.out.println("ELEC_TRUE赋值中。。。");
			ELEC_TRUE[l] = c2.getDouble(c2.getColumnIndex("energy"));
			System.out.println("得到---->" + ELEC_TRUE[l]);
			l++;
		}
		if (null != c2) {
			c2.close();
			c2 = null;
		}

		// 获取计划数据
		Cursor c21 = database
				.rawQuery(
						"select strftime('%m',t3.Begin_Time) AS month,sum(t3.Sale_Energy)AS sale_energy,sum(t3.Sale_Net_Energy) as net_energy from mktadmin_bidunits t1,MktTrade_Contract_Energy t2,mktTrade_Contract_DecomEnergy t3 where t1.bidunit_shortname='"
								+ pow_name
								+ "' AND t1.bidunit_id=t2.Sale_Unitid and t2.Contract_Id=t3.Contract_Id and strftime('%Y',t3.Begin_Time)='"
								+ pow_year
								+ "' and t3.decom_type=2 and t3.period=0 group by strftime('%m',t3.Begin_Time) order by month",
						null);
		ELEC_PLAN = null;
		MONTHS_temp = null;
		MONTHS_temp = new double[c21.getCount()];
		ELEC_PLAN = new double[c21.getCount()];
		int l1 = 0;
		while (c21.moveToNext()) {
			System.out.println("MONTHS_temp赋值中。。。");
			MONTHS_temp[l1] = Double.valueOf(c21.getString(c21
					.getColumnIndex("month")));
			System.out.println("得到---->" + MONTHS_temp[l1]);

			System.out.println("ELEC_PLAN赋值中。。。");
			ELEC_PLAN[l1] = c21.getDouble(c21.getColumnIndex("net_energy"));
			System.out.println("得到---->" + ELEC_PLAN[l1]);
			l1++;
		}
		if (null != c21) {
			c21.close();
			c21 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
		System.out.println("已得到绘图属性");

		// 更改plan_a plan_b
		plan_a = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		plan_b = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int i = 0; i < MONTHS.length; i++) {
			System.out.println("(int)MONTHS[i] - 1" + ((int) MONTHS[i] - 1));
			plan_b[(int) MONTHS[i] - 1] = ELEC_TRUE[i];
		}
		for (int i = 0; i < MONTHS_temp.length; i++) {
			System.out.println("(int)MONTHS_temp[i] - 1"
					+ ((int) MONTHS_temp[i] - 1));
			plan_a[(int) MONTHS_temp[i] - 1] = ELEC_PLAN[i];
		}
		getDataMaxComplete();
	}

	private void getDataMaxComplete() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				float tempN = 0;
				if (ELEC_TRUE.length != 0) {
					for (int i = 0; i < ELEC_TRUE.length; i++) {
						if (tempN <= ELEC_TRUE[i]) {
							tempN = (float) ELEC_TRUE[i];
						}
					}
				}
				if (ELEC_PLAN.length != 0) {
					System.out.println("图表最大y-----〉" + tempN);
					for (int i = 0; i < ELEC_PLAN.length; i++) {
						if (tempN <= ELEC_PLAN[i]) {
							tempN = (float) ELEC_PLAN[i];
						}
					}

				}
				MAX_Y = (int) ((int) tempN * 1.2);
			}
		}).start();
	}

	private void getData3Year() {
		// 开始根据电厂公司名字 获取年份数据
		// 需判断名字 对应的年份列表
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c331 = database
				.rawQuery(
						"Select distinct strftime('%Y',t3.Begin_Time) as years from mktadmin_bidunits t1,MktTrade_Contract_Energy t2,mktTrade_Contract_DecomEnergy t3 where t1.bidunit_shortname='"
								+ pow_name
								+ "' and t1.bidunit_id=t2.Sale_Unitid and t2.Contract_Id=t3.Contract_Id and t3.DECOM_TYPE=2 and t3.Period=0 order by years",
						null);
		TIME_result = null;
		TIME_result = new String[c331.getCount()];
		int k31 = 0;
		System.out.println("准备赋值time");
		while (c331.moveToNext()) {
			System.out.println("time赋值中。。。");
			TIME_result[k31] = c331.getString(c331.getColumnIndex("years"));
			System.out.println("得到---->" + TIME_result[k31]);
			k31++;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	private void getData3Wheel() {
		// 在此调用数据库数据，将电厂名称写入 DC_NAME_result 年份，TIME_result ，图表月months
		// 调用dbhelper
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("已打开数据库");
		Cursor c311 = database
				.rawQuery(
						"select distinct t2.bidunit_shortname as name from MktTrade_Contract_Energy t1,mktadmin_bidunits t2 where t2.sched_type in(0,1,2) and t1.sale_unitid=t2.bidunit_id",
						null);
		DC_NAME_result = null;
		DC_NAME_result = new String[c311.getCount()];
		int j31 = 0;
		System.out.println("准备赋值");
		while (c311.moveToNext()) {
			System.out.println("赋值中。。。");
			DC_NAME_result[j31] = c311.getString(c311.getColumnIndex("name"));
			System.out.println("得到---->" + DC_NAME_result[j31]);
			j31++;
		}
		if (null != c311) {
			c311.close();
			c311 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	protected void getDataTrueMax() {
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，实际发电量
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		pow_year = TIME_result[powersC_right.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c2 = database
				.rawQuery(
						"select  strftime('%m',t2.mkt_date) as month ,t2.energy as energy from MktAdmin_BidUnits t1,MktSbs_Result_Type t2 where t1.bidunit_shortname='"
								+ pow_name
								+ "' and t1.bidunit_id= t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date) = '"
								+ pow_year
								+ "' and t2.Tradetype_Id=0 order by month",
						null);
		MONTHS = null;
		ELEC_TRUE = null;
		MONTHS = new double[c2.getCount()];
		ELEC_TRUE = new double[c2.getCount()];
		int l = 0;
		System.out.println("准备赋值MONTHS");
		while (c2.moveToNext()) {
			System.out.println("MONTHS赋值中。。。");
			MONTHS[l] = Double
					.valueOf(c2.getString(c2.getColumnIndex("month")));
			System.out.println("得到---->" + MONTHS[l]);

			System.out.println("ELEC_TRUE赋值中。。。");
			ELEC_TRUE[l] = c2.getDouble(c2.getColumnIndex("energy"));
			System.out.println("得到---->" + ELEC_TRUE[l]);
			l++;
		}
		if (null != c2) {
			c2.close();
			c2 = null;
		}

		// 获取计划数据
		Cursor c21 = database
				.rawQuery(
						"select  distinct strftime('%m',t2.mkt_date) as month,t2.net_energy as energy from MktAdmin_BidUnits t1,MktPlan_Gen_DecomEnergy_Item t2 where  t1.bidunit_id= t2.bidunit_id and t1.bidunit_shortname='"
								+ pow_name
								+ "' and strftime('%Y',t2.mkt_date)='"
								+ pow_year
								+ "'and t2.decom_type=2 and t2.Trade_Id='100007000000194' order by month",
						null);
		// MONTHS = null;
		ELEC_PLAN = null;
		// MONTHS = new double[c21.getCount()];
		ELEC_PLAN = new double[c21.getCount()];
		int l1 = 0;
		while (c21.moveToNext()) {
			// System.out.println("MONTHS赋值中。。。");
			// MONTHS[l1] = Double.valueOf(c21.getString(c21
			// .getColumnIndex("month")));
			// System.out.println("得到---->" + MONTHS[l1]);

			System.out.println("ELEC_PLAN赋值中。。。");
			ELEC_PLAN[l1] = c21.getDouble(c21.getColumnIndex("energy"));
			System.out.println("得到---->" + ELEC_PLAN[l1]);
			l1++;
		}
		if (null != c21) {
			c21.close();
			c21 = null;
		}

		db.closeDatabase();
		System.out.println("已关闭数据库");
		System.out.println("已得到绘图属性");
	}
	/**
	 * 
	 * 获得各个分月电量数据
	 */
	private void get2EveryMonthElec() {

		db = new DatabaseHelper(this);
		System.out.println("已打开数据库");

		ElecTpyeId = new String[13][10];
		TpyeElec_2 = new String[13][10];
		// MONTHS.length
		for (int i = 1; i <= 12; i++) {

			String sql = null;

		 if (sqlSelect == 2) {
				if (i >= 10) {
					sql = "select t3.tradetype_name,t2.net_energy from mktadmin_bidunits t1,mktplan_gen_decomenergy_item t2,mktadmin_tradetype t3 where t2.bidunit_id=t1.bidunit_id and t1.bidunit_id= t2.bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ pow_year
							+ "' and t1.bidunit_shortname='"
							+ pow_name
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ i
							+ "' and t2.decom_type=2  and t2.TradeType_Id=t3.tradetype_id";

				} else {
					sql = "select t3.tradetype_name,t2.net_energy from mktadmin_bidunits t1,mktplan_gen_decomenergy_item t2,mktadmin_tradetype t3 where t2.bidunit_id=t1.bidunit_id and t1.bidunit_id= t2.bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ pow_year
							+ "' and t1.bidunit_shortname='"
							+ pow_name
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
			TpyeElec_2_temp = null;
			ElecTpyeId_temp = new String[c.getCount()];
			TpyeElec_2_temp = new String[c.getCount()];

			int i1 = 0;
			if (sqlSelect == 0) {
				if (null != c)
					while (c.moveToNext()) {
						ElecTpyeId_temp[i1] = c.getString(c
								.getColumnIndex("TradeType_Name"));
						TpyeElec_2_temp[i1] = c.getString(c
								.getColumnIndex("Energy"));
						System.out.println("type--->" + ElecTpyeId_temp[i1]);
						System.out.println("energy--->" + TpyeElec_2_temp[i1]);
						i1++;
					}
				if (null != c) {
					c.close();
					c = null;
				}
			} else if (sqlSelect == 2) {
				if (null != c)
					while (c.moveToNext()) {
						ElecTpyeId_temp[i1] = c.getString(c
								.getColumnIndex("TradeType_Name"));
						TpyeElec_2_temp[i1] = c.getString(c
								.getColumnIndex("Net_Energy"));
						System.out.println("type--->" + ElecTpyeId_temp[i1]);
						System.out.println("energy--->" + TpyeElec_2_temp[i1]);
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
				TpyeElec_2[i][item] = TpyeElec_2_temp[item];
				System.out.println(ElecTpyeId[i][item]);
			}

			if (sqlSelect == 2) {
				double elec_every_sum = 0;
				for (int i11 = 0; i11 < ElecTpyeId_temp.length; i11++) {
					elec_every_sum = elec_every_sum
							+ Double.parseDouble(TpyeElec_2_temp[i11]);
				}
				ELEC_2[i - 1] = elec_every_sum;
				elec_every_sum = 0;

			}

		}
	}
	protected void getData2YearAndData() {
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，实际发电量
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		pow_year = TIME_result[powersC_right.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c2 = database
				.rawQuery(
						"select  strftime('%m',t2.mkt_date) as month ,t2.energy as energy from MktAdmin_BidUnits t1,MktSbs_Result_Type t2 where t1.bidunit_shortname='"
								+ pow_name
								+ "' and t1.bidunit_id= t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date) = '"
								+ pow_year
								+ "' and t2.Tradetype_Id=0 order by month",
						null);
		MONTHS = null;
		ELEC_TRUE = null;
		MONTHS = new double[c2.getCount()];
		ELEC_TRUE = new double[c2.getCount()];
		int l = 0;
		System.out.println("准备赋值MONTHS");
		while (c2.moveToNext()) {
			System.out.println("MONTHS赋值中。。。");
			MONTHS[l] = Double
					.valueOf(c2.getString(c2.getColumnIndex("month")));
			System.out.println("得到---->" + MONTHS[l]);

			System.out.println("ELEC_TRUE赋值中。。。");
			ELEC_TRUE[l] = c2.getDouble(c2.getColumnIndex("energy"));
			System.out.println("得到---->" + ELEC_TRUE[l]);
			l++;
		}
		if (null != c2) {
			c2.close();
			c2 = null;
		}
		// 获取计划数据
		Cursor c21 = database
				.rawQuery(
						"select  distinct strftime('%m',t2.mkt_date) as month,t2.net_energy as energy from MktAdmin_BidUnits t1,MktPlan_Gen_DecomEnergy_Item t2 where  t1.bidunit_id= t2.bidunit_id and t1.bidunit_shortname='"
								+ pow_name
								+ "' and strftime('%Y',t2.mkt_date)='"
								+ pow_year
								+ "'and t2.decom_type=2 and t2.TradeType_Id='100007000000194' order by month",
						null);
		ELEC_PLAN = null;
		MONTHS_temp = null;
		MONTHS_temp = new double[c21.getCount()];
		ELEC_PLAN = new double[c21.getCount()];
		int l1 = 0;
		while (c21.moveToNext()) {
			System.out.println("MONTHS_temp赋值中。。。");
			MONTHS_temp[l1] = Double.valueOf(c21.getString(c21
					.getColumnIndex("month")));
			System.out.println("得到---->" + MONTHS_temp[l1]);

			System.out.println("ELEC_PLAN赋值中。。。");
			ELEC_PLAN[l1] = c21.getDouble(c21.getColumnIndex("energy"));
			System.out.println("得到---->" + ELEC_PLAN[l1]);
			l1++;
		}
		if (null != c21) {
			c21.close();
			c21 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
		System.out.println("已得到绘图属性");

		// 更改plan_a plan_b
		plan_a = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		plan_b = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		for (int i = 0; i < MONTHS.length; i++) {
			System.out.println("(int)MONTHS[i] - 1" + ((int) MONTHS[i] - 1));
			plan_b[(int) MONTHS[i] - 1] = ELEC_TRUE[i];
		}
		for (int i = 0; i < MONTHS_temp.length; i++) {
			System.out.println("(int)MONTHS_temp[i] - 1"
					+ ((int) MONTHS_temp[i] - 1));
			plan_a[(int) MONTHS_temp[i] - 1] = ELEC_PLAN[i];
		}
		getDataMaxComplete();
	}

	private void getData2Year() {
		// 开始根据电厂公司名字 获取年份数据
		// 需判断名字 对应的年份列表
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c331 = database
				.rawQuery(
						"Select distinct strftime('%Y',t1.Mkt_Date) as year from MktPlan_Gen_DecomEnergy_Item t1,MktAdmin_BidUnits t2 where t1.bidunit_id=t2.bidunit_id and t2.bidunit_shortname='"
								+ pow_name + "' order by year", null);
		TIME_result = null;
		TIME_result = new String[c331.getCount()];
		int k31 = 0;
		System.out.println("准备赋值time");
		while (c331.moveToNext()) {
			System.out.println("time赋值中。。。");
			TIME_result[k31] = c331.getString(c331.getColumnIndex("year"));
			System.out.println("得到---->" + TIME_result[k31]);
			k31++;
		}
		if (null != c331) {
			c331.close();
			c331 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	private void getData2Wheel() {
		// 在此调用数据库数据，将电厂名称写入 DC_NAME_result 年份，TIME_result ，图表月months
		// 调用dbhelper
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("已打开数据库");
		Cursor c311 = database
				.rawQuery(
						"select distinct t2.bidunit_shortname as name from MktPlan_Gen_DecomEnergy_Item t1,MktAdmin_BidUnits t2 where t2.sched_type in(0,1,2) and t1.bidunit_id=t2.bidunit_id order by name",
						null);
		DC_NAME_result = null;
		DC_NAME_result = new String[c311.getCount()];
		int j31 = 0;
		System.out.println("准备赋值");
		while (c311.moveToNext()) {
			System.out.println("赋值中。。。");
			DC_NAME_result[j31] = c311.getString(c311.getColumnIndex("name"));
			System.out.println("得到---->" + DC_NAME_result[j31]);
			j31++;
		}
		if (null != c311) {
			c311.close();
			c311 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	protected void getData1YearAndData() {
		// 设置滚动后图表数据
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		pow_year = TIME_result[powersC_right.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c2 = database
				.rawQuery(
						"select  strftime('%m',t2.mkt_date) as month,t2.net_energy as energy from MktAdmin_BidUnits t1,MktPlan_Gen_DecomEnergy_Item t2 where  t1.bidunit_id= t2.bidunit_id and t1.bidunit_shortname='"
								+ pow_name
								+ "' and strftime('%Y',t2.mkt_date)='"
								+ pow_year
								+ "'and t2.decom_type=2 and t2.Trade_Id='100007000000194' order by month",
						null);
		MONTHS = null;
		ELEC = null;
		MONTHS = new double[c2.getCount()];
		ELEC = new double[c2.getCount()];
		int l = 0;
		System.out.println("准备赋值MONTHS");
		while (c2.moveToNext()) {
			System.out.println("MONTHS赋值中。。。");
			MONTHS[l] = Double
					.valueOf(c2.getString(c2.getColumnIndex("month")));
			System.out.println("得到---->" + MONTHS[l]);

			System.out.println("ELEC赋值中。。。");
			ELEC[l] = c2.getDouble(c2.getColumnIndex("energy"));
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
	}

	private void getData1Year() {
		// 开始根据电厂公司名字 获取年份数据
		// 需判断名字 对应的年份列表
		// 语句：Select distinct strftime('%Y',t1.Mkt_Date) as year from
		// MktSbs_Result_Type t1,MktAdmin_BidUnits t2 where
		// t1.sbs_bidunit_id=t2.bidunit_id and
		// t2.bidunit_name='陕西华电蒲城发电有限责任公司'
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c111 = database
				.rawQuery(
						"Select distinct strftime('%Y',t1.Mkt_Date) as year from MktPlan_Gen_DecomEnergy_Item t1,MktAdmin_BidUnits t2  where t1.bidunit_id=t2.bidunit_id and t2.bidunit_shortname='"
								+ pow_name + "'", null);
		TIME_result = null;
		TIME_result = new String[c111.getCount()];
		int k1 = 0;
		System.out.println("准备赋值time");
		while (c111.moveToNext()) {
			System.out.println("time赋值中。。。");
			TIME_result[k1] = c111.getString(c111.getColumnIndex("year"));
			System.out.println("得到---->" + TIME_result[k1]);
			k1++;
		}
		if (null != c111) {
			c111.close();
			c111 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	/**
	 * @author Ternence
	 */
	private void getData1Wheel() {
		// 在此调用数据库数据，将电厂名称写入 DC_NAME_result 年份，TIME_result ，图表月months
		// Select distinct t2.bidunit_shortname as name from
		// MktPlan_Gen_DecomEnergy_Item t1,MktAdmin_BidUnits t2 where
		// t1.bidunit_id=t2.bidunit_id;
		// 调用dbhelper
		database = db.openDatabase();
		System.out.println("已打开数据库");
		Cursor c11 = database
				.rawQuery(
						"Select distinct t2.bidunit_shortname as name from MktPlan_Gen_DecomEnergy_Item t1,MktAdmin_BidUnits t2 where t2.sched_type in(0,1,2) and t1.bidunit_id=t2.bidunit_id order by name",
						null);
		DC_NAME_result = null;
		DC_NAME_result = new String[c11.getCount()];
		int j1 = 0;
		System.out.println("准备赋值");
		while (c11.moveToNext()) {
			System.out.println("赋值中。。。");
			DC_NAME_result[j1] = c11.getString(c11.getColumnIndex("name"));
			System.out.println("得到---->" + DC_NAME_result[j1]);
			j1++;
		}
		if (null != c11) {
			c11.close();
			c11 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	/**
	 * 获取最大数
	 */
	protected void getDataMax() {
		float tempN = 0;
		if (ELEC == null || (ELEC != null && ELEC.length == 0)) {
			tempN = 70000;
		} else {
			for (int i = 0; i < ELEC.length; i++) {
				if (tempN <= ELEC[i]) {
					tempN = (float) ELEC[i];
				}
			}

		}
		System.out.println("图表最大y-----〉" + tempN);
		MAX_Y = (int) ((int) tempN * 1.2);

	}

	/**
	 * getData0YearAndData()
	 * 
	 * @author Ternence
	 */
	private void getData0YearAndData() {
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		pow_year = TIME_result[powersC_right.getCurrentItem()];
		System.out.println("year 的数量--->" + TIME_result.length);
		if (TIME_result.length <= 1) {
			System.out.println("只有一年的数据");
			db.closeDatabase();
			System.out.println("已关闭数据库");
			System.out.println("已得到绘图属性");

		} else {
			System.out.println("用户选择------>" + pow_name);

			Cursor c2 = database
					.rawQuery(
							"select distinct strftime('%m',t2.mkt_date) as month,t2.energy as energy from MktAdmin_BidUnits t1,MktSbs_Result_Type t2 where t1.bidunit_shortname='"
									+ pow_name
									+ "' and t1.bidunit_id= t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date)='"
									+ pow_year
									+ "' and tradetype_id=0 order by month",
							null);
			MONTHS = null;
			ELEC = null;
			MONTHS = new double[c2.getCount()];
			ELEC = new double[c2.getCount()];
			int l = 0;
			System.out.println("准备赋值MONTHS");
			while (c2.moveToNext()) {
				System.out.println("MONTHS赋值中。。。");
				MONTHS[l] = Double.valueOf(c2.getString(c2
						.getColumnIndex("month")));
				System.out.println("得到---->" + MONTHS[l]);

				System.out.println("ELEC赋值中。。。");
				ELEC[l] = c2.getDouble(c2.getColumnIndex("energy"));
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
		}
	}

	/**
	 * getData0Year();
	 * 
	 * @author Ternence
	 */
	private void getData0Year() {
		// TODO Auto-generated method stub
		// 开始根据电厂公司名字 获取年份数据
		// 需判断名字 对应的年份列表
		// 语句：Select distinct strftime('%Y',t1.Mkt_Date) as year from
		// MktSbs_Result_Type t1,MktAdmin_BidUnits t2 where
		// t1.sbs_bidunit_id=t2.bidunit_id and
		// t2.bidunit_name='陕西华电蒲城发电有限责任公司'
		database = db.openDatabase();
		System.out.println("已打开数据库");
		// 获取滚轮选择的名字 ，改变sql语句，获取年份数据
		System.out.println(powersC.getCurrentItem());
		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		System.out.println("用户选择------>" + pow_name);

		Cursor c1 = database
				.rawQuery(
						"Select distinct strftime('%Y',t1.Mkt_Date) as year from MktSbs_Result_Type t1,MktAdmin_BidUnits t2 where t1.sbs_bidunit_id=t2.bidunit_id and t2.bidunit_shortname='"
								+ pow_name + "'", null);
		TIME_result = null;
		TIME_result = new String[c1.getCount()];
		int k = 0;
		System.out.println("准备赋值time");
		while (c1.moveToNext()) {
			System.out.println("time赋值中。。。");
			TIME_result[k] = c1.getString(c1.getColumnIndex("year"));
			System.out.println("得到---->" + TIME_result[k]);
			k++;
		}
		if (null != c1) {
			c1.close();
			c1 = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	/**
	 * 调用getData0Wheel()
	 * 
	 * @author Ternence
	 */
	private void getData0Wheel() {
		// 在此调用数据库数据，将电厂名称写入 DC_NAME_result 年份，TIME_result ，图表月months
		// 获取竞价单元名称

		// 调用dbhelper
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("已打开数据库");
		Cursor c = database
				.rawQuery(
						"select distinct t2.bidunit_shortname as name from MktSbs_Result_Type t1,MktAdmin_BidUnits t2 where t2.sched_type in(0,1,2) and t1.sbs_bidunit_id=t2.bidunit_id order by t2.bidunit_shortname ASC",
						null);
		DC_NAME_result = null;
		DC_NAME_result = new String[c.getCount()];
		int j = 0;
		System.out.println("准备赋值");
		while (c.moveToNext()) {
			System.out.println("赋值中。。。");
			DC_NAME_result[j] = c.getString(c.getColumnIndex("name"));
			System.out.println("得到---->" + DC_NAME_result[j]);
			j++;
		}
		if (null != c) {
			c.close();
			c = null;
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	/**
	 * 获得各个分月电量数据
	 */
	private void getEveryMonthElec() {

		System.out.println("执行了2-----------");
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("已打开数据库");

		ElecTpyeId = new String[13][10];
		TpyeElec = new double[13][10];

		// new 2013-9-4
		getDividedElec_yearBaseElec_1 = new double[13];
		getDividedElec_yearTrade_2 = new double[13];
		getDividedElec_yearSell_3 = new double[13];
		getDividedElec_yearTieline_4 = new double[13];
		getDividedElec_yearQuota_5 = new double[13];
		getDividedElec_Region_6 = new double[13];
		getDividedElec_Prov_7 = new double[13];
		getDividedElec_Other_8 = new double[13];
		getDividedElec_Tieline_9 = new double[13];

		pow_name = DC_NAME_result[powersC.getCurrentItem()];
		pow_year = TIME_result[powersC_right.getCurrentItem()];

		System.out.println("pow_name--now" + pow_name + "pow_year---now"
				+ pow_year);
		// new 2013-9-4
		for (int i = 1; i <= MONTHS.length; i++) {
			String sql = null;

			if (sqlSelect == 0) {
				if (i >= 10) {
					sql = "select t3.tradetype_id,t3.tradetype_name,t2.energy from mktadmin_bidunits t1,mktsbs_result_type t2, mktadmin_tradetype t3 where t1.bidunit_shortname='"
							+ pow_name
							+ "' and t1.bidunit_id=t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ pow_year
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ i
							+ "' and t2.Tradetype_Id= t3.tradetype_id and t2.period=0";

				} else {
					sql = "select t3.tradetype_id,t3.tradetype_name,t2.energy from mktadmin_bidunits t1,mktsbs_result_type t2, mktadmin_tradetype t3 where t1.bidunit_shortname='"
							+ pow_name
							+ "' and t1.bidunit_id=t2.sbs_bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ pow_year
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ "0"
							+ i
							+ "' and t2.Tradetype_Id= t3.tradetype_id and t2.period=0";

				}
			} else if (sqlSelect == 1) {
				if (i >= 10) {
					sql = "select t3.tradetype_id,t3.tradetype_name,t2.net_energy from mktadmin_bidunits t1,mktplan_gen_decomenergy_item t2,mktadmin_tradetype t3 where t2.bidunit_id=t1.bidunit_id and t1.bidunit_id= t2.bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ pow_year
							+ "' and t1.bidunit_shortname='"
							+ pow_name
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ i
							+ "' and t2.decom_type=2  and t2.TradeType_Id=t3.tradetype_id";

				} else {
					sql = "select t3.tradetype_id,t3.tradetype_name,t2.net_energy from mktadmin_bidunits t1,mktplan_gen_decomenergy_item t2,mktadmin_tradetype t3 where t2.bidunit_id=t1.bidunit_id and t1.bidunit_id= t2.bidunit_id and strftime('%Y',t2.mkt_date) = '"
							+ pow_year
							+ "' and t1.bidunit_shortname='"
							+ pow_name
							+ "' and strftime('%m',t2.mkt_date) = '"
							+ "0"
							+ i
							+ "' and t2.decom_type=2  and t2.TradeType_Id=t3.tradetype_id";

				}
			}

			Cursor c = database.rawQuery(sql, null);

			ElecTpyeId_temp = null;
			TpyeElec_temp = null;
			ElecTpyeId_temp = new String[c.getCount()];
			TpyeElec_temp = new double[c.getCount()];

			int i1 = 0;
			String TradeId = null;

			System.out.println("执行了3-----------");
			if (sqlSelect == 0) {
				while (c.moveToNext()) {
					ElecTpyeId_temp[i1] = c.getString(c
							.getColumnIndex("TradeType_Name"));
					TpyeElec_temp[i1] = Double.parseDouble(c.getString(c
							.getColumnIndex("Energy")));
					 System.out.println("type--->" + ElecTpyeId_temp[i1]);
					 System.out.println("energy--->" + TpyeElec_temp[i1]);

					// new 2013-9-4
					TradeId = c.getString(c.getColumnIndex("TradeType_Id"));

					// 1
					if (TradeId.equals("100007000000194")) {
						getDividedElec_yearBaseElec_1[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);

					}
					if (TradeId.equals("100007000001145")) {
						getDividedElec_yearTrade_2[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001160")) {
						getDividedElec_yearSell_3[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001162")) {
						getDividedElec_yearTieline_4[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001180")) {
						getDividedElec_yearQuota_5[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001181")) {
						getDividedElec_Region_6[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001182")) {
						getDividedElec_Prov_7[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001184")) {
						getDividedElec_Other_8[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001201")) {
						getDividedElec_Tieline_9[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}

					System.out.println("月--->" + (i) );
//					MONTH_ID++;
					System.out.println("cursor--->" + i1);
					// new 2013-9-4
					i1++;
				}
			} else if (sqlSelect == 1) {
				while (c.moveToNext()) {
					ElecTpyeId_temp[i1] = c.getString(c
							.getColumnIndex("TradeType_Name"));
					TpyeElec_temp[i1] = Double.parseDouble(c.getString(c
							.getColumnIndex("Net_Energy")));
					System.out.println("type--->" + ElecTpyeId_temp[i1]);
					System.out.println("energy--->" + TpyeElec_temp[i1]);

					// new 2013-9-4
					TradeId = c.getString(c.getColumnIndex("TradeType_Id"));

					// 1
					if (TradeId.equals("100007000000194")) {
						getDividedElec_yearBaseElec_1[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);

					}
					if (TradeId.equals("100007000001145")) {
						getDividedElec_yearTrade_2[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001160")) {
						getDividedElec_yearSell_3[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001162")) {
						getDividedElec_yearTieline_4[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001180")) {
						getDividedElec_yearQuota_5[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001181")) {
						getDividedElec_Region_6[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001182")) {
						getDividedElec_Prov_7[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001184")) {
						getDividedElec_Other_8[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}
					if (TradeId.equals("100007000001201")) {
						getDividedElec_Tieline_9[i-1] = TpyeElec_temp[i1];
						System.out.println("TradeId" + "TradeType_Name"
								+ TradeId + ElecTpyeId_temp[i1]);
					}

					System.out.println("月--->" + (i) );
//					MONTH_ID++;
					System.out.println("cursor--->" + i1);
					// new 2013-9-4
					i1++;
				}
			}

			MONTH_ID = 1;

			// 赋值
			// for (int item = 0; item < ElecTpyeId_temp.length; item++) {
			// ElecTpyeId[i][item] = ElecTpyeId_temp[item];
			// TpyeElec[i][item] = TpyeElec_temp[item];
			// System.out.println(ElecTpyeId[i][item]);
			// }
			if (null != c) {
				c.close();
				c = null;
			}
		}
		db.closeDatabase();
		System.out.println("已关闭数据库");
	}

	/**
	 * 上方滚轮控件
	 */
	private void loadingPowersChoose() {
		//
		powersC = (WheelView) findViewById(R.id.powers_choose);
		powersC.setVisibleItems(5);
		powersC.setAdapter(new ArrayWheelAdapter<String>(POWERS));

		powersC_right = (WheelView) findViewById(R.id.year_choose);
		powersC_right.setVisibleItems(5);
		powersC_right.setAdapter(new ArrayWheelAdapter<String>(TIME));

		timeC = (WheelView) findViewById(R.id.time_choose);
		timeC.setVisibleItems(5);
		timeC.setAdapter(new ArrayWheelAdapter<String>(TIME));

		itemC = (WheelView) findViewById(R.id.item_choose);
		itemC.setVisibleItems(5);
		itemC.setAdapter(new ArrayWheelAdapter<String>(ITEM));

		jizu = (WheelView) findViewById(R.id.jizu_name_choose);
		jizu.setVisibleItems(5);
		jizu.setAdapter(new ArrayWheelAdapter<String>(jizu_name));

		yue = (WheelView) findViewById(R.id.yue_choose);
		yue.setVisibleItems(5);
		yue.setAdapter(new ArrayWheelAdapter<String>(yuefen));

		textChoose_right = (TextView) findViewById(R.id.text_right_choose);
		yuechoose = (TextView) findViewById(R.id.text_zuiright_choose);
	}

	/**
	 * 确定按钮
	 */
	private void loadingOKbtn() {
		title = (TextView) findViewById(R.id.vipchoose_title);

		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 需要选择radiobutton 而且变换跳转内容的使用unuseRadio 默认0不使用
				switch (radioChoose) {
				case 0:
					// 计划 的改变底部图例
					if (sqlSelect == 2) {
						List<double[]> d_p = new ArrayList<double[]>();
						 get2EveryMonthElec() ;
						d_p.add(ELEC_2);
						d_p.add(plan_b);
						List<String[]> tags_p = new ArrayList<String[]>();
						tags_p.add(new String[] { "年发电量", "月份", "发电量" });
						tags_p.add(new String[] { "计划发电量", "实际发电量" });
						String title_p = " ";
						getDataMaxComplete();
						System.out.println(MAX_Y);
						SalesBarChart c = new SalesBarChart(MAX_Y);
						startActivity(c.execute(Vip_Info.this, d_p, tags_p,
								title_p));
					}
					// 实际的改变底部图例
					if (sqlSelect == 3) {
						SalesStackedBarChartDecom c = new SalesStackedBarChartDecom();
						double[] ds = new double[plan_b.length + 1];
						for (int i = 0; i < ds.length; i++) {
							ds[i] = 0;
							if (i < plan_b.length)
								ds[i] = plan_b[i];
							System.out.println(ds[i]);
						}
						double Contract_Elec = 0;
						database = db.openDatabase();

						Cursor c32 = database
								.rawQuery(
										"select strftime('%Y',t2.st_date) as year,sum(t3.SALE_NETENERGY)as sum from mktadmin_bidunits t1,MktTrade_Contract_Energy t2, mkttrade_contract_energyitems t3 where t1.bidunit_shortname='"
												+ pow_name
												+ "' AND t1.bidunit_id=t2.Sale_Unitid and t2.Contract_Id=t3.Contract_Id and  strftime('%Y',t2.st_date)='"
												+ pow_year
												+ "' group by  strftime('%Y',t2.st_date)",
										null);

						while (c32.moveToNext()) {
							Contract_Elec = c32.getDouble(c32
									.getColumnIndex("sum"));
						}
						if (null != c32) {
							c32.close();
							c32 = null;
						}
						db.closeDatabase();
						List<double[]> list = new ArrayList<double[]>();
						list.add(ds);
						startActivity(c.execute(Vip_Info.this, list,
								Contract_Elec, new String[] { "月份", "发电量",
										"合同执行情况" }));
						// tags_p.add(new String[] { "合同发电量", "实际发电量" });
					}

					break;
				case 1:
					System.out.println("执行了1-----------");
					getEveryMonthElec();
					SalesStackedBarChartMonthColors c = new SalesStackedBarChartMonthColors();
					System.out.println("执行了4-----------");
					pic_elec = null;
					pic_elec = new double[13][13];
					for (int i = 1; i < TpyeElec.length; i++) {
						for (int j = 0; j < TpyeElec[i].length; j++) {
							pic_elec[j][i - 1] = TpyeElec[i][j];
						}
					}

					List<double[]> list = new ArrayList<double[]>();
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_yearBaseElec_1"
								+ "[" + i + "]"
								+ getDividedElec_yearBaseElec_1[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_yearTrade_2" + "["
								+ i + "]" + getDividedElec_yearTrade_2[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_yearSell_3" + "["
								+ i + "]" + getDividedElec_yearSell_3[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_yearTieline_4" + "["
								+ i + "]" + getDividedElec_yearTieline_4[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_yearQuota_5" + "["
								+ i + "]" + getDividedElec_yearQuota_5[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_Region_6" + "[" + i
								+ "]" + getDividedElec_Region_6[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_Prov_7" + "[" + i
								+ "]" + getDividedElec_Prov_7[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_Other_8" + "[" + i
								+ "]" + getDividedElec_Other_8[i]);
					}
					for (int i = 0; i < 13; i++) {
						System.out.println("getDividedElec_Tieline_9" + "[" + i
								+ "]" + getDividedElec_Tieline_9[i]);
					}
					list.add(getDividedElec_yearBaseElec_1);
					list.add(getDividedElec_yearTrade_2);
					list.add(getDividedElec_yearSell_3);
					list.add(getDividedElec_yearTieline_4);
					list.add(getDividedElec_yearQuota_5);
					list.add(getDividedElec_Region_6);
					list.add(getDividedElec_Prov_7);
					list.add(getDividedElec_Other_8);
					list.add(getDividedElec_Tieline_9);
					System.out.println("执行了5-----------");
					startActivity(c.execute(Vip_Info.this, list, 0,
							new String[] { "月份", "发电量", " " }));

					// barChart = new SalesStackedBarChart(MAX_Y);
					// startActivity(barChart.execute(Vip_Info.this, d2, tags2,
					// title2));
					break;

				case 2:
					// 为bundle 绑定数据到private 中
					pow_name = DC_NAME_result[powersC.getCurrentItem()];
					pow_year = TIME_result[powersC_right.getCurrentItem()];
					System.out.println(pow_name + pow_year);

					Bundle bundle = new Bundle();
					bundle.putInt("sqlSelect", sqlSelect);
					bundle.putString("PowerName", pow_name);
					bundle.putString("PowerYear", pow_year);

					Intent intent2 = new Intent();
					intent2.putExtras(bundle);
					intent2.setClass(Vip_Info.this, Vip_PrivateActivity.class);
					startActivity(intent2);
					break;
				case 3:

					System.out.println("要传的是" + pow_jizu);
					Bundle bundle2 = new Bundle();
					bundle2.putString("jizu", pow_jizu);

					bundle2.putString("participantname", pow_name);

					Intent intent3 = new Intent();
					intent3.putExtras(bundle2);
					intent3.setClass(Vip_Info.this,
							Vip_JXJH_Contract_Choose.class);
					startActivity(intent3);
					break;

				case 4:
					/**
					 * jinhang 跳到listview privateActivity
					 */
					Intent intent4 = new Intent();
					intent4.setClass(Vip_Info.this, Vip_Genutilizehour.class);
					startActivity(intent4);
					break;
				case 5:
					/**
					 * Ternence 跳到listview vip_show_comlete
					 */
					pow_name = DC_NAME_result[powersC.getCurrentItem()];
					pow_year = TIME_result[powersC_right.getCurrentItem()];
					Bundle bundle1 = new Bundle();
					bundle1.putInt("sqlSelect", sqlSelect);
					bundle1.putString("PowerName", pow_name);
					bundle1.putString("PowerYear", pow_year);
					Intent intent5 = new Intent();
					intent5.putExtras(bundle1);
					intent5.setClass(Vip_Info.this, Vip_pub_two.class);
					startActivity(intent5);
					break;
				case 6:
					/**
					 * jinhang 跳到listview Vip_jiesuan
					 */
					pow_name = DC_NAME_result[powersC.getCurrentItem()];
					pow_year = TIME_result[powersC_right.getCurrentItem()];
					pow_month = True_MONTHS[yue.getCurrentItem()];

					System.out.println("跳转前 month" + pow_month);

					Bundle bundle5 = new Bundle();
					bundle5.putInt("sqlSelect", sqlSelect);
					bundle5.putString("PowerName", pow_name);
					bundle5.putString("PowerYear", pow_year);
					bundle5.putString("PowerMonth", pow_month);
					Intent intent6 = new Intent();
					intent6.putExtras(bundle5);
					intent6.setClass(Vip_Info.this, Vip_jiesuan.class);
					startActivity(intent6);
					break;
				case 7:
					/**
					 * jinhang 跳到listview One_Share
					 */

					textView = (TextView) findViewById(R.id.text_choose2);
					title.setText("机组年度检修计划数据");
					textChoose.setText("市场成员");
					textView.setText("机组名称");
					radiogroup.setVisibility(8);
					radiobtn_chat.setVisibility(8);
					radiobtn_table.setVisibility(8);
					jizu.setVisibility(0);
					// 更改轮子竞价单元

					sqlSelect = 7;

					database = db.openDatabase();
					System.out.println("已打开数据库");
					String sql1 = "select distinct t1.Participant_ShortName from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.Generator_Id";
					Cursor c12 = database.rawQuery(sql1, null);
					DC_NAME_result = null;
					DC_NAME_result = new String[c12.getCount()];
					int j11 = 0;
					System.out.println("准备赋值");
					while (c12.moveToNext()) {
						System.out.println("赋值中。。。");
						DC_NAME_result[j11] = c12.getString(c12
								.getColumnIndex("Participant_ShortName"));
						System.out.println("得到---->" + DC_NAME_result[j11]);
						j11++;
					}
					if (null != c12) {
						c12.close();
						c12 = null;
					}
					db.closeDatabase();
					System.out.println("已关闭数据库");
					powersC.setAdapter(new ArrayWheelAdapter<String>(
							DC_NAME_result));
					POWERS = null;
					POWERS = DC_NAME_result;
					System.out.println("已成功更改滑轮1数据");

					// 更改轮子机组名称
					database = db.openDatabase();
					System.out.println("已打开数据库");
					// 获取滚轮选择的名字 ，改变sql语句，获取机组名称数据
					System.out.println(powersC.getCurrentItem());

					pow_name = DC_NAME_result[powersC.getCurrentItem()];// 当前选择的市场成员

					System.out.println("用户选择------>" + pow_name);
					String sql2 = "select distinct  t2.PHYUNIT_NAME as name from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.generator_id and t1.Participant_shortname='"
							+ pow_name + "'";
					Cursor c71 = database.rawQuery(sql2, null);

					jizu_result = null;
					jizu_result = new String[c71.getCount()];
					int k11 = 0;
					while (c71.moveToNext()) {
						jizu_result[k11] = c71.getString(c71
								.getColumnIndex("name"));
						System.out.println("机组---->" + jizu_result[k11]);
						k11++;
					}
					if (null != c71) {
						c71.close();
						c71 = null;
					}
					db.closeDatabase();
					System.out.println("已关闭数据库");
					// 给右边轮子赋值；
					jizu.setAdapter(new ArrayWheelAdapter<String>(jizu_result));

					// 电厂名称滑轮设置连同
					powersC.addScrollingListener(new OnWheelScrollListener() {

						@Override
						public void onScrollingStarted(WheelView wheel) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onScrollingFinished(WheelView wheel) {
							// TODO Auto-generated method stub
							// 更改轮子机组名称
							database = db.openDatabase();
							System.out.println("已打开数据库");
							// 获取滚轮选择的名字 ，改变sql语句，获取机组名称数据
							System.out.println(powersC.getCurrentItem());

							pow_name = DC_NAME_result[powersC.getCurrentItem()];// 当前选择的市场成员

							System.out.println("用户选择------>" + pow_name);
							String sql2 = "select distinct  t2.PHYUNIT_NAME as name from MktAdmin_Participants t1,MktAdmin_PhyUnits t2,MktPlan_Repair_Plan t3 where t1.Participant_Id=t2.Participant_Id and t2.Phyunit_Id=t3.generator_id and t1.Participant_shortname='"
									+ pow_name + "'";
							Cursor c71 = database.rawQuery(sql2, null);

							jizu_result = null;
							jizu_result = new String[c71.getCount()];
							int k11 = 0;
							while (c71.moveToNext()) {
								jizu_result[k11] = c71.getString(c71
										.getColumnIndex("name"));
								System.out
										.println("机组---->" + jizu_result[k11]);
								k11++;
							}
							if (null != c71) {
								c71.close();
								c71 = null;
							}
							db.closeDatabase();
							System.out.println("已关闭数据库");
							// 给右边轮子赋值；
							jizu.setAdapter(new ArrayWheelAdapter<String>(
									jizu_result));
						}
					});

					// 机组名称滑轮增加监听
					jizu.addScrollingListener(new OnWheelScrollListener() {
						@Override
						public void onScrollingStarted(WheelView wheel) {
						}

						@Override
						public void onScrollingFinished(WheelView wheel) {
						}
					});

					radioChoose = 3;// 跳到listview维修计划
					break;

				default:
					break;
				}
			}
		});
	}
}
