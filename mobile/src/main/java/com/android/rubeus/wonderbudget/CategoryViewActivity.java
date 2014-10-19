package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.rubeus.wonderbudget.CustomAdapter.TransactionLineAdapter;
import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.util.ArrayList;
import java.util.List;


public class CategoryViewActivity extends Activity {
    TransactionLineAdapter adapter;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        db = new DatabaseHandler(this);

        Intent intent = getIntent();
        int categoryId = (int) intent.getLongExtra("categoryId", 1);

        setTitle(db.getCategory(categoryId).getName());

        ListView listView = (ListView) findViewById(android.R.id.list);

        List<Transaction> list = db.getTransactionsOfCategory((int) categoryId);

        adapter = new TransactionLineAdapter(this, list, db);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryViewActivity.this, TransactionActionActivity.class);
                intent.putExtra("typeOfDialog", TransactionActionActivity.VIEW_TRANSACTION);
                intent.putExtra("transactionId", adapter.getItemId(position));
                startActivityForResult(intent, TransactionActionActivity.VIEW_TRANSACTION);
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int number = 0;
            private ArrayList<Transaction> listToDelete = new ArrayList<Transaction>();
            private ArrayList<Integer> checkedPosition = adapter.getCheckedPositions();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
                if(checked){
                    number++;
                    listToDelete.add(db.getTransaction((int) id));
                    checkedPosition.add(position);
                }
                else{
                    number--;
                    listToDelete.remove(db.getTransaction((int) id));
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
                            db.deleteTransaction(listToDelete.get(i));
                        }
                        adapter.refresh(db.getAllTransactions());
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == this.RESULT_OK){
            switch (requestCode){
                case TransactionActionActivity.VIEW_TRANSACTION:
                    adapter.refresh(db.getAllTransactions());
                    break;
            }

        }
    }
}
