package com.android.rubeus.wonderbudget.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.R;
/**
 * Created by rubeus on 11/2/14.
 */
public class NavigationDrawerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private String[] list;

    public NavigationDrawerAdapter(Context context, String[] list){
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        String name = list[position];

        if(view == null){
            ViewHolder cache = new ViewHolder();
            view = inflater.inflate(R.layout.nav_drawer_row, null);
            cache.name = (TextView) view.findViewById(R.id.navItemName);

            view.setTag(cache);
        }

        ViewHolder cache = (ViewHolder) view.getTag();
        cache.name.setText(name);

        return view;
    }

    private static class ViewHolder{
        public TextView name;
    }
}
