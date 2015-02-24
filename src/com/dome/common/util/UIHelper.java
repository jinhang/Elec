package com.dome.common.util;

import java.util.regex.Pattern;

import com.dome.dialog.DialogBulder;
import com.dome.dialog.DialogBulderFinish;
import com.dome.viewer.R;
import com.dome.viewer.TabHostActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.AlteredCharSequence;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ���ܣ�������°�����
 * @author ���
 *
 */

public class UIHelper {

  
    
//     static PopupWindow window =new PopupWindow();
     static boolean flag=false;

	/**
	 * @param loading
	 */
	public static void showNetError(ImageView loading) {
		 AnimHelper.animEnd(loading);
		 loading.setBackgroundResource(R.drawable.error_net);
	}
	
	
	
	public static boolean exit(Context activity){
		
	
		
	
		new DialogBulder(activity)
		.setTitle("��ܰ��ʾ")
		.setMessage("ȷ��Ҫ�뿪��")
		.setButtons("ȷ��", "ȡ��",
				new DialogBulder.OnDialogButtonClickListener() {
					public void onDialogButtonClick(
							Context context, DialogBulder builder,
							Dialog dialog, int dialogId, int which) {
						if (which == BUTTON_LEFT) {
							//�˳�ϵͳ
							int pid = android.os.Process.myPid();

							android.os.Process.killProcess(pid);
							
							//System.exit(0);
						}else{
							dialog.cancel();
						}
					}
				}).create().show();
		return true;
	
	
	}
	
public static boolean exit(Context activity,final Something some){
		
	/*	AlertDialog.Builder builder=new Builder(activity);
		//builder.setTitle("ȷ��Ҫ�뿪��");
		LayoutInflater inflator=LayoutInflater.from(activity);
		View view=inflator.inflate(R.layout.dilog, null);
		builder.setView(view);
		Button confirm=(Button) view.findViewById(R.id.left);
		confirm.setText("ȷ��");
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				some.doFinish();
				
			}
		});
		
		
		Button canccel=(Button) view.findViewById(R.id.right);
		canccel.setText("ȡ��");
		canccel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				;
				
			}
		});
		
		TextView textView=(TextView) view.findViewById(R.id.message);
		textView.setText("ȷ��Ҫ�뿪��");
		
		builder.create().show();
		
		return true;*/
		
	
		new DialogBulderFinish(activity)
		.setTitle("��ܰ��ʾ")
		.setMessage("ȷ��Ҫ�뿪��")
		.setButtons("ȷ��", "ȡ��",new DialogBulderFinish.OnDialogButtonClickListener() {
				
					@Override
					public void onDialogButtonClick(Context context,
							DialogBulderFinish builder, Dialog dialog,
							int dialogId, int which) {
						if (which == BUTTON_LEFT) {
							some.doFinish();
						}else{
							;
						}
						
						
					}
				}).create().show();
		return true;
	
	
	}
	public interface Something{
		void doFinish();
	}

}
