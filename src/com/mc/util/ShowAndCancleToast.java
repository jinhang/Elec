package com.mc.util;

import android.content.Context;
import android.widget.Toast;
/*
 * 马超  设置功能类
 */
public class ShowAndCancleToast {
	private Toast mToast;

	public void showToast(String text,Context context) {
		if (mToast == null) {
			mToast = Toast.makeText(context, text,
					Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
	}

	public void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

}
