package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Category;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.util.List;

public class OverviewFragment extends Fragment {
    private static final String TAG = "OverviewFragment";
    private DatabaseHandler db;

    public static OverviewFragment newInstance() {
//        OverviewFragment fragment = ;
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);

        return new OverviewFragment();
    }
    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHandler(this.getActivity());

        String pathDebut = "android.resource://" + getActivity().getPackageName() + "/";

        db.deleteAllCategories();
        db.addCategory(new Category("Courses", Uri.parse(pathDebut + R.drawable.courses).toString()));
        db.addCategory(new Category("Electroménager", Uri.parse(pathDebut + R.drawable.electromenager).toString()));
        db.addCategory(new Category("Gadget", Uri.parse(pathDebut + R.drawable.electromenager).toString()));
        db.addCategory(new Category("Salaire", Uri.parse(pathDebut + R.drawable.salaire).toString()));
        db.addCategory(new Category("Banque", Uri.parse(pathDebut + R.drawable.banque).toString()));

        db.deleteAllTransactions();
        db.addTransaction(new Transaction(-12, 1, true, false, System.currentTimeMillis(), "SuperU"));
        db.addTransaction(new Transaction(-36, 3, false, false, System.currentTimeMillis(), "Batterie pour Galaxy S2"));
        db.addTransaction(new Transaction(-18, 2, true, true, System.currentTimeMillis(), "Cuiseur à riz"));
        db.addTransaction(new Transaction(40, 5, true, false, System.currentTimeMillis(), "Remboursement de la banque"));

        List<Transaction> list = db.getAllTransactions();
        for(Transaction t : list){
            Log.d(TAG, "Id:"+t.getId()+"   Amount="+t.getAmount()+"   Done:"+t.isDone()+"   Repeat:"+t.isRepeat()+"   Date:"+t.getDate()+"  Commentary:"+t.getCommentary()
            + "    Category:" + db.getCategory(t.getCategory()).getName());
        }
        List<Category> listCategories = db.getAllCategories();
        for(Category c: listCategories){
            Log.d(TAG, "Name: "+c.getName()+ "    Path: "+c.getThumbUrl());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        TextView totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        TextView realAmount = (TextView) view.findViewById(R.id.realAmount);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        totalAmount.setText(db.getTotalAmount()+" €");
        realAmount.setText(db.getRealAmount()+" €");
        progressBar.setProgress(100);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
