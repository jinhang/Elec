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
import com.dome.adapter.ScrollingTabsAdapter;
import com.dome.common.util.AnimHelper;
import com.dome.common.util.AppContext;
import com.dome.common.util.UIHelper;
import com.dome.httpdao.SystemPubInfo;
import com.dome.httpobtain.NetUtils;
import com.dome.httpxml.XmlPull;
import com.dome.tabchange.receiver.TabsChangeReceiver;
import com.dome.tabchange.receiver.TabsChangeReceiver.ChangeListViewShowListenner;
import com.dome.viewer.ab.IInfoCasActivity;
import com.dome.viewer.widget.PullToRefreshListView;
import com.dome.viewer.widget.ScrollableTabView;

/**
 * 
 * @author 周皓  获取数据
 * 
 */
public class SysInfoTabsActivity extends IInfoCasActivity {

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
	private ScrollingTabsAdapter mScrollingTabsAdapter;
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

	private List<Map<String, Object>> getData(List<SystemPubInfo> sysInfos1) {
		if (sysInfos1.size() == 0) {
			return null;
		}

		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		if (sysInfos1 == null) {
			return null;
		}

		for (SystemPubInfo temp : sysInfos1) {

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("title", temp.getTopic());
			map.put("time", temp.getPub_Time());
			list1.add(map);
		}

		return list1;
	}

	class DownLoader extends
			AsyncTask<HashMap<String, String>, Integer, String> {
		String temp1 = null;

		//此处有bug
		@Override
		protected String doInBackground(HashMap<String, String>... params) {
			System.out.println("开始下载！");
			sysInfos1 = AppStart.dbUtil.getLastRows(1);
			sysInfos2 = AppStart.dbUtil.getLastRows(0);
			sysInfos3 = AppStart.dbUtil.getLastRows(2);
			sysInfos4 = AppStart.dbUtil.getLastRows(3);
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
		// 动态注册广播接受器
		registReceiver();
		new DownLoader().execute();
		loadingButtons();
		initScrollableTabs();

		initViews();

	}

	private void initViews() {
		headTv = (TextView) findViewById(R.id.systv);
		headTv.setText("通知信息");

	}

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
				// 跳转到系统信息内容详情
				Intent intent = new Intent(SysInfoTabsActivity.this,
						LookInfoActivity.class);
				SysInfoTabsActivity.this.startActivity(intent);
			}

		});

		if (flag) {
			showListView();
			redirectToNoData();
		}

		showListView();

	}

	private void initScrollableTabs() {
		mScrollableTabView = (ScrollableTabView) this
				.findViewById(R.id.scrollabletabview);
		mScrollingTabsAdapter = new ScrollingTabsAdapter(this);
		mScrollableTabView.setAdapter(mScrollingTabsAdapter);
		mScrollableTabView.setViewPage(AppContext.SYSINFO_TYPE_FOCUS);
	}

	@Override
	public void init() {

	}

	@Override
	public void refresh(Object... param) {
		int type = (Integer) param[0];

	}

	class TabsChangelistenner implements
			TabsChangeReceiver.ChangeListViewShowListenner {

		@Override
		public void changeListViewShow(int type) {
			ArrayList<Map<String, Object>> temp = null;

			switch (type) {

			case AppContext.SYSINFO_TYPE_SESSION_INFO:

				Log.i("type", "会议通知");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos1);

				sysInfos_temp = sysInfos1;
				break;

			case AppContext.SYSINFO_TYPE_TRADE_CENTER:
				Log.i("type", "交易中心");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos2);
				sysInfos_temp = sysInfos2;
				break;
			case AppContext.SYSINFO_TYPE_WORK_FLOW:
				Log.i("type", "工作流程");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos3);

				sysInfos_temp = sysInfos3;
				break;
			case AppContext.SYSINFO_TYPE_NEW_MACHINE:
				Log.i("type", "新机调试");
				temp = (ArrayList<Map<String, Object>>) getData(sysInfos4);
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

	private void loadingButtons() {
		btn_back = (Button) findViewById(R.id.btn_home_left);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unRegistReceiver();
				SysInfoTabsActivity.this.finish();
			}
		});
	}

	// 提示无数据之前，要将listview中的数据清空
	private void redirectToNoData() {
		lvSysInfo.clearData();
		empty.setVisibility(View.VISIBLE);
		System.out.println("该项目前没有最新数据");
	}

	private void redirectToData(ArrayList<Map<String, Object>> temp) {
		empty.setVisibility(View.INVISIBLE);
		lvSysInfo.setNewData(temp);
	}

	private void showListView() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.loading_page);
		AnimHelper.animEnd(loading);
		layout.setVisibility(View.GONE);
	}

	private void registReceiver() {

		if (!isRigisted) {
			receiver = new TabsChangeReceiver(new TabsChangelistenner());
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.dome.tabs.change.receiver");
			this.registerReceiver(receiver, filter);
			isRigisted = !isRigisted;
		}

	}

	private void unRegistReceiver() {
		if (receiver != null) {
			if (isRigisted)
				this.unregisterReceiver(receiver);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			unRegistReceiver();
		}
		this.finish();
		return true;
	}

}
