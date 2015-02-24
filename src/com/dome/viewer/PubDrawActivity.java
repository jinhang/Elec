/**
 * 
 */
package com.dome.viewer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.dome.chartdemo.SalesBarChart;
import com.dome.chartdemo.SalesStackedBarChartDecom;
import com.dome.chartdemo.SalesStackedBarChartMonthColors;

/**
 * ªÊÕºΩÁ√Ê
 * @author ÷‹©
 *
 */
public class PubDrawActivity extends Activity{
	
	
	public static final int rows=9;
	public static final int columns=13;
	//≤‚ ‘
		@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SalesStackedBarChartMonthColors c=new SalesStackedBarChartMonthColors();
	 
		List<double[]> list=produceArray();
	
		startActivity(c.execute(PubDrawActivity.this,list,81,new String[]{"1","2","3"}));
	}
		
		
		public static List<double[]> produceArray(){
			
			List<double[]> ds=new ArrayList<double[]>();
			
	        for (int i = 0; i < rows; i++) {
	        	double[] d=new double[columns];
	        	for (int j = 0; j < columns; j++) {
					d[j]=1d;
				}
	        	ds.add(d);
	        }
		  return ds;	
		}
}
