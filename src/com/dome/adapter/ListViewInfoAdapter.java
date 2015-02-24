package com.dome.adapter;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dome.common.util.StringUtils;
import com.dome.viewer.R;

/**
 * 
 * @author 周皓 张藤原 listview 适配器
 *
 */
public class ListViewInfoAdapter extends BaseAdapter {

    private final Context                     context;//运行上下文
    private  ArrayList<Map<String,Object>>                  listItems;//数据集合
    private final LayoutInflater              listContainer;//视图容器
    private final int                         itemViewResource;//自定义项视图源
    static class ListItemView{              //自定义控件集合  
        public TextView title;  
        public TextView date; 
    } 

    public ListViewInfoAdapter(Context context, ArrayList<Map<String,Object>> data,int resource) {
        this.context = context;         
        this.listContainer = LayoutInflater.from(context);  //创建视图容器并设置上下文
        this.itemViewResource = resource;
        this.listItems = data;
    }

    public void addNewData(ArrayList<Map<String,Object>> data) {
        listItems.addAll(data);
        this.notifyDataSetChanged();
    }
    
    public void clearData() {

    	listItems.clear();
        this.notifyDataSetChanged();
      
    }
    
    
    public void setNewData(ArrayList<Map<String,Object>> data) {

    	listItems=data;
        this.notifyDataSetChanged();
      
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return 0;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView  listItemView = null;

        if (convertView == null) {
            //获取list_item布局文件的视图
            convertView = listContainer.inflate(this.itemViewResource, null);

            listItemView = new ListItemView();
            //获取控件对象
            listItemView.title = (TextView)convertView.findViewById(R.id.sys_cac_item_title);
            listItemView.date= (TextView)convertView.findViewById(R.id.sys_cac_item_time);
       
            //设置控件集到convertView
             convertView.setTag(listItemView);
        }else {
             listItemView = (ListItemView)convertView.getTag();
        }
        
        Map map = listItems.get(position);
        listItemView.title.setText(((String)map.get("title")).toString());
        listItemView.title.setTag(map);//设置隐藏参数(实体类)
        listItemView.date.setText(((String)map.get("time")).toString());
     
        return convertView;
    }
}
