/**
 * 
 */
package com.dome.common.util;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dome.viewer.R;

/**
 * @author ��Ȼ
 * ���ܣ�ͼ����ʽ
 *
 */
public class AnimHelper {
	
	
	 public static void animStart(ImageView imageView){
		AnimationDrawable animationDrawable = (AnimationDrawable)imageView.getBackground();
		animationDrawable.start();
	}
	
	 public static void  animEnd(ImageView imageView){
   		AnimationDrawable animationDrawable = (AnimationDrawable)imageView.getBackground();
   		animationDrawable.stop();
	}

   

}
