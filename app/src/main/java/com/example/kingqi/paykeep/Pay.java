package com.example.kingqi.paykeep;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pay extends LitePalSupport implements Serializable {
    private int year,month,day;
    private String name;
    private double money;
    private boolean isPrivate;
    private long id;
    private boolean uploaded;

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public String toString() {
        return year+"/"+month+"/"+day+" "+name+" money:"+money+" isPrivate:"+isPrivate+"\n";
    }

    public static List<Pay> createTestListPays(int num){
        List<Pay> pays = new ArrayList<>();
        for (int i =0;i<num;i++){
            Pay pay = new Pay();
            pay.setYear(2019);
            pay.setMonth(3);
            pay.setDay(13);
            pay.setName("水果");
            pay.setMoney(11);
            pay.setPrivate(false);
            pays.add(pay);
            pay.save();
        }
        return pays;
    }
}
