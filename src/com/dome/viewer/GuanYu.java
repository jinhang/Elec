package com.dome.viewer;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
/**
 * ����:������ ������
 * @author ��
 *
 */
public class GuanYu extends Activity{
    
	private Button back;
	private TextView version;
	private String versionCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guanyu);


		PackageManager pm = getPackageManager();

		PackageInfo pinfo;
		try {
			//�õ�verion��ֵ
			pinfo = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
			versionCode = pinfo.versionName;

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		version=(TextView)findViewById(R.id.version);
		version.setText("�汾   v"+versionCode);
		//���ؼ�����
		back = (Button)findViewById(R.id.btn_back_contract);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				GuanYu.this.finish();

			}
		});
	}


}
