package com.dome.viewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

import com.dome.autoview.AutoScrollView;

/**
 * 
 * 
 * @author ����
 * 
 */
public class Vip_JXJH_Contract_Show extends Activity {
	private ListView list;
	private com.dome.autoview.AutoScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vip_jxjh_contract_show);

		scrollView = (AutoScrollView) findViewById(R.id.auto_scrollview);
	}

	/**
	 * ���忪ʼ
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		if (!scrollView.isScrolled()) {
			scrollView.setScrolled(true);
		}
		super.onStart();
	}

	/**
	 * �������
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		if (scrollView.isScrolled()) {
			scrollView.setScrolled(false);
		}
		super.onStop();
	}

}
