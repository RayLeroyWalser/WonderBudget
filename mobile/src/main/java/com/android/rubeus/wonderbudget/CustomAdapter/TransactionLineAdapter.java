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

import java.util.List;

/**
 * Created by rubeus on 9/13/14.
 */
public class TransactionLineAdapter extends BaseAdapter {
    private Context context;
    private List<Transaction> list;
    private LayoutInflater inflater;
    private DatabaseHandler db;

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
        String pathDebut = "android.resource://" + context.getPackageName() + "/";
        if(t.isDone()){
            cache.cleared.setImageURI(Uri.parse(pathDebut + R.drawable.cleared));
        }
        else{
            cache.cleared.setImageURI(Uri.parse(pathDebut + R.drawable.not_cleared));
        }
        cache.categoryIcon.setImageURI(Uri.parse(db.getCategory(t.getCategory()).getThumbUrl()));
        cache.category.setText(db.getCategory(t.getCategory()).getName());
        cache.comment.setText(t.getCommentary());
        int amount = t.getAmount();
        if(amount>=0){
            cache.amount.setTextColor(context.getResources().getColor(R.color.positive_amount));
        }
        else{
            cache.amount.setTextColor(context.getResources().getColor(R.color.negative_amount));
        }
        cache.amount.setText(amount+" €");

        return view;
    }

    private static class CacheView{
        public ImageView cleared;
        public ImageView categoryIcon;
        public TextView category;
        public TextView comment;
        public TextView amount;
    }
}
