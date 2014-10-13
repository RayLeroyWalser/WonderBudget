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
import com.android.rubeus.wonderbudget.Utility.DateUtility;
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

        //Initiate the DB
        db = new DatabaseHandler(this);
        initDatabase();

        //Add recurring transaction of the month
        createRecurringTransaction();
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

    private void initDatabase(){
        //Store in Shared Preferences if this is the first time the app is launched
        SharedPreferences settings = getSharedPreferences(PREF, 0);

        if(settings.getBoolean("firstLaunch", true)){
            String pathDebut = "android.resource://" + getPackageName() + "/";

            db.deleteAllCategories();
            db.addCategory(new Category("Uncategorized", Uri.parse(pathDebut + R.drawable.uncategorized).toString()));
            db.addCategory(new Category("Courses", Uri.parse(pathDebut + R.drawable.courses).toString()));
            db.addCategory(new Category("Alimentatation", Uri.parse(pathDebut + R.drawable.alimentation).toString()));
            db.addCategory(new Category("Mobilier", Uri.parse(pathDebut + R.drawable.electromenager).toString()));
            db.addCategory(new Category("Gadget", Uri.parse(pathDebut + R.drawable.gadget).toString()));
            db.addCategory(new Category("Frais annexe", Uri.parse(pathDebut + R.drawable.banque).toString()));
            db.addCategory(new Category("Revenu", Uri.parse(pathDebut + R.drawable.salaire).toString()));
            db.addCategory(new Category("Média", Uri.parse(pathDebut + R.drawable.media).toString()));
            db.addCategory(new Category("Logement", Uri.parse(pathDebut + R.drawable.logement).toString()));
            db.addCategory(new Category("Administration", Uri.parse(pathDebut + R.drawable.administration).toString()));
            db.addCategory(new Category("Shopping", Uri.parse(pathDebut + R.drawable.shopping).toString()));
            db.addCategory(new Category("Santé", Uri.parse(pathDebut + R.drawable.sante).toString()));
            db.addCategory(new Category("Animaux", Uri.parse(pathDebut + R.drawable.animaux).toString()));

            db.deleteAllRecurringTransactions();
            db.addRecurringTransaction(new RecurringTransaction(-29.99, 8, DateUtility.dayToMillisecond(1,8,2014), "Abonnement Freebox", 0, 2, 1, 1));
            db.addRecurringTransaction(new RecurringTransaction(-120, 12, DateUtility.dayToMillisecond(1,8,2014), "Hôpital", 0, 10, 6, 1));
            db.addRecurringTransaction(new RecurringTransaction(-2060, 10, DateUtility.dayToMillisecond(1,8,2014), "Impôt", 0, -1, 1, 2));


//            List<Transaction> list = db.getAllTransactions();
//            for(Transaction t : list){
//                Log.d(TAG, "Id:" + t.getId() + "   Amount=" + t.getAmount() + "   Done:" + t.isDone() + "   Date:" + t.getDate() + "  Commentary:" + t.getCommentary()
//                        + "    Category:" + db.getCategory(t.getCategory()).getName());
//            }

//            List<RecurringTransaction> listRecurringTransaction = db.getAllRecurringTransactions();
//            for(RecurringTransaction t : listRecurringTransaction){
//                Log.d(TAG, "Id:" + t.getId() + "   Amount=" + t.getAmount() + "   Done:" + t.isDone() + "   Date:" + t.getDate() + "  Commentary:" + t.getCommentary()
//                        + "    Category:" + db.getCategory(t.getCategory()).getName()
//                        + "     Paid: " + t.getNumberOfPaymentPaid() + "   Total: "+ t.getNumberOfPaymentTotal());
//            }

//            List<Category> listCategories = db.getAllCategories();
//            for(Category c: listCategories){
//                Log.d(TAG, "Name: "+c.getName()+ "    Path: "+c.getThumbUrl());
//            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstLaunch", false);
            editor.commit();
        }


    }

    private void createRecurringTransaction(){
        SharedPreferences settings = getSharedPreferences(PREF, 0);
        int currentMonth = DateUtility.getCurrentMonth();
        int currentYear = DateUtility.getCurrentYear();
        long ms;
        int day, month, year;

        if(currentMonth > settings.getInt("month", currentMonth-1)%12 ||
                currentYear > settings.getInt("year", currentYear-1)){ // we add all the recurring transactions once every month
            Log.v(TAG, "A new month: adding all recurring transactions");
            List<RecurringTransaction> list = db.getAllRecurringTransactions();
            for(RecurringTransaction t : list){
                ms = t.getDate();
                day = DateUtility.getDay(ms);

                if(t.getNumberOfPaymentTotal()-t.getNumberOfPaymentPaid() > 0 || t.getNumberOfPaymentTotal() == -1){ // if there are payments left
                    switch (t.getTypeOfRecurrent()){
                        case RecurringTransaction.MONTH:
                            month = (DateUtility.getMonth(ms)+ (t.getNumberOfPaymentPaid()+1)*t.getDistanceBetweenPayment())  % 12;
                            year = currentYear;
                            if(currentMonth == month){
                                addRecurringTransactionOfTheMonth(day, month, year, t);
                            }
                            break;
                        case RecurringTransaction.YEAR:
                            month = DateUtility.getMonth(ms);
                            year = DateUtility.getYear(ms) + (t.getNumberOfPaymentPaid()+1)*t.getDistanceBetweenPayment();
                            if(currentYear == year && currentMonth == month){
                                addRecurringTransactionOfTheMonth(day, month, year, t);
                            }
                            break;
                    }
                }
                else{
                    db.deleteRecurringTransaction(t);
                }
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("month", currentMonth);
            editor.putInt("year", currentYear);
            editor.commit();
        }
    }

    private void addRecurringTransactionOfTheMonth(int day, int month, int year, RecurringTransaction t){
        Transaction ts = new Transaction(t.getAmount(), t.getCategory(), false, DateUtility.dayToMillisecond(day,month,year), t.getCommentary());
        db.addTransaction(ts);
        Log.d(TAG, "Added :   Id:" + ts.getId() + "   Amount=" + ts.getAmount() + "   Done:" + ts.isDone() + "   Date:" + ts.getDate() + "  Commentary:" + ts.getCommentary()
                + "    Category:" + db.getCategory(ts.getCategory()).getName());

        // Number of payment paid ++
        t.setNumberOfPaymentPaid(t.getNumberOfPaymentPaid()+1);
        db.updateRecurringTransaction(t);
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
