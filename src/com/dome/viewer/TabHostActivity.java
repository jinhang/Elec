package com.dome.viewer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.dome.common.util.ExitApplication;
import com.dome.common.util.UIHelper;
import com.dome.common.util.UIHelper.Something;
import com.dome.tabchange.receiver.TabsChangeReceiver;
import com.dome.tabchange.receiver.TabsChangeReceiver.ChangeListViewShowListenner;
/**
 * 
 * @author ����ԭ ������Ŀ��ת
 * --��� �޸� ͼ����ת
 * --�� �޸� ��Ա��ת
 *
 */
public class TabHostActivity extends Activity {

	@Override
	protected void onStart() {
		super.onStart();
	}

	private RadioGroup radioGroup;

	// ҳ������
	private ViewPager mPager;
	// Tabҳ���б�
	private List<View> listViews;
	// ��ǰҳ�����
	private LocalActivityManager manager = null;

	private MyPagerAdapter mpAdapter = null;
	private int index;

	private TabsChangeReceiver changelistenner;

	private boolean isRigisted = false;

	// ����intent��������ֵ
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

	

	@Override
	protected void onPause() {
		Log.i("", "onPause()");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i("", "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {

		Log.i("", "onDestroy()");

		if (isRigisted == true) {
			this.unregisterReceiver(changelistenner);
		}

		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (getIntent() != null) {
			index = getIntent().getIntExtra("index", 0);
			mPager.setCurrentItem(index);
			setIntent(null);
		} else {
			if (index < 4) {
				index = index + 1;
				mPager.setCurrentItem(index);
				index = index - 1;
				mPager.setCurrentItem(index);

			} else if (index == 4) {
				index = index - 1;
				mPager.setCurrentItem(index);
				index = index + 1;
				mPager.setCurrentItem(index);
			}
		}
	}

	/**
	 * ��ҳ��ΪTabhost Ϊ��ܣ�ÿ��tab��תһ��activity
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.tabhost);
		registListenner();
		mPager = (ViewPager) findViewById(R.id.vPager);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		InitViewPager();
		radioGroup = (RadioGroup) this.findViewById(R.id.rg_main_btns);
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {

						case R.id.buyHomeTab:
							index = 0;
							listViews.set(
									0,
									getView("A", new Intent(
											TabHostActivity.this,
											OneActivity.class)));
							mpAdapter.notifyDataSetChanged();
							mPager.setCurrentItem(0);
							break;

						case R.id.winAfficheTab:
							index = 1;
							listViews.set(
									1,
									getView("B", new Intent(
											TabHostActivity.this,
											ChartActivity.class)));
							mpAdapter.notifyDataSetChanged();
							mPager.setCurrentItem(1);
							break;

						case R.id.accountTab:
							index = 2;
							listViews.set(
									2,
									getView("C", new Intent(
											TabHostActivity.this,
											FourActivity.class)));
							mpAdapter.notifyDataSetChanged();
							mPager.setCurrentItem(2);
							break;

						case R.id.moreTab:
							index = 3;
							listViews.set(
									3,
									getView("D", new Intent(
											TabHostActivity.this,
											FiveActivity.class)));
							mpAdapter.notifyDataSetChanged();
							mPager.setCurrentItem(3);
							break;
						default:
							break;
						}
					}
				});
	}

	/**
	 * 
	 */
	private void registListenner() {

		changelistenner = new TabsChangeReceiver(
				new ChangeListViewShowListenner() {

					public void changeListViewShow(int type) {
						if (type == 3) {
							mPager.setCurrentItem(2);
						}

					}
				});

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.dome.tabhost.change.receiver");
		if (!isRigisted)
			this.registerReceiver(changelistenner, filter);

	}

	/**
	 * ��ʼ��ViewPager
	 */
	private void InitViewPager() {
		Intent intent = null;
		listViews = new ArrayList<View>();
		mpAdapter = new MyPagerAdapter(listViews);
		intent = new Intent(TabHostActivity.this, OneActivity.class);
		listViews.add(getView("A", intent));
		intent = new Intent(TabHostActivity.this, ChartActivity.class);
		listViews.add(getView("B", intent));
		/*
		 * intent = new Intent(TabHostActivity.this, ThreeActivity.class);
		 * listViews.add(getView("B", intent));
		 */
		intent = new Intent(TabHostActivity.this, FourActivity.class);
		listViews.add(getView("C", intent));
		intent = new Intent(TabHostActivity.this, FiveActivity.class);
		listViews.add(getView("D", intent));
		mPager.setOffscreenPageLimit(0);
		mPager.setAdapter(mpAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * ViewPager������
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * ҳ���л�������ViewPager�ı�ͬ���ı�TabHost����
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int arg0) {
			manager.dispatchResume();
			switch (arg0) {
			case 0:
				index = 0;
				radioGroup.check(R.id.buyHomeTab);
				listViews.set(
						0,
						getView("A", new Intent(TabHostActivity.this,
								OneActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 1:
				index = 1;
				radioGroup.check(R.id.winAfficheTab);
				listViews.set(
						1,
						getView("B", new Intent(TabHostActivity.this,
								ChartActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			/*
			 * case 2: index = 2; radioGroup.check(R.id.integralTab);
			 * listViews.set( 2, getView("C", new Intent(TabHostActivity.this,
			 * ThreeActivity.class))); mpAdapter.notifyDataSetChanged(); break;
			 */
			case 2:
				index = 2;
				radioGroup.check(R.id.accountTab);
				listViews.set(
						2,
						getView("C", new Intent(TabHostActivity.this,
								FourActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 3:
				index = 3;
				radioGroup.check(R.id.moreTab);
				listViews.set(
						3,
						getView("D", new Intent(TabHostActivity.this,
								FiveActivity.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		System.out.println("tuichu  tabs");
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.out.println("tuichu  tabs");
			
			UIHelper.exit(TabHostActivity.this,new Something() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void doFinish() {
					ExitApplication.getInstance().exit();
				}
			});
			
		default:
			break;
		}
		return true;
	}
}