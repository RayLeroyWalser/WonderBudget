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
        db.deleteAllTransactions();
        db.addTransaction(new Transaction(12, "Courses", true, false, System.currentTimeMillis()));
        db.addTransaction(new Transaction(-36, "Gadget", false, false, System.currentTimeMillis()));
        db.addTransaction(new Transaction(18, "Courses", true, true, System.currentTimeMillis()));

        List<Transaction> list = db.getAllTransactions();
        for(Transaction t : list){
            Log.d(TAG, "Id:"+t.getId()+"   Amount="+t.getAmount()+"   Done:"+t.isDone()+"   Repeat:"+t.isRepeat()+"   Date:"+t.getDate());
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
