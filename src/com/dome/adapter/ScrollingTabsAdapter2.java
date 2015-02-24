package com.dome.adapter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.dome.viewer.R;

/**
 * 滑动适配器
 * @author jinhang 修改周皓的类
 *
 */
public class ScrollingTabsAdapter2 implements TabAdapter {

    private final Activity activity;

    public ScrollingTabsAdapter2(Activity act) {
        activity = act;
    }

    public View getView(int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        final Button tab = (Button) inflater.inflate(R.layout.tabs, null);
        
        final String[] mTitles = activity.getResources().getStringArray(
                R.array.environment_type_name);
        
        
        Set<String> tab_sets = new HashSet<String>(Arrays.asList(mTitles));
        String[] tabs_new = new String[tab_sets.size()];
        int cnt = 0;
        for (int i = 0; i < mTitles.length; i++) {
            if (tab_sets.contains(mTitles[i])) {
                tabs_new[cnt] = mTitles[i];
                cnt++;
            }
        }
        if (position < tabs_new.length)
            tab.setText(tabs_new[position].toUpperCase());
        return tab;
    }

}
