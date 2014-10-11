package com.android.rubeus.wonderbudget.Entity;

/**
 * Created by rubeus on 10/7/14.
 */
public class RecurringTransaction extends Transaction {
    public final static int DAY = 1;
    public final static int WEEK = 2;
    public final static int MONTH = 3;
    public final static int YEAR = 4;

    private int numberOfPaymentLeft; // -1 if recurring transaction has no end date
    private int distanceBetweenPayment; //ex: 1 (year), 28 (days)
    private int typeOfRecurrent; // day,week,month or year

    public RecurringTransaction(){}

    public RecurringTransaction(int id, double amount, int category, long date, String commentary, int numberOfPaymentLeft, int distanceBetweenPayment, int typeOfRecurrent){
        super(id,amount,category,true,date,commentary);
        this.numberOfPaymentLeft = numberOfPaymentLeft;
        this.distanceBetweenPayment = distanceBetweenPayment;
        this.typeOfRecurrent = typeOfRecurrent;
    }

    public RecurringTransaction(double amount, int category, long date, String commentary, int numberOfPaymentLeft, int distanceBetweenPayment, int typeOfRecurrent){
        super(amount,category,true,date,commentary);
        this.numberOfPaymentLeft = numberOfPaymentLeft;
        this.distanceBetweenPayment = distanceBetweenPayment;
        this.typeOfRecurrent = typeOfRecurrent;
    }

    public int getNumberOfPaymentLeft() {
        return numberOfPaymentLeft;
    }

    public void setNumberOfPaymentLeft(int numberOfPaymentLeft) {
        this.numberOfPaymentLeft = numberOfPaymentLeft;
    }

    public int getDistanceBetweenPayment() {
        return distanceBetweenPayment;
    }

    public void setDistanceBetweenPayment(int distanceBetweenPayment) {
        this.distanceBetweenPayment = distanceBetweenPayment;
    }

    public int getTypeOfRecurrent() {
        return typeOfRecurrent;
    }

    public void setTypeOfRecurrent(int typeOfRecurrent) {
        this.typeOfRecurrent = typeOfRecurrent;
    }
}
