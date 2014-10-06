package com.android.rubeus.wonderbudget;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
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
    private TextView title;
    private EditText editAmount, editComment;
    private DatabaseHandler db;

    private String commentText, amountText, pathDebut;
    private boolean isCleared;
    private int category, transactionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transaction_action);

        Intent intent = getIntent();
        int type = intent.getIntExtra("typeOfDialog", 2);
        transactionId = (int) intent.getLongExtra("transactionId", 1);

        pathDebut = "android.resource://" + getPackageName() + "/";

        db = new DatabaseHandler(this);

        clearedIcon = (ImageView) findViewById(R.id.clearedAddTransaction);
        categoryIcon = (ImageView) findViewById(R.id.categoryIconAddTransaction);
        spinner = (Spinner) findViewById(R.id.spinner);
        editComment = (EditText) findViewById(R.id.editComment);
        editAmount = (EditText) findViewById(R.id.editAmount);
        title = (TextView) findViewById(R.id.title);
        ok = (ImageView) findViewById(R.id.ok);

        initLook(type);
    }

    private void initLook(int typeOfDialog){

        //Category
        SpinnerAdapter mSpinnerAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                db.getAllCategoriesCursor(true),
                new String[] { db.KEY_NAME },
                new int[] { android.R.id.text1 },
                0);
        spinner.setAdapter(mSpinnerAdapter);

        switch (typeOfDialog){
            case ADD_NEW_TRANSACTIION:
                //Choose title
                title.setText(getResources().getString(R.string.add_new_transaction));

                //Choose the category
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = (int) id;
                        categoryIcon.setImageURI(Uri.parse(db.getCategory((int)id).getThumbUrl()));
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
                        switchClearedStatus();
                    }
                });

                ok(typeOfDialog);

                break;
            case VIEW_TRANSACTION:
                //Disable button OK
                ok.setVisibility(View.GONE);

                //Set title
                title.setText(getResources().getString(R.string.view_transaction));

                //Show the transaction status (cleared/not cleared)
                isCleared = db.getTransaction(transactionId).isDone();
                switchClearedStatus();
                clearedIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchToEditTransaction();
                        isCleared = (!isCleared);
                        switchClearedStatus();
                    }
                });

                //Show category
                category = db.getTransaction(transactionId).getCategory();
                categoryIcon.setImageURI(Uri.parse(db.getCategory(category).getThumbUrl()));

                //Spinner
                spinner.setSelection(category - 1);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position!=category-1)
                            switchToEditTransaction();
                        category = (int) id;
                        categoryIcon.setImageURI(Uri.parse(db.getCategory((int)id).getThumbUrl()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        category = 1;
                    }
                });

                //Amount
                editAmount.setText(db.getTransaction(transactionId).getAmount() + "");
                editAmount.setFocusable(false);
                editAmount.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switchToEditTransaction();
                        return false;
                    }
                });

                //Comment
                editComment.setText(db.getTransaction(transactionId).getCommentary());
                editComment.setFocusable(false);
                editComment.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switchToEditTransaction();
                        return false;
                    }
                });

                ok(typeOfDialog);

                break;
            default:
        }
    }

    private void switchClearedStatus(){
        if(isCleared){
            clearedIcon.setImageURI(Uri.parse(pathDebut + R.drawable.cleared));
        }
        else{
            clearedIcon.setImageURI(Uri.parse(pathDebut + R.drawable.not_cleared));
        }
    }

    private void switchToEditTransaction(){
        ok.setVisibility(View.VISIBLE);
        title.setText(getResources().getString(R.string.edit_transaction));
        editAmount.setFocusable(true);
        editAmount.setFocusableInTouchMode(true);
        editComment.setFocusable(true);
        editComment.setFocusableInTouchMode(true);
    }

    private void ok(final int type){
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
                Transaction t = new Transaction(Double.parseDouble(amountText), category, isCleared, System.currentTimeMillis(), commentText);
                switch (type){
                    case ADD_NEW_TRANSACTIION:
                        db.addTransaction(t);
                        break;
                    case VIEW_TRANSACTION:
                        t.setId(transactionId);
                        db.updateTransaction(t);
                        TransactionActionActivity.this.setResult(RESULT_OK);
                        break;
                }
                finish();
            }
            }
        });
    }
}
