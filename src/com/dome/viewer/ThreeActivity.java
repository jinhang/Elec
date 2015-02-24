package com.dome.viewer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dome.common.util.ExitApplication;
import com.dome.common.util.UIHelper;
import com.dome.common.util.UIHelper.Something;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
/**
 * 
 * @author 作废
 *
 */
public class ThreeActivity extends Activity {

	private Button btn_back;

	private ListView list;

	public String[] ITEM_NAMES;

	public Resources rs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rs = getResources();
		ITEM_NAMES = rs.getStringArray(R.array.state_gird_read_test_names);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.three);

		loadingButtons();
	}

	private void loadingButtons() {
		btn_back = (Button) findViewById(R.id.btn_home_left);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ThreeActivity.this.finish();
			}
		});

		list = (ListView) findViewById(R.id.list_year_news);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.list_item, new String[] { "text", },
				new int[] { R.id.itemtext, });

		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {

			//@SuppressLint("SdCardPath")
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				Toast.makeText(getApplicationContext(),
						"开始阅览" + (2013 - position) + "年陕西电力报",
						Toast.LENGTH_SHORT).show();
				/**************************
				 * 此处之前应该将下载好的文件放在指定文件夹/sdcard/shanxidianli/pdf即可，
				 * 点击的时候只需与相应的文件对应即可
				 *********************************/

				File file = new File("/sdcard/sxElec/yearNews/2013.pdf");
				Uri uri = Uri.fromFile(file);
				Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
				String uriString = uri.toString();
				//intent1.setClass(ThreeActivity.this, PdfViewerActivity.class);
				startActivity(intent1);

				/***************************************************************/

			}
		});
	}

	private List<Map<String, String>> getData() {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < ITEM_NAMES.length; i++) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("text", ITEM_NAMES[i]);
			list.add(map);
		}

		return list;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
         UIHelper.exit(ThreeActivity.this,new Something() {
				
				@SuppressWarnings("deprecation")
				@Override
				public void doFinish() {
					ExitApplication.getInstance().exit();
				}
			});
			break;
		default:
			break;
		}
		return true;
	}

}
