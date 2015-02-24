package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.dome.common.util.ExitApplication;
import com.dome.common.util.UIHelper;
import com.dome.common.util.UIHelper.Something;
import com.dome.db.DatabaseHelper;

/**
 * ���ܣ���Աҳ��
 * 
 * @author ��
 * 
 * 
 */
@SuppressLint("NewApi")
public class FourActivity extends Activity {

	// jinhang
	SQLiteDatabase database;
	DatabaseHelper db;
	String participant_id;
	String contractname[];// 3
	String bidunitname[];// 3
	String contract_id[];// 2
	String contract_name2[];// 2
	String bidunit_name2[];// 2
	String imei;
	public static final int WANTLOGIN = 0;
	public static final int TAGPUB = 2;
	public static final int TAGPRI = 3;
	public static final String CURRLISTSTRING = "currlist";
	private String CURRLISTSTRING_LEFT = "one";
	private String CURRLISTSTRING_RIGHT = "two";
	public static final int INVALIDELIST = 4;

	private boolean ISLOGIN = true;
	private boolean ISPUBLIC = true;

	// ��Ҫ�ĸ����б�
	private ListView list;
	List<Map<String, String>> listMaptemp;
	List<Map<String, String>> listMapPu = new ArrayList<Map<String, String>>();// public

	List<Map<String, String>> listMapPr = new ArrayList<Map<String, String>>();// private
	String[] strsPublic;
	String[] strsPrivate_left = null;
	String[] strsPrivate_right = null;

	// ��Ա��Ϣ��ť
	private Button btnPublic;
	private Button btnPrivate;
	private Button tvLogin;

	// ��½��ť
	TextView textView_imei;

	// ��ȡimei
	TelephonyManager myTelephonyManager;

	// ��Դ����
	private Resources rs;
	SimpleAdapter adapterpu;
	SimpleAdapter adapterpr;
	// ���������Ϣ
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case WANTLOGIN:// ���ص�½֮��Ľ���
				initForTwo();

				break;
			case INVALIDELIST:

				if (!ISPUBLIC) {
					list.setAdapter(adapterpr);
					list.setOnItemClickListener(new OnItemClickListenerPr());

				} else if (ISPUBLIC) {
					list.setAdapter(adapterpu);
					list.setOnItemClickListener(new OnItemClickListenerPu());
				}

				break;
			default:
				break;
			}
		}

	};
	private Spinner spDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rs = getResources();
		imei = getInfoIMEI();
		ExitApplication.getInstance().addActivity(this);
		// �����б�
		strsPublic = rs.getStringArray(R.array.public_messages);
		strsPrivate_left = new String[] { "�鿴��ͬ" };
		strsPrivate_right = new String[] { "��Ա�ɿ�" };

		initData();

		// �ж�����Ѿ���½��������½���Ľ���
		if (!ISLOGIN) {
			setContentView(R.layout.four);
			handler.sendEmptyMessage(WANTLOGIN);
			tvLogin = (Button) findViewById(R.id.phone_login_text);
			textView_imei = (TextView) findViewById(R.id.tv_imei);
			textView_imei.setText(getInfoIMEI());
			tvLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					handler.sendEmptyMessage(WANTLOGIN);
				}
			});
		} else {
			handler.sendEmptyMessage(WANTLOGIN);
		}
	}
	//��listview�������
	private void initData() {
		Map<String, String> mpPublic;
		Map<String, String> mpPrivate_left;

		int length = strsPublic.length;
		for (String strTemp : strsPublic) {
			mpPublic = new HashMap<String, String>();
			mpPublic.put(CURRLISTSTRING, strTemp);
			listMapPu.add(mpPublic);
		}

		for (int i = 0; i < strsPrivate_left.length; i++) {
			mpPrivate_left = new HashMap<String, String>();
			mpPrivate_left.put(CURRLISTSTRING_LEFT, strsPrivate_left[i]);
			mpPrivate_left.put(CURRLISTSTRING_RIGHT, strsPrivate_right[i]);
			listMapPr.add(mpPrivate_left);
		}

	}

	/**
	 * ���õ�¼֮��Ľ���
	 */
	private void initForTwo() {
		setContentView(R.layout.after_login);

		OnClickListenner clickListenner = new OnClickListenner();
		btnPublic = (Button) findViewById(R.id.msg_public);
		btnPublic.setBackgroundDrawable(rs
				.getDrawable(R.drawable.sapi_tab_btn_pressed));
		btnPrivate = (Button) findViewById(R.id.msg_private);

		btnPrivate.setOnClickListener(clickListenner);
		btnPublic.setOnClickListener(clickListenner);// ˽����Ϣ

		list = (ListView) findViewById(R.id.list_messages);
		list.setOverScrollMode(View.OVER_SCROLL_NEVER);

		adapterpu = new SimpleAdapter(FourActivity.this, listMapPu,
				R.layout.list_item, new String[] { CURRLISTSTRING, },
				new int[] { R.id.itemtext, });

		adapterpr = new SimpleAdapter(FourActivity.this, listMapPr,
				R.layout.private_item, new String[] { CURRLISTSTRING_LEFT,
				CURRLISTSTRING_RIGHT }, new int[] { R.id.tv_key,
				R.id.tv_data });

		list.setAdapter(adapterpu);
		/**
		 * ��ʾ�������
		 */
		list.setOnItemClickListener(new OnItemClickListenerPu());

	}

	/**
	 * ���ܣ��õ�Participant_id
	 */
	public String havaParticipant_id() {
		// jinhang �����ݿ⸳ֵ
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("�Ѵ����ݿ�");
		imei = getInfoIMEI();
		System.out.println(imei);
		String sqli = "Select participant_Id from LogIn where IMEI='" + imei
				+ "'";
		Cursor yd = database.rawQuery(sqli, null);
		// Participant_Id �����ж����� ����
		System.out.println("׼����ֵ");
		while (yd.moveToNext()) {
			System.out.println("��ֵ�С�����");
			participant_id = yd.getString(yd.getColumnIndex("Participant_Id"));
			System.out.println("�õ�---->" + participant_id);

		}

		db.closeDatabase();
		System.out.println("�ѹر����ݿ�");
		return participant_id;
	}
	/**
	 * �����ݿ�����������пմ���
	 */
	public void Participant_id_is_null() {
		new AlertDialog.Builder(this).setTitle("��ʾ��").setMessage("����ע���Ա��")
		.setPositiveButton("ȷ��", null).show();
	}
	/**
	 * ��������������������
	 */
	public void Participant_id_is_jiaoyicenter() {
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("�Ѵ����ݿ�1");

		String sqlc = "select t2.contract_name,t1.bidunit_name from MktAdmin_BidUnits t1,mkttrade_contract_energy t2 where t2.sale_unitid=t1.bidunit_id and t1.sched_type in(0,1,2)";
		Cursor cd = database.rawQuery(sqlc, null);
		// ContractName[] BidunitName[]
		System.out.println("׼����ֵ1");
		int ki = 0;
		while (cd.moveToNext()) {
			System.out.println("��ֵ��1������");

			contractname[ki] = cd.getString(cd.getColumnIndex("ContractName"));
			bidunitname[ki] = cd.getString(cd.getColumnIndex("BidunitName"));
			System.out.println("�õ�1---->" + contractname[ki] + "and"
					+ bidunitname[ki]);
			ki++;
		}
		db.closeDatabase();
		System.out.println("�ѹر����ݿ�");
		initData();
	}
	/**
	 * ��ͨ�������� ���͵���������
	 */
	public void Participant_id_is_normal() {
		// һ�������
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("�Ѵ����ݿ�2");

		String sql = "select distinct t2.contract_id,t2.contract_name,t1.bidunit_name from MktAdmin_BidUnits t1,mkttrade_contract_energy t2,mkttrade_contract_energyitems t3 where t2.contract_id=t3.contract_id and t2.sale_unitid=t1.bidunit_id and t2.sale_unitid in(Select t1.Bidunit_Id from MktAdmin_BidUnits t1 where t1.sched_type in(0,1,2) and t1.Participant_id in (Select participant_Id from LogIn where IMEI='"
				+ imei + "'))";
		Cursor sd = database.rawQuery(sql, null);
		System.out.println("׼����ֵ");
		int cc = 0;

		contract_id = new String[sd.getCount()];
		contract_name2 = new String[sd.getCount()];
		bidunit_name2 = new String[sd.getCount()];

		while (sd.moveToNext()) {
			System.out.println("��ֵ��2������");

			contract_id[cc] = sd.getString(sd.getColumnIndex("Contract_Id"));
			contract_name2[cc] = sd.getString(sd
					.getColumnIndex("Contract_Name"));
			bidunit_name2[cc] = sd.getString(sd.getColumnIndex("Bidunit_Name"));
			System.out.println("�õ�2---->" + contract_id[cc] + "and"
					+ contract_name2[cc] + "and" + bidunit_name2[cc]);
			cc++;
		}
		for (int j = 0; j < cc; j++) {
			strsPrivate_left[j] = contract_name2[j];// ��ͬ����
			strsPrivate_right[j] = bidunit_name2[j];// ���۵�Ԫ����
		}
		db.closeDatabase();
		System.out.println("�ѹر����ݿ�");
		initData();
	}
	/**
	 * item�����ࣺ���ƽ���仯
	 * @author jinhang
	 *
	 */
	class OnClickListenner implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.msg_public:
				System.out.println("ispublic" + ISPUBLIC);
				if (ISPUBLIC) {
					System.out.println("ispublic" + ISPUBLIC);
				} else {
					ISPUBLIC = true;
					System.out.println("ispublic" + ISPUBLIC);
					handler.sendEmptyMessage(INVALIDELIST);

					btnPrivate.setBackgroundDrawable(rs
							.getDrawable(R.drawable.sapi_tab_btn_normal));
					btnPublic.setBackgroundDrawable(rs
							.getDrawable(R.drawable.sapi_tab_btn_pressed));
				}

				break;
			case R.id.msg_private:
				System.out.println("siyou");
				if (ISPUBLIC) {
					ISPUBLIC = false;
					System.out.println("ispublic" + ISPUBLIC);
					handler.sendEmptyMessage(INVALIDELIST);

					btnPublic.setBackgroundDrawable(rs
							.getDrawable(R.drawable.sapi_tab_btn_normal));
					btnPrivate.setBackgroundDrawable(rs
							.getDrawable(R.drawable.sapi_tab_btn_pressed));

				} else {
					System.out.println("ispublic" + ISPUBLIC);
				}

				break;

			default:
				break;
			}
		}
	}
	/**
	 * �õ�imei����
	 * @return
	 */
	String getInfoIMEI() {
		myTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String imei = myTelephonyManager.getDeviceId();
		return imei;
	}
	/**
	 * �����б�item����
	 * @author jinhang
	 *
	 */
	class OnItemClickListenerPu implements
	android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			{
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putInt("chooseId", arg2);

				if (arg2 == 5) {
					intent.setClass(FourActivity.this, Vip_Different_Description.class);
					startActivity(intent);
				} else {
					intent.putExtras(bundle);
					intent.setClass(FourActivity.this, Vip_Info.class);
					startActivity(intent);
				}

			}
		}
	}

	/**
	 * ��ת��˽���б�ietem����
	 * 
	 * @author jinhang
	 * 
	 */
	class OnItemClickListenerPr implements
	android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String id;
			id = havaParticipant_id();
			System.out.println("�õ���--participant_id--��" + id);
			if (id == null) {
				Participant_id_is_null();
			} else if (id.equals("100003000000011") == true) {
				// Participant_id_is_jiaoyicenter();

				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				//
				// bundle.putString("imei",imei);
				// bundle.putString("participant_id",id);
				// System.out.println("Ҫ������"+imei+id);
				// intent.putExtras(bundle);
				intent.setClass(FourActivity.this, Share_Choose.class);
				startActivity(intent);
			} else if (id.equals("100003000000011") == false) {
				// Participant_id_is_normal();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				bundle.putString("imei", imei);
				bundle.putString("participant_id", id);
				System.out.println("Ҫ������" + imei + id);
				intent.putExtras(bundle);
				intent.setClass(FourActivity.this, Vip_Share.class);
				startActivity(intent);
			}

		}

	}
	/**
	 * menu������
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			UIHelper.exit(FourActivity.this, new Something() {

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
