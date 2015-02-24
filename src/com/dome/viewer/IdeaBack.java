package com.dome.viewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.dome.httpdao.DB_LogIn;
import com.dome.soap.SoapGet;
import com.dome.soap.SoapHeader;
import com.dome.soap.SoapPareser;
import com.dome.soap.SoapHeader.parserAndInsert;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * �������
 * 
 * @author ��
 * 
 */
public class IdeaBack extends Activity {
	private SoapObject resultObject;
	// 1, ָ��WebService�����ռ� xxxx.com Ϊ��Ҫ���ʵ�����
	String nameSpace = "http://222.24.63.111:88/SG/services/";
	// 2, ���õķ�������
	String methodName = "idea_Back";
	// 3, EndPoint
	String endPoint = "http://222.24.63.111:88/SG/services/idea_Back.asmx";
	// 4, SOAPAction
	// SOAP Action���������ռ� + ���÷���������
	String soapAction = "http://222.24.63.111:88/SG/services/idea_Back";
	int REQUESTNUM = 1;
	private SoapHeader[] headers=new SoapHeader[REQUESTNUM];
	private Button back;
	private Button send;
	private EditText idea;
    private String APIKEY;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// �ޱ���
		setContentView(R.layout.idea_back);

		back = (Button) findViewById(R.id.btn_back_contract);
		send = (Button) findViewById(R.id.btn_send_contract);
		idea = (EditText) findViewById(R.id.idea);
		idea.requestFocus();
		idea.setSelection(0);
		back.setOnClickListener(new ClickListener());
		send.setOnClickListener(new ClickListener());

	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_back_contract:
				IdeaBack.this.finish();

				break;
			case R.id.btn_send_contract:
				Toast.makeText(getApplicationContext(), "��ʼfankui ", 1000);
				if(!idea.getText().toString().equals("") && isNetworkConnected(getApplicationContext())){
					Resources rs=getApplicationContext().getResources();
					//������������
					APIKEY=rs.getString(R.string.APIKEY);//�������ļ����ҵ�apikey
					List<Map<String, String>> list1=new ArrayList<Map<String,String>>();
				    final String imei=getInfoIMEI(getApplicationContext());
				    System.out.println("�ֻ�imei��Ϊ"+imei);
					list1.add(addParams("in0",imei));
					list1.add(addParams("in1", idea.getText().toString()));
					list1.add(addParams("in2",APIKEY));
					headers[0]=getAndPut(rs, R.array.url_iead_back, list1, new parserAndInsert() {
						public void afterDown(SoapObject object) {
							
							SoapObject pubinfos = (SoapObject) object.getProperty(0);
							String out = pubinfos.getPropertyAsString("is_Ok");
							System.out.println("9999999999999999999999999999999999999"+out);
							if(out.equals("ok")){
								Toast.makeText(getApplicationContext(), "����¼����л���ķ���", 1000);
								IdeaBack.this.finish();
							}else
							{
								Toast.makeText(getApplicationContext(), "����ʧ��", 1000);
							}
							/*//�������ݵ����ݿ���
							List<DB_LogIn> infos=SoapPareser.pareseLogIn(object);
							infos.get(0).setIMEI(imei);
							System.out.println("�����ȡ  imei"+infos.get(0).getIMEI());
							AppStart.dbUtil.insertDB_LogIn(infos);*/
						}
					});
					oneHeaderRun(headers[0]);
//					SoapGet get=new SoapGet(headers,getApplicationContext());
				}else {
					Toast.makeText(IdeaBack.this, "������������������Ϊ��", 1000).show();
				}
				break;
			default:
				break;
			}
		}
	}
	/**
	 * ��ȡimei������ݿ���
	 * @param context
	 * @return
	 */
	private String getInfoIMEI(Context context) {
		TelephonyManager myTelephonyManager =(TelephonyManager) context.getSystemService("phone");
		String imei = myTelephonyManager.getDeviceId();
		return imei;
	}
	
	/**
	 * @param rs ��Դ
	 * @param configId �����ļ���ַ
	 * @param list �������
	 * @param insert ��������ִ��
	 * 
	 */
	private SoapHeader getAndPut(Resources rs,int configId,List<Map<String, String>> list,parserAndInsert insert){
		
		//��ȡ������Ϣ
		String[] request=rs.getStringArray(configId);
		
		//��Ӳ������ݿ�Ķ���
		SoapHeader header=new SoapHeader(insert);
		
		//���������ͷ��Ϣ
		header.setUrl(request[0]);
		header.setNamespace(request[1]);
		header.setMethod(request[2]);
		header.setSoapAction(request[3]);
		   
		//���ö�̬�Ĳ���
		if(list==null){
			;
		}else{
	    
		  for (int i = 0; i < list.size(); i++) {
	    	Map<String, String> map=list.get(i);
	    	header.addPropertities("in"+i,map.get("in"+i));
	     }
		}
		//��������
		return header;
	}
	/**
	 * �ж������Ƿ����
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	private Map<String, String> addParams(String a,String b){
		Map<String, String> map=new HashMap<String, String>();
		map.put(a, b);
		return map;
		
	}
	
	/**
	 * ��ÿ��������д���
	 * 
	 * @param header
	 */
	private void oneHeaderRun(SoapHeader header) {
		try {
			SoapObject rpc = new SoapObject(header.getNamespace(),
					header.getMethod());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.encodingStyle = "UTF-8";
			// һ��forѭ����һ������Ӳ���
			List<HashMap<String, String>> list = header.getPropertities();
			int lenth = list.size();
             
			for (HashMap<String, String> temp : list) {
				Set<String> parameters = temp.keySet();
				for (String string : parameters) {
					rpc.addProperty(string, temp.get(string));
				}
			}

			envelope.bodyOut = rpc;
			envelope.dotNet = false;
			envelope.setOutputSoapObject(rpc);
			HttpTransportSE ht = new HttpTransportSE(header.getUrl());
			ht.debug = true;
			try {
				ht.call(header.getSoapAction(), envelope);
			} catch (IOException e) {
				System.out.println("500���󣡣���");
				return ;
			}
			
			resultObject = (SoapObject) envelope.bodyIn;
			String is_OK = resultObject.getProperty("out").toString();
			if(is_OK.equals("ok")){
				final Toast toast=Toast.makeText(IdeaBack.this, "����¼����л���ķ���", 1000);
//				toast.setGravity(Gravity.TOP, -1, 1);
				
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						toast.show();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).start();
				IdeaBack.this.finish();
			}else
			{
				Toast toast=Toast.makeText(IdeaBack.this, "����ʧ��", 1000);
//				toast.setGravity(Gravity.TOP, -1, 1);
				toast.show();
			}
			System.out.println("09999999999999"+resultObject.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
