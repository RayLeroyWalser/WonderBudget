package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.Transaction;


public class TransactionActionActivity extends Activity {
    public static final String TAG = "TransactionActionActivity";
    public static final int ADD_NEW_TRANSACTIION = 1;
    public static final int VIEW_TRANSACTION = 2;

    private ImageView clearedIcon, categoryIcon, ok;
    private Spinner spinner;
    private TextView categoryName, comment, title;
    private EditText editAmount, editComment;
    private DatabaseHandler db;

    private String commentText, amountText, pathDebut;
    private boolean isCleared;
    private int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transaction_action);

        Intent intent = getIntent();
        int type = intent.getIntExtra("typeOfDialog", 2);

        pathDebut = "android.resource://" + getPackageName() + "/";

        db = new DatabaseHandler(this);

        clearedIcon = (ImageView) findViewById(R.id.clearedAddTransaction);
        categoryIcon = (ImageView) findViewById(R.id.categoryIconAddTransaction);
        spinner = (Spinner) findViewById(R.id.spinner);
        categoryName = (TextView) findViewById(R.id.categoryName);
        comment = (TextView) findViewById(R.id.comment);
        editComment = (EditText) findViewById(R.id.editComment);
        editAmount = (EditText) findViewById(R.id.editAmount);
        title = (TextView) findViewById(R.id.title);
        ok = (ImageView) findViewById(R.id.ok);

        initLook(type);
    }

    private void initLook(int typeOfDialog){
        switch (typeOfDialog){
            case ADD_NEW_TRANSACTIION:
                categoryName.setVisibility(View.GONE);
                comment.setVisibility(View.GONE);

                //Choose title
                title.setText(getResources().getString(R.string.add_new_transaction));

                //Choose the Category
                SpinnerAdapter mSpinnerAdapter = new SimpleCursorAdapter(this,
                        android.R.layout.simple_list_item_1,
                        db.getAllCategoriesCursor(true),
                        new String[] { db.KEY_NAME },
                        new int[] { android.R.id.text1 },
                        0);
                spinner.setAdapter(mSpinnerAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = (int) id;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        category = 1;
                    }
                });

                //Choose the transaction status (cleared/not cleared)
                clearedIcon.setImageURI(Uri.parse(pathDebut + R.drawable.not_cleared));
                isCleared = false;
                clearedIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isCleared = (!isCleared);
                        if(isCleared){
                            clearedIcon.setImageURI(Uri.parse(pathDebut + R.drawable.cleared));
                        }
                        else{
                            clearedIcon.setImageURI(Uri.parse(pathDebut + R.drawable.not_cleared));
                        }
                    }
                });

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Choose the amount
                        amountText = editAmount.getText().toString().trim();

                        //Choose the comment
                        commentText = editComment.getText().toString().trim();

                        if(amountText.equals("")){
                            Toast.makeText(TransactionActionActivity.this, getResources().getString(R.string.error_edit_amount), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Transaction t = new Transaction(Integer.parseInt(amountText), category, isCleared, System.currentTimeMillis(), commentText);
                            Log.d(TAG, "Id:" + t.getId() + "   Amount=" + t.getAmount() + "   Done:" + t.isDone() + "   Date:" + t.getDate() + "  Commentary:" + t.getCommentary()
                                    + "    Category:" + db.getCategory(t.getCategory()).getName());
                            db.addTransaction(t);
                            finish();
                        }
                    }
                });

                break;
            case VIEW_TRANSACTION:
                break;
            default:
        }
    }
}
