package com.android.rubeus.wonderbudget.NavDrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.R;

import java.util.List;

/**
 * Created by rubeus on 11/2/14.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {
    private List<NavDrawerItem> list;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> list){
        this.list = list;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView bank;
        public ImageView icon;
        public int type;

        public ViewHolder(View v, int type) {
            super(v);

            this.type = type;

            switch(type){
                case NavDrawerItem.HEADER:
                    name = (TextView) v.findViewById(R.id.account_name);
                    bank = (TextView) v.findViewById(R.id.account_bank);
                    icon = (ImageView) v.findViewById(R.id.circleView);
                    break;
                case NavDrawerItem.ITEM:
                    name = (TextView) v.findViewById(R.id.navItemName);
                    icon = (ImageView) v.findViewById(R.id.navItemIcon);
                    break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = null;

        switch(viewType){
            case NavDrawerItem.HEADER:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_drawer_header, viewGroup, false);
                break;
            case NavDrawerItem.ITEM:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nav_drawer_row, viewGroup, false);
                break;
        }
        return new ViewHolder(v, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        switch(viewHolder.type){
            case NavDrawerItem.HEADER:
                viewHolder.name.setText(list.get(position).getName());
                viewHolder.bank.setText(list.get(position).getBank());
                String imageUri = list.get(position).getImage();
                if(imageUri.length() != 0){
                    viewHolder.icon.setImageURI(Uri.parse(list.get(position).getImage()));
                } else{
                    viewHolder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_face_white_48dp));
                }
                break;
            case NavDrawerItem.ITEM:
                viewHolder.name.setText(list.get(position).getName());
                viewHolder.icon.setImageDrawable(list.get(position).getIcon());
                break;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }
}
