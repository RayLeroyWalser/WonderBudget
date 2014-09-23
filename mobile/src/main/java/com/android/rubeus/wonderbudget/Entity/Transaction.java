package com.android.rubeus.wonderbudget.Entity;

/**
 * Created by rubeus on 9/12/14.
 */
public class Transaction {
    private int id;
    private int amount;
    private int category;
    private boolean isDone;
    private boolean isRepeat;
    private long date;
    private String commentary;

    public Transaction(){}

    public Transaction(int id, int amount, int category, boolean isDone, boolean isRepeat, long date, String commentary){
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.isDone = isDone;
        this.isRepeat = isRepeat;
        this.date = date;
        this.commentary = commentary;
    }

    public Transaction(int amount, int category, boolean isDone, boolean isRepeat, long date, String commentary){
        this.amount = amount;
        this.category = category;
        this.isDone = isDone;
        this.isRepeat = isRepeat;
        this.date = date;
        this.commentary = commentary;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
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

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }
}
