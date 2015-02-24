package com.dome.viewer.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.dome.adapter.TabAdapter;
import com.dome.common.util.UIHelper;
import com.dome.viewer.R;


/**
 * 
 * @author 张藤原 金航 环保信息页面自定义布局
 *
 */
public class ScrollableTabView extends HorizontalScrollView {
	
	
	int  index=0;
    private int tabNum;//标签数量
    private int currentTab;//当前的标签
    private Activity activity;//所属的activity
    private TabAdapter mAdapter = null;
    private final LinearLayout mContainer;//标签的容器
    private final ArrayList<View> mTabs = new ArrayList<View>();//存放标签

    
    
    public ScrollableTabView(Context context) {
        this(context, null);
    }

    
    public ScrollableTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    
    public ScrollableTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        this.activity = (Activity) context;
        this.setHorizontalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(false);

        //获取所有的标签的名字，在资源文件中
        String[] mTitles = context.getResources().getStringArray(
                R.array.message_type_name);
        tabNum = mTitles.length;

        //设置容器
        mContainer = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.setLayoutParams(params);
        mContainer.setOrientation(LinearLayout.HORIZONTAL);

        //添加容器
        this.addView(mContainer);
    }

    
    public void setAdapter(TabAdapter tabAdapter) {
        this.mAdapter = tabAdapter;
        if (mAdapter != null) {
            initTabs();
        }
    }

    public void setViewPage(int currentTab) {
        this.currentTab = currentTab;
        if (currentTab >= 0) {
            initTabs();
        }
    }

    private void initTabs() {
        mContainer.removeAllViews();
        mTabs.clear();

        if (mAdapter == null) {
            return;
        }
        
        //从adpter中获取view并添加内容，而且也要添加监听
        for (int i = 0; i < tabNum; i++) {
            final int index = i;
            View tab = mAdapter.getView(i);
            mContainer.addView(tab);
            tab.setFocusable(true);
            mTabs.add(tab);

            tab.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    selectTab(index);
                    tabClick(index);
                
                }
            });
            
        }
        
        selectTab(0);
       
        
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            selectTab(currentTab);
        }
    }

    /**
     * 根据传入的参数，将平稳的滑动到指定的位置
     * @param position
     */
    public void selectTab(int position) {
        for (int i = 0, pos = 0; i < mContainer.getChildCount(); i++, pos++) {
            View tab = mContainer.getChildAt(i);
            tab.setSelected(pos == position);
        }
        
        View selectView = mContainer.getChildAt(position);
        final int w = selectView.getMeasuredWidth();
        final int l = selectView.getLeft();
        final int x = l - this.getWidth() / 2 + w / 2;
        smoothScrollTo(x, this.getScrollY());
    }

    
    /**
     * 点击的时候发送广播,通知主界面改变数据显示
     * @param position
     */
    private void tabClick(int position) {
        Intent intent=new Intent("com.dome.tabs.change.receiver");
        intent.putExtra("type",position);
        activity.sendBroadcast(intent);
    }
}