package com.android.rubeus.wonderbudget.CustomAdapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Transaction;
import com.android.rubeus.wonderbudget.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubeus on 9/13/14.
 */
public class TransactionLineAdapter extends BaseAdapter {
    private Context context;
    private List<Transaction> list;
    private LayoutInflater inflater;
    private DatabaseHandler db;
    private String pathDebut;
    private ArrayList<Integer> checkedPositions = new ArrayList<Integer>();

    public TransactionLineAdapter(Context context, List<Transaction> list, DatabaseHandler db){
        this.context = context;
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.db = db;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Transaction getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void refresh(List<Transaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        Transaction t = list.get(position);

        if(view == null){
            CacheView cache = new CacheView();
            view = inflater.inflate(R.layout.transaction_list_row, null);
            cache.cleared = (ImageView) view.findViewById(R.id.cleared);
            cache.categoryIcon = (ImageView) view.findViewById(R.id.categoryIcon);
            cache.category = (TextView) view.findViewById(R.id.category);
            cache.comment = (TextView) view.findViewById(R.id.comment);
            cache.amount = (TextView) view.findViewById(R.id.amount);

            view.setTag(cache);
        }

        CacheView cache = (CacheView) view.getTag();
        pathDebut = "android.resource://" + context.getPackageName() + "/";
        updateClearedIcon(cache.cleared,t);
        cache.cleared.setTag(position);
        cache.cleared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction t = getItem((Integer) v.getTag());
                t.setDone(!t.isDone());
                db.updateTransaction(t);
                updateClearedIcon((ImageView)v,t);
            }
        });

        cache.categoryIcon.setImageURI(Uri.parse(db.getCategory(t.getCategory()).getThumbUrl()));
        cache.category.setText(db.getCategory(t.getCategory()).getName());
        cache.comment.setText(t.getCommentary());
        double amount = t.getAmount();
        if(amount>=0){
            cache.amount.setTextColor(context.getResources().getColor(R.color.positive_amount));
        }
        else{
            cache.amount.setTextColor(context.getResources().getColor(R.color.negative_amount));
        }
        cache.amount.setText(amount + " €");

        if(checkedPositions.contains(position)){
            view.setBackgroundColor(context.getResources().getColor(R.color.pale_blue));
        }
        else{
            view.setBackgroundColor(context.getResources().getColor(R.color.app_background));
        }

        return view;
    }

    private void updateClearedIcon(ImageView v, Transaction t){
        if(t.isDone()){
            v.setImageURI(Uri.parse(pathDebut + R.drawable.cleared));
        }
        else{
            v.setImageURI(Uri.parse(pathDebut + R.drawable.not_cleared));
        }
    }

    private static class CacheView{
        public ImageView cleared;
        public ImageView categoryIcon;
        public TextView category;
        public TextView comment;
        public TextView amount;
    }
}
