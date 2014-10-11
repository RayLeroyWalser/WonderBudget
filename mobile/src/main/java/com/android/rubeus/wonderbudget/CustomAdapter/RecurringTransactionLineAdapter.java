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
import com.android.rubeus.wonderbudget.Entity.RecurringTransaction;
import com.android.rubeus.wonderbudget.Entity.Transaction;
import com.android.rubeus.wonderbudget.R;
import com.android.rubeus.wonderbudget.Utility.DateUtility;

import java.util.List;

/**
 * Created by rubeus on 10/7/14.
 */
public class RecurringTransactionLineAdapter extends BaseAdapter {
    private Context context;
    private List<RecurringTransaction> list;
    private LayoutInflater inflater;
    private DatabaseHandler db;
    private String pathDebut;

    public RecurringTransactionLineAdapter(Context context, List<RecurringTransaction> list, DatabaseHandler db){
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
    public RecurringTransaction getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void refresh(List<RecurringTransaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        RecurringTransaction t = list.get(position);

        if(view == null){
            CacheView cache = new CacheView();
            view = inflater.inflate(R.layout.recurring_transaction_list_row, null);
            cache.cleared = (ImageView) view.findViewById(R.id.cleared);
            cache.categoryIcon = (ImageView) view.findViewById(R.id.categoryIcon);
            cache.category = (TextView) view.findViewById(R.id.category);
            cache.comment = (TextView) view.findViewById(R.id.comment);
            cache.amount = (TextView) view.findViewById(R.id.amount);
            cache.date = (TextView) view.findViewById(R.id.date);
            cache.recurrenceDetail = (TextView) view.findViewById(R.id.recurrenceDetail);

            view.setTag(cache);
        }

        CacheView cache = (CacheView) view.getTag();
        pathDebut = "android.resource://" + context.getPackageName() + "/";
        cache.cleared.setImageURI(Uri.parse(pathDebut + R.drawable.cleared));

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
        cache.date.setText(DateUtility.getDate(t.getDate(), "EEEE dd MMM yyyy"));

        String details = "Every " + t.getDistanceBetweenPayment();
        switch (t.getTypeOfRecurrent()){
            case RecurringTransaction.DAY: details += " day(s)"; break;
            case RecurringTransaction.WEEK: details += " week(s)"; break;
            case RecurringTransaction.MONTH: details += " month(s)"; break;
            case RecurringTransaction.YEAR: details += " year(s)"; break;
        }
        details += "\n" + t.getNumberOfPaymentLeft() + " payment(s) left";
        cache.recurrenceDetail.setText(details);

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
        public TextView date;
        public TextView recurrenceDetail;
    }
}
