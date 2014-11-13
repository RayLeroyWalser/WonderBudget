package com.android.rubeus.wonderbudget.CustomAdapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.Entity.Account;
import com.android.rubeus.wonderbudget.R;

import java.util.List;

/**
 * Created by rubeus on 11/12/14.
 */
public class AccountLineAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Account> list;
    private Context context;

    public AccountLineAdapter(Context context, List<Account> list){
        this.list = list;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Account a = list.get(position);

        if(view == null){
            ViewHolder cache = new ViewHolder();

            if(position == 0){
                view = inflater.inflate(R.layout.account_row_active, null);
            }
            else{
                view = inflater.inflate(R.layout.account_row_inactive, null);
            }

            cache.name = (TextView) view.findViewById(R.id.name);
            cache.thumb = (ImageView) view.findViewById(R.id.thumb);

            view.setTag(cache);
        }

        ViewHolder cache = (ViewHolder) view.getTag();

        cache.name.setText(a.getName());

        if(a.getThumbUrl().equals("")){
            cache.thumb.setImageDrawable(context.getResources().getDrawable(R.drawable.unknown));
        }
        else{
            cache.thumb.setImageURI(Uri.parse(a.getThumbUrl()));
        }

        return view;
    }

    private static class ViewHolder{
        public TextView name;
        public ImageView thumb;
    }
}
