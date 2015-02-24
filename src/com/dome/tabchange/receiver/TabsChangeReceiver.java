/**
 * 
 */
package com.dome.tabchange.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


/**
 * @author 周皓  获取改变信息广播
 *
 */
public class TabsChangeReceiver extends BroadcastReceiver{

	private int type;
	private ChangeListViewShowListenner listenner;

	/**
	 * 
	 */
	public TabsChangeReceiver(ChangeListViewShowListenner listenner) {
		this.listenner=listenner;
	}
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int temp=intent.getIntExtra("type", -1);
		if(temp<=-1){
			try {
				Toast.makeText(context, "信息类型传输错误！！", Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			setType(temp);
			getListenner().changeListViewShow(getType());
		}
		
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	
	public interface ChangeListViewShowListenner{
		void changeListViewShow(int type);
	}
	
	public ChangeListViewShowListenner getListenner() {
		return listenner;
	}

	
	public void setListenner(ChangeListViewShowListenner listenner) {
		this.listenner = listenner;
	}


}
