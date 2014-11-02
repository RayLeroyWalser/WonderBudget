package com.android.rubeus.wonderbudget.Entity;

/**
 * Created by rubeus on 11/2/14.
 */
public class Account {
    private int id;
    private String name;

    public Account(){}

    public Account(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Account(String name){
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
