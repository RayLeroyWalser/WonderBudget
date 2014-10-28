package com.android.rubeus.wonderbudget.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.rubeus.wonderbudget.Entity.Category;
import com.android.rubeus.wonderbudget.Entity.RecurringTransaction;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rubeus on 9/12/14.
 */
public class DatabaseHandler extends SQLiteOpenHelper{
    public final static int DATABASE_VERSION = 1;
    public final static String DATABASE_NAME = "WonderBudget";
    public final static String TABLE_TRANSACTION = "transactions";
    public final static String TABLE_RECURRING_TRANSACTION = "recurringTransactions";
    public final static String TABLE_CATEGORY = "categories";

    //Common keys
    public final static String KEY_ID = "id";
    public static final String _ID = "_id";

    //Transactions
    public final static String KEY_AMOUNT = "amount";
    public final static String KEY_CATEGORY = "category";
    public final static String KEY_IS_DONE = "isDone";
    public final static String KEY_DATE = "date";
    public final static String KEY_COMMENTARY = "commentary";

    //Recurring transactions
    public final static String KEY_NUMBER_PAYMENT_PAID = "numberOfPaymentPaid";
    public final static String KEY_NUMBER_PAYMENT_TOTAL = "numberOfPaymentTotal";
    public final static String KEY_DISTANCE_REPETITION = "distanceBetweenPayement";
    public final static String KEY_TYPE_OF_RECURRENT = "typeOfRecurrent";

    //Categories
    public final static String KEY_NAME = "name";
    public final static String KEY_THUMB_URL = "thumbUrl";

    //Singleton
    private static DatabaseHandler instance;

    private DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context context){
        if(instance ==  null){
            instance = new DatabaseHandler(context);
        }
        return instance;
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
                + KEY_AMOUNT + " REAL, "
                + KEY_CATEGORY + " INTEGER, "
                + KEY_IS_DONE + " INTEGER, "
                + KEY_DATE + " INTEGER, "
                + KEY_COMMENTARY + " TEXT, "
                + "FOREIGN KEY (" + KEY_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + ") "
                + ")";

        String createTableRecurringTransaction = "CREATE TABLE " + TABLE_RECURRING_TRANSACTION + "("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_AMOUNT + " REAL, "
                + KEY_CATEGORY + " INTEGER, "
                + KEY_DATE + " INTEGER, "
                + KEY_COMMENTARY + " TEXT, "
                + KEY_NUMBER_PAYMENT_PAID + " INTEGER, "
                + KEY_NUMBER_PAYMENT_TOTAL + " INTEGER, "
                + KEY_DISTANCE_REPETITION + " INTEGER, "
                + KEY_TYPE_OF_RECURRENT + " INTEGER, "
                + "FOREIGN KEY (" + KEY_CATEGORY + ") REFERENCES " + TABLE_CATEGORY + "(" + KEY_ID + ") "
                + ")";


        db.execSQL(createTableCategory);
        db.execSQL(createTableTransaction);
        db.execSQL(createTableRecurringTransaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECURRING_TRANSACTION);
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
        values.put(KEY_DATE, t.getDate());
        values.put(KEY_COMMENTARY, t.getCommentary());

        // Inserting Row
        db.insert(TABLE_TRANSACTION, null, values);
        db.close(); // Closing database connection
    }

    public Transaction getTransaction(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION + " WHERE " + KEY_ID + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[] { String.valueOf(id) });

//        Cursor cursor = db.query(TABLE_TRANSACTION, new String[] { KEY_ID,
//                        KEY_AMOUNT, KEY_CATEGORY, KEY_IS_DONE, KEY_DATE , KEY_COMMENTARY}, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Transaction transaction = new Transaction(cursor.getInt(0),
                cursor.getDouble(1),
                cursor.getInt(2),
                cursor.getInt(3)>0?true:false,
                cursor.getLong(4),
                cursor.getString(5));

        return transaction;
    }

    public List<Transaction> getAllTransactions(){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION + " ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction();
                t.setId(cursor.getInt(0));
                t.setAmount(cursor.getDouble(1));
                t.setCategory(cursor.getInt(2));
                t.setDone(cursor.getInt(3)>0?true:false);
                t.setDate(cursor.getLong(4));
                t.setCommentary(cursor.getString(5));
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

    public double getTotalAmount(){
        String query = "SELECT ROUND(SUM(" + KEY_AMOUNT + "),2) FROM " + TABLE_TRANSACTION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getDouble(0);
        }
        return 0;
    }

    public double getRealAmount(){
        String query = "SELECT ROUND(SUM(" + KEY_AMOUNT + "),2) FROM " + TABLE_TRANSACTION + " WHERE " + KEY_IS_DONE + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getDouble(0);
        }
        return 0;
    }

    public List<Transaction> getTransactionsOfCategory(int id){
        List<Transaction> transactionList = new ArrayList<Transaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION + " WHERE " + KEY_CATEGORY + " = " + id + " ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction();
                t.setId(cursor.getInt(0));
                t.setAmount(cursor.getDouble(1));
                t.setCategory(cursor.getInt(2));
                t.setDone(cursor.getInt(3)>0?true:false);
                t.setDate(cursor.getLong(4));
                t.setCommentary(cursor.getString(5));
                // Adding transaction to list
                transactionList.add(t);
            } while (cursor.moveToNext());
        }

        // return transaction list
        return transactionList;
    }

    public Transaction getLastTransactionOfCategory(int id){
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTION + " WHERE " + KEY_CATEGORY + " = " + id + " ORDER BY " + KEY_DATE + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            Transaction t = new Transaction();
            t.setId(cursor.getInt(0));
            t.setAmount(cursor.getDouble(1));
            t.setCategory(cursor.getInt(2));
            t.setDone(cursor.getInt(3) > 0 ? true : false);
            t.setDate(cursor.getLong(4));
            t.setCommentary(cursor.getString(5));

            return t;
        }

        return null;
    }

    public double getAmountOfCategoryCurrentMonth(int idCategory, long debutDate){
        ArrayList<Double> listAmount = new ArrayList<Double>();

        String query = "SELECT ROUND(SUM(" + KEY_AMOUNT + "),2) FROM " + TABLE_TRANSACTION + " WHERE "
                + KEY_CATEGORY + " = " + idCategory + " AND "
                + KEY_DATE + " >= " + debutDate;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        return 0;
    }


    /***********************************************************************************

                                RECURRING TRANSACTIONS

     **********************************************************************************/

    public void addRecurringTransaction(RecurringTransaction t){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, t.getAmount());
        values.put(KEY_CATEGORY, t.getCategory());
        values.put(KEY_DATE, t.getDate());
        values.put(KEY_COMMENTARY, t.getCommentary());
        values.put(KEY_NUMBER_PAYMENT_PAID, t.getNumberOfPaymentPaid());
        values.put(KEY_NUMBER_PAYMENT_TOTAL, t.getNumberOfPaymentTotal());
        values.put(KEY_DISTANCE_REPETITION, t.getDistanceBetweenPayment());
        values.put(KEY_TYPE_OF_RECURRENT, t.getTypeOfRecurrent());

        // Inserting Row
        db.insert(TABLE_RECURRING_TRANSACTION, null, values);
        db.close(); // Closing database connection
    }

    public void deleteAllRecurringTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECURRING_TRANSACTION, null, null);
    }

    public List<RecurringTransaction> getAllRecurringTransactions(){
        List<RecurringTransaction> transactionList = new ArrayList<RecurringTransaction>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECURRING_TRANSACTION + " ORDER BY " + KEY_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RecurringTransaction t = new RecurringTransaction();
                t.setId(cursor.getInt(0));
                t.setAmount(cursor.getDouble(1));
                t.setCategory(cursor.getInt(2));
                t.setDate(cursor.getLong(3));
                t.setCommentary(cursor.getString(4));
                t.setNumberOfPaymentPaid(cursor.getInt(5));
                t.setNumberOfPaymentTotal(cursor.getInt(6));
                t.setDistanceBetweenPayment(cursor.getInt(7));
                t.setTypeOfRecurrent(cursor.getInt(8));
                // Adding transaction to list
                transactionList.add(t);
            } while (cursor.moveToNext());
        }

        // return transaction list
        return transactionList;
    }

    public int updateRecurringTransaction(RecurringTransaction t){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, t.getAmount());
        values.put(KEY_CATEGORY, t.getCategory());
        values.put(KEY_DATE, t.getDate());
        values.put(KEY_COMMENTARY, t.getCommentary());
        values.put(KEY_NUMBER_PAYMENT_PAID, t.getNumberOfPaymentPaid());
        values.put(KEY_NUMBER_PAYMENT_TOTAL, t.getNumberOfPaymentTotal());
        values.put(KEY_DISTANCE_REPETITION, t.getDistanceBetweenPayment());
        values.put(KEY_TYPE_OF_RECURRENT, t.getTypeOfRecurrent());

        // updating row
        return db.update(TABLE_RECURRING_TRANSACTION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(t.getId()) });
    }

    public void deleteRecurringTransaction(RecurringTransaction t){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECURRING_TRANSACTION, KEY_ID + " = ?",
                new String[] { String.valueOf(t.getId()) });
        db.close();
    }

    public RecurringTransaction getRecurringTransaction(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_RECURRING_TRANSACTION + " WHERE " + KEY_ID + " =?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(id)});
        if (cursor != null)
            cursor.moveToFirst();

        RecurringTransaction transaction = new RecurringTransaction(cursor.getInt(0),
                cursor.getDouble(1),
                cursor.getInt(2),
                cursor.getLong(3),
                cursor.getString(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getInt(7),
                cursor.getInt(8)
           );

        return transaction;
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

    public Cursor getAllCategoriesCursor(boolean adapter){
        SQLiteDatabase db = this.getWritableDatabase();

        String id = KEY_ID;
        if(adapter){
            id = KEY_ID +" "+_ID;
        }
        String columns = id+", "+KEY_NAME;
        String selectQuery = "SELECT " + columns + " FROM " + TABLE_CATEGORY;
        return db.rawQuery(selectQuery, null);
    }

    public void deleteCategory(Category c){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, KEY_ID + " = ?",
                new String[] { String.valueOf(c.getId()) });
        db.close();
    }

    public void deleteAllCategories(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CATEGORY, null, null);
    }
}
