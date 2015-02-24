/**
 * 
 */
package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

/**
 * ��Ա��Ϣҳ��
 * 
 * @author ����ԭ 
 *         �޸�--Ϊ �鿴��ͬ
 * 
 */
public class Vip_private_only extends Activity {

	public Resources rs;
	private String[] ITEM_NAMES;
	private List<Map<String, String>> datalist = new ArrayList<Map<String, String>>();
	private ExpandableListView lvShow1;

	// ���ؼ�
	private Button btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rs = getResources();
		ITEM_NAMES = rs.getStringArray(R.array.tablemodel);
		setContentView(R.layout.after_login_pr_tableshow_more_mode);
		lvShow1 = (ExpandableListView) findViewById(R.id.expandableListView_pri);
		lvShow1.setGroupIndicator(null);

		// Ϊһ����Ŀ�ṩ����
		// Groups ����
		List<Map<String, Object>> Groups = new ArrayList<Map<String, Object>>();
		Map<String, Object> groupone = new HashMap<String, Object>();
		groupone.put("groupname", "��ͬ����Ϣ");
		Groups.add(groupone);
		Map<String, Object> grouptwo = new HashMap<String, Object>();
		grouptwo.put("groupname", "��ͬ��Ϣ2");
		Groups.add(grouptwo);

		// ��������List<Map<String,Object>> childone��childtwo
		// Ϊ������Ŀ�ṩ����
		// childone
		List<Map<String, Object>> childone = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < ITEM_NAMES.length; i++) {
			Map<String, Object> childonedateone = new HashMap<String, Object>();
			childonedateone.put("ID", ITEM_NAMES[i]);
			childonedateone.put("name", "��ͬ����" + i);
			childone.add(childonedateone);
		}
		List<Map<String, Object>> childtwo = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < ITEM_NAMES.length; i++) {
			Map<String, Object> childonedateone = new HashMap<String, Object>();
			childonedateone.put("ID", ITEM_NAMES[i]);
			childonedateone.put("name", "��ͬ����" + i);
			childtwo.add(childonedateone);
		}
		// Childs
		List<List<Map<String, Object>>> Childs = new ArrayList<List<Map<String, Object>>>();
		Childs.add(childone);
		Childs.add(childtwo);

		SimpleExpandableListAdapter simpleExpandListAdapter = new SimpleExpandableListAdapter(
				Vip_private_only.this, Groups,
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
				Vip_private_only.this.finish();
			}
		});

	}
}
