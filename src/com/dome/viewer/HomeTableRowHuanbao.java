/**
 * 
 */
package com.dome.viewer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dome.adapter.ListViewInfoAdapter;
import com.dome.adapter.ScrollingTabsAdapter2;
import com.dome.common.util.AnimHelper;
import com.dome.common.util.AppContext;
import com.dome.common.util.UIHelper;
import com.dome.httpdao.SystemPubInfo;
import com.dome.httpobtain.NetUtils;
import com.dome.httpxml.XmlPull;
import com.dome.tabchange.receiver.TabsChangeReceiver;
import com.dome.tabchange.receiver.TabsChangeReceiver.ChangeListViewShowListenner;
import com.dome.viewer.SysInfoTabsActivity.DownLoader;
import com.dome.viewer.SysInfoTabsActivity.TabsChangelistenner;
import com.dome.viewer.ab.IInfoCasActivity;
import com.dome.viewer.widget.PullToRefreshListView;
import com.dome.viewer.widget.ScrollableTabView;

/**
 * @author 金航 功能：显示环保信息 
 *   修改——张藤原    添加数据
 * 
 */
public class HomeTableRowHuanbao extends IInfoCasActivity {

	public static int newsType = AppContext.SYSINFO_TYPE_FOCUS;
	public boolean flag = false;
	// 可视化的组件
	private ImageView empty;
	private ImageView loading;
	private TextView headTv;
	private PullToRefreshListView lvNews;
	private ListViewInfoAdapter lvSysInfo;
	private Button btn_back;
	private ScrollableTabView mScrollableTabView;
	private ScrollingTabsAdapter2 mScrollingTabsAdapter;
	private Resources resources;
	private boolean isRigisted = false;

	private List<Map<String, Object>> lvNewsData = new ArrayList<Map<String, Object>>();

	public static NetUtils netUtils = new NetUtils();
	List<SystemPubInfo> sysInfos1;
	List<SystemPubInfo> sysInfos2;
	List<SystemPubInfo> sysInfos3;
	List<SystemPubInfo> sysInfos4;
	List<SystemPubInfo> sysInfos_temp;
	public static SystemPubInfo info_temp;
	public TabsChangeReceiver receiver = null;

	private String TPYE_NAME = "1";
	/**
	 * 给listview添加数据
	 * @param sysInfos1
	 * @return
	 */
	private List<Map<String, Object>> getData(List<SystemPubInfo> sysInfos1) {
		if (sysInfos1.size() == 0) {
			return null;
		}

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();

		for (SystemPubInfo temp : sysInfos1) {

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("title", temp.getTopic());
			map.put("time", temp.getPub_Time());
			list1.add(map);
		}

		return list1;
	}
	/**
	 * 从数据库获取数据
	 * @author jinhang
	 *
	 */
	class DownLoader extends
	AsyncTask<HashMap<String, String>, Integer, String> {
		String temp1 = null;


		@Override
		protected String doInBackground(HashMap<String, String>... params) {
			System.out.println("开始下载！");
			System.out.println("——————————————————执行获取list");
			sysInfos1 = AppStart.dbUtil.getLastRows(8);
			sysInfos2 = AppStart.dbUtil.getLastRows(7);
			sysInfos3 = AppStart.dbUtil.getLastRows(9);
			sysInfos4 = AppStart.dbUtil.getLastRows(10);
			System.out.println("——————————————————执行获取list）——————结束");
			return temp1;
		}

		@Override
		protected void onPostExecute(String result) {

			sysInfos_temp = sysInfos1;
			// 初始化listview控件
			lvNewsData = getData(sysInfos1);
			initNewsListView(lvNewsData);
			initScrollableTabs();
            
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resources = getResources();

		// 设置无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sysinfo_cas);
        empty = (ImageView) findViewById(R.id.content_empty);
		loading = (ImageView) findViewById(R.id.iv_loading);
		AnimHelper.animStart(loading);
        new DownLoader().execute(null);
		loadingButtons();
		initScrollableTabs();

		initViews();

	}
	/**
	 * 初始化界面
	 */
	private void initViews() {
		headTv = (TextView) findViewById(R.id.systv);
		headTv.setText("环保信息");

	}
	/**
	 * 加载界面内容
	 * @param lvNewsData
	 */
	private void initNewsListView(List<Map<String, Object>> lvNewsData) {

		if (lvNewsData == null) {
			flag = true;
			lvNewsData = new ArrayList<Map<String, Object>>();
		}

		lvSysInfo = new ListViewInfoAdapter(this,
				(ArrayList<Map<String, Object>>) lvNewsData,
				R.layout.sys_cac_list_item);
		lvNews = (PullToRefreshListView) findViewById(R.id.frame_listview_sysinfo);
		lvNews.setAdapter(lvSysInfo);

		lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				info_temp = sysInfos_temp.get(position - 1);
				String pubid = sysInfos_temp.get(position - 1).getPub_Id();
				// 跳转到系统信息内容详情
				Intent intent = new Intent(HomeTableRowHuanbao.this,
						HuanbaoLookInfoActivity.class);
				intent.putExtra("TypeName", TPYE_NAME);
				intent.putExtra("ChooseListId", (position));
				intent.putExtra("pubid", pubid);



				HomeTableRowHuanbao.this.startActivity(intent);
			}

		});

		if (flag) {
			showListView();
			redirectToNoData();
		}

		showListView();

	}
	/**
	 * 滑动效果
	 */
	private void initScrollableTabs() {
		mScrollableTabView = (ScrollableTabView) this
				.findViewById(R.id.scrollabletabview);
		mScrollingTabsAdapter = new ScrollingTabsAdapter2(this);
		mScrollableTabView.setAdapter(mScrollingTabsAdapter);
		mScrollableTabView.setViewPage(AppContext.SYSINFO_TYPE_FOCUS);
	}

	/**
	 * 刷新
	 */

	@Override
	public void refresh(Object... param) {
		int type = (Integer) param[0];

	}
	/**
	 * 
	 * @author 按钮监听显示listview
	 *
	 */
	class TabsChangelistenner implements
	TabsChangeReceiver.ChangeListViewShowListenner {


		@Override
		public void changeListViewShow(int type) {
			ArrayList<Map<String, Object>> temp = null;

			switch (type) {
			case AppContext.SYSINFO_TYPE_SESSION_INFO:
				Log.i("type", "脱硫信息");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos1);
				TPYE_NAME = "1";
				sysInfos_temp = sysInfos1;
				break;
			case AppContext.SYSINFO_TYPE_TRADE_CENTER:
				Log.i("type", "脱硝信息");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos2);
				TPYE_NAME = "2";
				sysInfos_temp = sysInfos2;
				break;
			case AppContext.SYSINFO_TYPE_WORK_FLOW:
				Log.i("type", "煤耗信息");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos3);
				sysInfos_temp = sysInfos3;
				TPYE_NAME = "3";
				break;
			case AppContext.SYSINFO_TYPE_NEW_MACHINE:
				Log.i("type", "节能减排");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos4);
				TPYE_NAME = "4";
				sysInfos_temp = sysInfos4;
				break;
			default:
				break;
			}
			if (temp == null) {
				redirectToNoData();
			} else {
				redirectToData(temp);

			}
		}

	}
	//显示返回按钮并且 监听
	private void loadingButtons() {
		btn_back = (Button) findViewById(R.id.btn_home_left);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			HomeTableRowHuanbao.this.finish();
			}
		});
	}

	// 提示无数据之前，要将listview中的数据清空
	private void redirectToNoData() {
		lvSysInfo.clearData();
		empty.setVisibility(View.VISIBLE);
		System.out.println("该项目前没有最新数据");
	}
	//加载数据
	private void redirectToData(ArrayList<Map<String, Object>> temp) {
		empty.setVisibility(View.INVISIBLE);
		lvSysInfo.setNewData(temp);
	}
	//显示listview
	private void showListView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.loading_page);
		AnimHelper.animEnd(loading);
		layout.setVisibility(View.GONE);
	}
   //menu监听
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();

		}
		this.finish();
		return true;
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

}