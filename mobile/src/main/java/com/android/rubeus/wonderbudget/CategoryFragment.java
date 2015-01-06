package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.rubeus.wonderbudget.CustomAdapter.CategoryLineAdapter;
import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Category;
import com.android.rubeus.wonderbudget.Entity.Transaction;
import com.android.rubeus.wonderbudget.Utility.DateUtility;
import com.android.rubeus.wonderbudget.Utility.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DatabaseHandler db;
    private CategoryLineAdapter adapter;
    private ArrayList<Double> listAmount = new ArrayList<Double>();
    private ArrayList<Long> listDate = new ArrayList<Long>();
    private List<Category> listCategory;

    private OnCategorySelectedListener listener;

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
        db = DatabaseHandler.getInstance(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Indicate that this fragment uses an ActionBar
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Highlight in the navigation drawer
        ((MainActivity)getActivity()).getmNavigationDrawerFragment().getmDrawerListView().setItemChecked(MainActivity.CATEGORY_FRAGMENT, true);

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        refreshCategoryList();

        adapter = new CategoryLineAdapter(getActivity(), listCategory, db, listAmount, listDate);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onCategorySelected((int) adapter.getItemId(position));
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshCategoryList();
        adapter.refresh(listCategory, listAmount, listDate);
    }

    public void restoreActionBar() {
        android.support.v7.app.ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(getString(R.string.title_fragment_category_view));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
        }
    }

    private void refreshCategoryList(){
        listCategory = db.getAllCategories();
        listAmount.clear();
        listDate.clear();
        int id;
        Transaction t;
        int account = PreferencesUtility.getAccount(getActivity());

        for(int i=0; i<listCategory.size(); i++){
            id = listCategory.get(i).getId();
            listAmount.add(db.getAmountOfCategoryCurrentMonth(id, DateUtility.getFirstDayOfThisMonth(), account));
            t = db.getLastTransactionOfCategory(id, account);
            listDate.add(t!=null ? t.getDate() : 0);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnCategorySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCategorySelectedListener");
        }
    }

    public interface OnCategorySelectedListener{
        public void onCategorySelected(int categoryId);
    }
}
