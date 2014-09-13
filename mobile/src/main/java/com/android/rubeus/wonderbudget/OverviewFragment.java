package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OverviewFragment extends Fragment {
    private static final String TAG = "OverviewFragment";

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);

        return fragment;
    }
    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(this.getActivity());

        db.addCategory(new Category("Courses"));
        db.addCategory(new Category("Gadget"));
        db.addCategory(new Category("APL"));


        db.deleteAllTransactions();
        db.addTransaction(new Transaction(-12, 1, true, false, System.currentTimeMillis(), "SuperU"));
        db.addTransaction(new Transaction(-36, 3, false, false, System.currentTimeMillis(), "Amazon batterie"));
        db.addTransaction(new Transaction(18, 2, true, true, System.currentTimeMillis(), ""));

        List<Transaction> list = db.getAllTransactions();
        for(Transaction t : list){
            Log.d(TAG, "Id:"+t.getId()+"   Amount="+t.getAmount()+"   Done:"+t.isDone()+"   Repeat:"+t.isRepeat()+"   Date:"+t.getDate()+"  Commentary:"+t.getCommentary()
            + "    Category:" + db.getCategory(t.getCategory()).getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
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
