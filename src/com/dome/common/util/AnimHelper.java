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
 * @author 浩然
 * 功能：图表样式
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
