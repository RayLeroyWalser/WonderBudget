package com.android.rubeus.wonderbudget;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.android.rubeus.wonderbudget.CustomAdapter.TransactionLineAdapter;
import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.util.List;

public class CategoryFragment extends Fragment {
    private String fragmentName = "Categories:";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private ListView listView;
    private DatabaseHandler db;
    private TransactionLineAdapter adapter;
    private int categoryId;

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }
    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Retrieve the fragment of the Navigation Drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        //Initiate the database handler
        db = new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Indicate that this fragment uses an ActionBar
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //Retrieve the list view
        final ListView listView = (ListView) view.findViewById(android.R.id.list);

        //Populate the dropdown menu with the categories' names
        SpinnerAdapter mSpinnerAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1,
                db.getAllCategoriesCursor(true),
                new String[] { db.KEY_NAME },
                new int[] { android.R.id.text1 },
                0);

        //Add a listener for each item in the list of the dropdown menu
        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {

            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                categoryId = (int)itemId;
                List<Transaction> transactions = db.getTransactionsOfCategory(categoryId);
                adapter = new TransactionLineAdapter(getActivity(), transactions, db);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
                        intent.putExtra("typeOfDialog", TransactionActionActivity.VIEW_TRANSACTION);
                        intent.putExtra("transactionId", adapter.getItemId(position));
                        startActivityForResult(intent, TransactionActionActivity.VIEW_TRANSACTION);
                    }
                });
                return true;
            }
        };

        /** Setting dropdown items and item navigation listener for the actionbar */
        getActivity().getActionBar().setListNavigationCallbacks(mSpinnerAdapter, navigationListener);

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

    public void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setTitle(fragmentName);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TransactionActionActivity.VIEW_TRANSACTION:
                if(resultCode == getActivity().RESULT_OK){
                    adapter.refresh(db.getTransactionsOfCategory(categoryId));
                }
                break;
        }
    }
}