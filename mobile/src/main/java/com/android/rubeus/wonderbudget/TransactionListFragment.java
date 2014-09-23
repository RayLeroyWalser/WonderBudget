package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.rubeus.wonderbudget.CustomAdapter.TransactionLineAdapter;
import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.util.List;

public class TransactionListFragment extends Fragment {
    private ListView listView;

    public static TransactionListFragment newInstance() {
        return new TransactionListFragment();
    }
    public TransactionListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);

        listView = (ListView) view.findViewById(android.R.id.list);

        DatabaseHandler db = new DatabaseHandler(this.getActivity());
        List<Transaction> list = db.getAllTransactions();

        TransactionLineAdapter adapter = new TransactionLineAdapter(getActivity(), list, db);
        listView.setAdapter(adapter);

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
