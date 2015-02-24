package com.dome.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dome.db.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

/**
 * ���ܣ���ҳ��ҳ�� �г�����
 * @author ��
 *
 */
public class HomeTableRow4 extends Activity {


	SQLiteDatabase database;
	DatabaseHelper db;


	public static final String CURRLISTSTRING = "currlist";
	private String CURRLISTSTRING_LEFT = "one";
	private String CURRLISTSTRING_RIGHT = "two";

	String name[];

	// ��Ҫ�ĸ����б�
	private ListView list;
	List<Map<String, String>> listMaptemp;
	List<Map<String, String>> list_market= new ArrayList<Map<String, String>>();

	List<Map<String, String>> list_participant = new ArrayList<Map<String, String>>();

	String[] strsf;
	String[] strsPrivate_left={"a","b"};


	// ��Ա��Ϣ���г���Ա��ť
	private Button btnm;
	private Button btnp;


	Button button;

	// ��Դ����
	private Resources rs;
	//listview������
	SimpleAdapter adapterm;
	SimpleAdapter adapterp;
	//��ʾ��Ϣ
	TextView title;
	TextView time;
	TextView content;
	TextView publisher;
	TextView cheaker;
    //��ʱ����
	String title_content;
	String time_content;
	String content_content;
	String publisher_content;
	String cheaker_content;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		rs = getResources();

		setContentView(R.layout.home_tablerow4);
		button=(Button)findViewById(R.id.btnback);

		btnm = (Button) findViewById(R.id.msg_market);

		btnp = (Button) findViewById(R.id.msg_participant);
		btnm.setBackgroundDrawable(rs
				.getDrawable(R.drawable.tab_select_holo));
		list = (ListView) findViewById(R.id.list_info);
		
		initData();
        /**
         * �����г����ܰ�ť
         */
		btnp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

                //����ͼƬ
				btnp.setBackgroundDrawable(rs
						.getDrawable(R.drawable.tab_select_holo));
				btnm.setBackgroundDrawable(rs
						.getDrawable(R.drawable.testround));
				//����ˢ��
				RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.market);
				layout1.setVisibility(View.INVISIBLE);
				LinearLayout layout2 = (LinearLayout) findViewById(R.id.list);
				layout2.setVisibility(View.VISIBLE);

				
			}
		});
		//��Ա���ܼ���
		btnm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//����ͼƬ
				btnm.setBackgroundDrawable(rs
						.getDrawable(R.drawable.tab_select_holo));
				btnp.setBackgroundDrawable(rs
						.getDrawable(R.drawable.testround));
				//ˢ�½���
				RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.market);
				layout1.setVisibility(View.VISIBLE);
				LinearLayout layout2 = (LinearLayout) findViewById(R.id.list);
				layout2.setVisibility(View.INVISIBLE);
				
			}


		});
		//��Ա����Item����
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int agr = arg2;
				System.out.println(name.length);
				for(int i = 0; i < name.length;i ++){
					System.out.println(name[i]);
				}
				//�пպ���������ݾ���ת
				if(name[arg2]!=null){
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					//String num=name[arg2];
					
					bundle.putString("Participant_name",name[arg2]);
					System.out.println("Ҫ������"+name[arg2]);
					intent.putExtras(bundle);
					intent.setClass(HomeTableRow4.this,Participant_Introduce.class);
					startActivity(intent);
				}
				//����Ϊ����ʾ
				else{
					tip();
				}
			}
		});
        //���ذ�ť����
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HomeTableRow4.this.finish();	
			}
		});
	}
	/**
	 * ����Ϊ����ʾ
	 */
	public void tip(){
		new AlertDialog.Builder(this)
		.setTitle("��ʾ��")
		.setMessage("��ʱû�����ݣ�")
		.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int i) {
				HomeTableRow4.this.finish();
			}
		})
		.show();
	}
	/**
	 * �����ݿ����ȡ��Ա��Ϣ����
	 */
	private void havadata1() {

		
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("�Ѵ����ݿ�2");
		List<Map<String, String>> listMapPr = new ArrayList<Map<String, String>>();
		String sql="Select distinct Participant_Name from MktAdmin_Participants where Participant_Type=1";
		Cursor sd = database.rawQuery(sql,null);
		System.out.println("׼����ֵ");
		int cc=0;
		name = new String[sd.getCount()];
		System.out.println(name.length+"dfasdfsadfsd");
		while (sd.moveToNext()) {
			System.out.println("��ֵ��2������");
			name[cc]=sd.getString(sd.getColumnIndex("Participant_Name"));
			cc++;	
		}
		db.closeDatabase();
		System.out.println("�ѹر����ݿ�");
	}
	/**
	 * �����ݿ��ȡ�����г���������
	 */
	public void have_marketinfo() {
		// TODO Auto-generated method stub
		db = new DatabaseHelper(this);
		database = db.openDatabase();
		System.out.println("�Ѵ����ݿ�2");
		String sql="select distinct Topic,Pub_Time,Pub_Info,Publisher,Reviewer from  System_PubInfo where Type_ID=6";
		Cursor sd = database.rawQuery(sql,null);
		System.out.println("׼����ֵ");
		int cc=0;
		name = new String[sd.getCount()];
		while (sd.moveToNext()) {
			System.out.println("��ֵ��2������");
			title_content=sd.getString(sd.getColumnIndex("Topic"));
			time_content=sd.getString(sd.getColumnIndex("Pub_Time"));
			content_content=sd.getString(sd.getColumnIndex("Pub_Info"));
			publisher_content=sd.getString(sd.getColumnIndex("Publisher"));
			cheaker_content=sd.getString(sd.getColumnIndex("Reviewer"));
			cc++;	
		}
		db.closeDatabase();
		System.out.println("�ѹر����ݿ�");
	}
	/**
	 * ��ʾ��Ϣ��ȡ������
	 */
	private void initData() {

		title=(TextView)findViewById(R.id.title4);
		time=(TextView)findViewById(R.id.time4);
		content=(TextView)findViewById(R.id.content4);
		publisher=(TextView)findViewById(R.id.tv_publisher4);
		cheaker=(TextView)findViewById(R.id.tv_checker4);
		//�������
		have_marketinfo();
		if(title_content!=null){
			title.setText(title_content);
			time.setText(time_content);
			content.setText(content_content);
			publisher.setText(publisher_content);
			cheaker.setText(cheaker_content);
		}
		
		Map<String, String> partici;
		havadata1();
		for (int i=0;i<name.length;i++) {
			partici = new HashMap<String, String>();
			partici.put(CURRLISTSTRING,name[i]);
			list_participant.add(partici);
		}
		adapterp = new SimpleAdapter(HomeTableRow4.this, list_participant,
				R.layout.list_item, new String[] { CURRLISTSTRING },
				new int[] { R.id.itemtext });
		list.setAdapter(adapterp);
	}
}

























