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
