package com.android.rubeus.wonderbudget.Utility;

import android.content.Context;
import android.util.JsonWriter;

import com.android.rubeus.wonderbudget.DBHandler.DatabaseHandler;
import com.android.rubeus.wonderbudget.Entity.RecurringTransaction;
import com.android.rubeus.wonderbudget.Entity.Transaction;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by rubeus on 10/28/14.
 */
public class JsonUtility {

    public static void writeJsonStream(Context context, OutputStream out) throws IOException {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");

        writer.beginObject();

        // Write all transactions
        writer.name("transactions");
        writeAllTransactions(writer, context);

        // Write all recurring transactions
        writer.name("recurringTransactions");
        writeAllRecurringTransactions(writer, context);

        writer.endObject();
        writer.close();
    }

    public static void writeAllTransactions(JsonWriter writer, Context context) throws IOException {
        writer.beginArray();
        DatabaseHandler db = DatabaseHandler.getInstance(context);
        List<Transaction> list = db.getAllTransactions();
        for(Transaction t : list){
            writeTransaction(writer, t);
        }
        writer.endArray();
    }

    public static void writeTransaction(JsonWriter writer, Transaction t) throws IOException {
        writer.beginObject();
        writer.name("id").value(t.getId());
        writer.name("amount").value(t.getAmount());
        writer.name("category").value(t.getCategory());
        writer.name("isDone").value(t.isDone());
        writer.name("date").value(t.getDate());
        writer.name("commentary").value(t.getCommentary());
        writer.endObject();
    }

    public static void writeAllRecurringTransactions(JsonWriter writer, Context context) throws IOException {
        writer.beginArray();
        DatabaseHandler db = DatabaseHandler.getInstance(context);
        List<RecurringTransaction> list = db.getAllRecurringTransactions();
        for(RecurringTransaction r : list){
            writeRecurringTransaction(writer, r);
        }
        writer.endArray();
    }

    public static void writeRecurringTransaction(JsonWriter writer, RecurringTransaction r) throws IOException {
        writer.beginObject();
        writer.name("id").value(r.getId());
        writer.name("amount").value(r.getAmount());
        writer.name("category").value(r.getCategory());
        writer.name("date").value(r.getDate());
        writer.name("commentary").value(r.getCommentary());
        writer.name("numberOfPaymentPaid").value(r.getNumberOfPaymentPaid());
        writer.name("numberOfPaymentTotal").value(r.getNumberOfPaymentTotal());
        writer.name("distanceBetweenPayment").value(r.getDistanceBetweenPayment());
        writer.name("typeOfRecurrent").value(r.getTypeOfRecurrent());
        writer.endObject();
    }
}
