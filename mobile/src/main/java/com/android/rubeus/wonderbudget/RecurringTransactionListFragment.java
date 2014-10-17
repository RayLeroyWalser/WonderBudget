package com.android.rubeus.wonderbudget;



import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.rubeus.wonderbudget.CustomAdapter.RecurringTransactionLineAdapter;
import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.RecurringTransaction;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecurringTransactionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RecurringTransactionListFragment extends Fragment {
    private RecurringTransactionLineAdapter adapter;
    private DatabaseHandler db;

    public static RecurringTransactionListFragment newInstance() {
        return new RecurringTransactionListFragment();
    }
    public RecurringTransactionListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_recurring_transaction_list, container, false);

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        db = new DatabaseHandler(this.getActivity());
        List<RecurringTransaction> list = db.getAllRecurringTransactions();

        adapter = new RecurringTransactionLineAdapter(getActivity(), list, db);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
                intent.putExtra("typeOfDialog", TransactionActionActivity.EDIT_RECURRING_TRANSACTION);
                intent.putExtra("transactionId", adapter.getItemId(position));
                startActivityForResult(intent, TransactionActionActivity.EDIT_RECURRING_TRANSACTION);
            }
        });

        return view;
    }


}
