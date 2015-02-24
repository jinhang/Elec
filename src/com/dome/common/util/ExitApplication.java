package com.dome.common.util;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
/**
 * 
 * @author ��� ����ԭ �˳��� 
 *
 */
public class ExitApplication extends Application {
	
	private List<Activity> activityList = new LinkedList();
	
	private static ExitApplication instance;

	private ExitApplication() {
	}

	// ����ģʽ�л�ȡΨһ��ExitApplicationʵ��
	public static ExitApplication getInstance() {
		if (null == instance) {
			instance = new ExitApplication();
		}
		return instance;
	}

	// ���Activity��������
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// ��������Activity��finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		System.exit(0);
	}
}