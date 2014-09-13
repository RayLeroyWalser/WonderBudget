package com.android.rubeus.wonderbudget;

/**
 * Created by rubeus on 9/12/14.
 */
public class Transaction {
    private int id;
    private int amount;
    private String category;
    private boolean isDone;
    private boolean isRepeat;
    private long date;

    public Transaction(){}

    public Transaction(int id, int amount, String category, boolean isDone, boolean isRepeat, long date){
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.isDone = isDone;
        this.isRepeat = isRepeat;
        this.date = date;
    }

    public Transaction(int amount, String category, boolean isDone, boolean isRepeat, long date){
        this.amount = amount;
        this.category = category;
        this.isDone = isDone;
        this.isRepeat = isRepeat;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
