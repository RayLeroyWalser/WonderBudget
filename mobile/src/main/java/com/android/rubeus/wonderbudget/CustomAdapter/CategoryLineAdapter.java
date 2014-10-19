package com.android.rubeus.wonderbudget.CustomAdapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Category;
import com.android.rubeus.wonderbudget.Entity.Transaction;
import com.android.rubeus.wonderbudget.R;
import com.android.rubeus.wonderbudget.Utility.DateUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubeus on 10/19/14.
 */
public class CategoryLineAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Category> list;
    private DatabaseHandler db;
    private ArrayList<Integer> checkedPositions = new ArrayList<Integer>();

    public CategoryLineAdapter(Context context, List<Category> list, DatabaseHandler db){
        this.context = context;
        this.list = list;
        this.db = db;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Category getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void refresh(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Category c = list.get(position);

        if(view == null){
            ViewHolder cache = new ViewHolder();
            view = inflater.inflate(R.layout.category_list_row, null);
            cache.background = (ImageView) view.findViewById(R.id.background);
            cache.icon = (ImageView) view.findViewById(R.id.icon);
            cache.name = (TextView) view.findViewById(R.id.categoryName);
            cache.amount = (TextView) view.findViewById(R.id.categoryAmount);
            cache.lastTransaction = (TextView) view.findViewById(R.id.lastTransaction);

            view.setTag(cache);
        }

        ViewHolder cache = (ViewHolder) view.getTag();

        cache.background.setBackground(context.getResources().getDrawable(R.drawable.oval));
        ((GradientDrawable)cache.background.getBackground()).setColor(context.getResources().getColor(R.color.blue));

        cache.icon.setImageURI(Uri.parse(c.getThumbUrl()));

        cache.name.setText(c.getName());

        double amount = db.getAmountOfCategoryCurrentMonth((int) getItemId(position), DateUtility.getFirstDayOfThisMonth());
        if(amount > 0)
            cache.amount.setText(amount + " " + context.getResources().getString(R.string.euro));

        Transaction t = db.getLastTransactionOfCategory(c.getId());
        if(t != null)
            cache.lastTransaction.setText(DateUtility.getDate(t.getDate(), "EEEE dd MMM yyyy"));

        return view;
    }

    private static class ViewHolder{
        public ImageView background;
        public ImageView icon;
        public TextView name;
        public TextView amount;
        public TextView lastTransaction;
    }
}
