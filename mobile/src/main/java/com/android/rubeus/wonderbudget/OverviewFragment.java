package com.android.rubeus.wonderbudget;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Utility.PreferencesUtility;

public class OverviewFragment extends Fragment {
    private DatabaseHandler db;
    private TextView totalAmount, realAmount;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }
    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = DatabaseHandler.getInstance(this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        //Navagation drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Highlight in the navigation drawer
        ((MainActivity)getActivity()).getmNavigationDrawerFragment().getmDrawerListView().setItemChecked(MainActivity.OVERVIEW_FRAGMENT, true);

        totalAmount = (TextView) view.findViewById(R.id.totalAmount);
        realAmount = (TextView) view.findViewById(R.id.realAmount);
        ImageButton addButton = (ImageButton) view.findViewById(R.id.addButton);
        ((GradientDrawable)addButton.getBackground()).setColor(getResources().getColor(R.color.blue));

        refresh();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTransaction();
            }
        });

        totalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransactionFragment();
            }
        });

        realAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTransactionFragment();
            }
        });

        return view;
    }

    private void openTransactionFragment(){
        Fragment newFragment = new TransactionListFragment();

        FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void createNewTransaction(){
        Intent intent = new Intent(getActivity(), TransactionActionActivity.class);
        intent.putExtra("typeOfDialog", TransactionActionActivity.ADD_NEW_TRANSACTIION);
        startActivityForResult(intent, TransactionActionActivity.ADD_NEW_TRANSACTIION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case TransactionActionActivity.ADD_NEW_TRANSACTIION:
                refresh();
                break;
        }
    }

    private void refresh(){
        //Retrieve account number
        int account = PreferencesUtility.getAccount(getActivity());
        totalAmount.setText(db.getTotalAmount(account)+"");
        realAmount.setText(db.getRealAmount(account)+"");
    }

    public void restoreActionBar() {
        android.support.v7.app.ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_fragment_transaction));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            inflater.inflate(R.menu.transaction_list, menu);
            restoreActionBar();
        }
    }
}
