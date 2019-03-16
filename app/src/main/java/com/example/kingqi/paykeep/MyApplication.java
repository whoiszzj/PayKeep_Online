package com.example.kingqi.paykeep;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //数据库操作：
        LitePal.initialize(this);
        Connector.getDatabase();
    }
    public static Context getContext(){
        return context;
    }
}
