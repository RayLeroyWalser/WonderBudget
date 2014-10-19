package com.android.rubeus.wonderbudget;



import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.rubeus.wonderbudget.CustomAdapter.RecurringTransactionLineAdapter;
import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.RecurringTransaction;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecurringTransactionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RecurringTransactionListFragment extends Fragment {
    private static final String fragmentName = "Recurring transactions";
    private RecurringTransactionLineAdapter adapter;
    private DatabaseHandler db;
    private NavigationDrawerFragment mNavigationDrawerFragment;

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
        setHasOptionsMenu(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

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

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int number = 0;
            private ArrayList<RecurringTransaction> listToDelete = new ArrayList<RecurringTransaction>();
            private ArrayList<Integer> checkedPosition = adapter.getCheckedPositions();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                if(checked){
                    number++;
                    listToDelete.add(db.getRecurringTransaction((int) id));
                    checkedPosition.add(position);
                }
                else{
                    number--;
                    listToDelete.remove(db.getRecurringTransaction((int) id));
                    checkedPosition.remove(checkedPosition.indexOf(position));
                }
                mode.setTitle(number + " selected");
                adapter.notifyDataSetChanged();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.deleteTransaction:
                        for(int i=0; i<listToDelete.size(); i++){
                            db.deleteRecurringTransaction(listToDelete.get(i));
                        }
                        adapter.refresh(db.getAllRecurringTransactions());
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.transaction_list_context_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.
                checkedPosition.clear();
                adapter.notifyDataSetChanged();
                number=0;
                listToDelete.clear();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            adapter.refresh(db.getAllRecurringTransactions());
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(fragmentName);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.recurring_transaction_list_menu, menu);
            restoreActionBar();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.addTransaction:
                Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
                intent.putExtra("typeOfDialog", TransactionActionActivity.ADD_NEW_RECURRING_TRANSACTION);
                startActivityForResult(intent, TransactionActionActivity.ADD_NEW_RECURRING_TRANSACTION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
