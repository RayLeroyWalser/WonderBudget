package com.android.rubeus.wonderbudget;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.rubeus.wonderbudget.CustomAdapter.CategoryLineAdapter;
import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Category;
import com.android.rubeus.wonderbudget.Entity.Transaction;
import com.android.rubeus.wonderbudget.Utility.DateUtility;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private String fragmentName = "Categories";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DatabaseHandler db;

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

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        ArrayList<Double> listAmount = new ArrayList<Double>();
        ArrayList<Long> listDate = new ArrayList<Long>();
        List<Category> listCategory = db.getAllCategories();
        int id;
        Transaction t;

        for(int i=0; i<listCategory.size(); i++){
            id = listCategory.get(i).getId();
            listAmount.add(db.getAmountOfCategoryCurrentMonth(id, DateUtility.getFirstDayOfThisMonth()));
            t = db.getLastTransactionOfCategory(id);
            listDate.add(t!=null ? t.getDate() : 0);
        }
        CategoryLineAdapter adapter = new CategoryLineAdapter(getActivity(), listCategory, db, listAmount, listDate);
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
            restoreActionBar();
        }
    }
}
