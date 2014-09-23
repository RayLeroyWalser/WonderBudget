package com.android.rubeus.wonderbudget.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.rubeus.wonderbudget.Entity.Category;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubeus on 9/12/14.
 */
public class DatabaseHandler extends SQLiteOpenHelper{
    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "WonderBudget";
    private final static String TABLE_TRANSACTION = "transactions";
    private final static String TABLE_CATEGORY = "categories";

    //Common keys
    private final static String KEY_ID = "id";

    //Transactions
    private final static String KEY_AMOUNT = "amount";
    private final static String KEY_CATEGORY = "category";
    private final static String KEY_IS_DONE = "isDone";
    private final static String KEY_IS_REPEAT = "isRepeat";
    private final static String KEY_DATE = "date";
    private final static String KEY_COMMENTARY = "commentary";

    //Categories
    private final static String KEY_NAME = "name";
    private final static String KEY_THUMB_URL = "thumbUrl";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableCategory = "CREATE TABLE " + TABLE_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_NAME + " TEXT, "
                + KEY_THUMB_URL + " TEXT"
                + ")";

        String createTableTransaction = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_AMOUNT + " INTEGER, "
                + KEY_CATEGORY + " INTEGER, "
                + KEY_IS_DONE + " INTEGER, "
                + KEY_IS_REPEAT + " INTEGER, "
                + KEY_DATE + " INTEGER, "
                + KEY_COMMENTARY + " TEXT, "
                + "FOREIGN KEY (" + KEY_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + ") "
                + ")";

        db.execSQL(createTableCategory);
        db.execSQL(createTableTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    /***********************************************************************************

                                   TRANSACTION

     **********************************************************************************/

    public void addTransaction(Transaction t){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, t.getAmount());
        values.put(KEY_CATEGORY, t.getCategory());
        values.put(KEY_IS_DONE, t.isDone());
        values.put(KEY_IS_REPEAT, t.isRepeat());
        values.put(KEY_DATE, t.getDate());
        values.put(KEY_COMMENTARY, t.getCommentary());

        // Inserting Row
        db.insert(TABLE_TRANSACTION, null, values);
        db.close(); // Closing database connection
    }

    public Transaction getTranstaction(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TRANSACTION, new String[] { KEY_ID,
                        KEY_AMOUNT, KEY_CATEGORY, KEY_IS_DONE, KEY_IS_REPEAT, KEY_DATE , KEY_COMMENTARY}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Transaction transaction = new Transaction(cursor.getInt(0),
                cursor.getInt(1),
                cursor.getInt(2),
                cursor.getInt(3)>0?true:false,
                cursor.getInt(4)>0?true:false,
                cursor.getInt(5),
                cursor.getString(6));

        return transaction;
    }

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction();
                t.setId(cursor.getInt(0));
                t.setAmount(cursor.getInt(1));
                t.setCategory(cursor.getInt(2));
                t.setDone(cursor.getInt(3)>0?true:false);
                t.setRepeat(cursor.getInt(4)>0?true:false);
                t.setDate(cursor.getLong(5));
                t.setCommentary(cursor.getString(6));
                // Adding transaction to list
                transactionList.add(t);
            } while (cursor.moveToNext());
        }

        // return transaction list
        return transactionList;
    }

    public int getTransactionCount(){
        String countQuery = "SELECT  * FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateTransaction(Transaction t){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, t.getAmount());
        values.put(KEY_CATEGORY, t.getCategory());
        values.put(KEY_IS_DONE, t.isDone());
        values.put(KEY_IS_REPEAT, t.isRepeat());
        values.put(KEY_DATE, t.getDate());
        values.put(KEY_COMMENTARY, t.getCommentary());

        // updating row
        return db.update(TABLE_TRANSACTION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(t.getId()) });
    }

    public void deleteTransaction(Transaction t){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTION, KEY_ID + " = ?",
                new String[] { String.valueOf(t.getId()) });
        db.close();
    }

    public void deleteAllTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TRANSACTION, null, null);
    }

    /***********************************************************************************

                                    CATEGORIES

     **********************************************************************************/

    public void addCategory(Category c){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, c.getName());
        values.put(KEY_THUMB_URL, c.getThumbUrl());

        db.insert(TABLE_CATEGORY, null, values);
        db.close();
    }

    public Category getCategory(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORY, new String[] {KEY_ID, KEY_NAME, KEY_THUMB_URL}, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor!=null)
            cursor.moveToFirst();

        Category category = new Category(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

        return category;
    }

    public List<Category> getAllCategories(){
        List<Category> list = new ArrayList<Category>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                Category c = new Category();
                c.setId(cursor.getInt(0));
                c.setName(cursor.getString(1));
                c.setThumbUrl(cursor.getString(2));

                list.add(c);
            }
            while(cursor.moveToNext());
        }
        return list;
    }
}
