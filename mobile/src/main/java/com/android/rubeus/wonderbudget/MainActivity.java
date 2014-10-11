package com.android.rubeus.wonderbudget;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Category;
import com.android.rubeus.wonderbudget.Entity.RecurringTransaction;
import com.android.rubeus.wonderbudget.Entity.Transaction;
import com.android.rubeus.wonderbudget.Utility.FontsOverride;

import java.util.List;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = "MainActivity";
    private static final String PREF = "Preferences";
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Set custom font for the entire application
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Roboto-Thin.ttf");

        //Store in Shared Preferences if this is the first time the app is launched
        SharedPreferences settings = getSharedPreferences(PREF, 0);

        if(settings.getBoolean("firstLaunch", true)){
            db = new DatabaseHandler(this);
            String pathDebut = "android.resource://" + getPackageName() + "/";

            db.deleteAllCategories();
            db.addCategory(new Category("Courses", Uri.parse(pathDebut + R.drawable.courses).toString()));
            db.addCategory(new Category("Electroménager", Uri.parse(pathDebut + R.drawable.electromenager).toString()));
            db.addCategory(new Category("Gadget", Uri.parse(pathDebut + R.drawable.electromenager).toString()));
            db.addCategory(new Category("Banque", Uri.parse(pathDebut + R.drawable.banque).toString()));
            db.addCategory(new Category("Revenu", Uri.parse(pathDebut + R.drawable.salaire).toString()));
            db.addCategory(new Category("Média", Uri.parse(pathDebut + R.drawable.electromenager).toString()));

            db.deleteAllTransactions();
            db.addTransaction(new Transaction(-12, 1, true, System.currentTimeMillis(), "SuperU"));
            db.addTransaction(new Transaction(-36, 3, false, System.currentTimeMillis(), "Batterie pour Galaxy S2"));
            db.addTransaction(new Transaction(-18, 2, true, System.currentTimeMillis(), "Cuiseur à riz"));
            db.addTransaction(new Transaction(40, 5, false, System.currentTimeMillis(), "Remboursement de la banque"));
            db.addTransaction(new Transaction(-18, 3, true, System.currentTimeMillis(), "NAS"));

            db.deleteAllRecurringTransactions();
            db.addRecurringTransaction(new RecurringTransaction(-29.99, 6, System.currentTimeMillis(), "Abonnement Freebox", -1, 1, 1));


            List<Transaction> list = db.getAllTransactions();
            for(Transaction t : list){
                Log.d(TAG, "Id:" + t.getId() + "   Amount=" + t.getAmount() + "   Done:" + t.isDone() + "   Date:" + t.getDate() + "  Commentary:" + t.getCommentary()
                        + "    Category:" + db.getCategory(t.getCategory()).getName());
            }
            List<Category> listCategories = db.getAllCategories();
            for(Category c: listCategories){
                Log.d(TAG, "Name: "+c.getName()+ "    Path: "+c.getThumbUrl());
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstLaunch", false);
            editor.commit();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        switch (position){
            case 0:
                ft.replace(R.id.container, OverviewFragment.newInstance());
                break;
            case 1:
                ft.replace(R.id.container, TransactionListFragment.newInstance());
                break;
            case 2:
                ft.replace(R.id.container, RecurringTransactionListFragment.newInstance());
                break;
            case 3:
                ft.replace(R.id.container, CategoryFragment.newInstance());
                break;
            default:
                ft.replace(R.id.container, PlaceholderFragment.newInstance(position+1));
        }

        ft.commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.overview, menu);
//            restoreActionBar();
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_overview, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
