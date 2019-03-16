package com.example.kingqi.paykeep;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PayKeep";
    private List<Pay> pays = getDataFromDB();
    private RecyclerView recyclerView;
    private PayAdapter adapter;
    private TextView sum;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        sync();
    }

    private void init(){
//        fitWindows();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Calendar calendar = Calendar.getInstance();
        int m = calendar.get(Calendar.MONTH)+1;
        toolbar.setSubtitle(m+"月账单:");
        setSupportActionBar(toolbar);

        recyclerView= (RecyclerView)findViewById(R.id.recycler_view);
        adapter = new PayAdapter(pays);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                //进入编辑页面：
                Intent intent = new Intent(MainActivity.this,EditPay.class);
                startActivityForResult(intent,1);
            }
        });

        sum = (TextView)findViewById(R.id.sum);
        refreshSum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK ){
                    final Pay pay =(Pay) data.getSerializableExtra("pay");

                    pay.setId(LitePal.count(Pay.class)+1);
                    pay.setUploaded(false);
                    pay.save();

                    pays.add(0,pay);
                    adapter.notifyItemInserted(0);
                    recyclerView.scrollToPosition(0);

                    addPay(pay,1);

                    Snackbar.make(fab,"添加成功",Snackbar.LENGTH_LONG)
                            .setAction("撤销", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deletePay(pay);
                                    pays.remove(0);
                                    adapter.notifyItemRemoved(0);
                                    recyclerView.scrollToPosition(0);
                                    LitePal.delete(Pay.class,pay.getId());
                                }
                            })
                            .show();

                }
        }
    }
    private void sync(){
        List<Pay> pays = LitePal.where("uploaded=?","0").find(Pay.class);
        for (Pay pay:pays){
            addPay(pay,0);
        }
    }
    private void addPay(final Pay pay,final int n){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("id",String.valueOf(pay.getId()))
                        .add("item_spend",pay.getName())
                        .add("money",String.valueOf(pay.getMoney()))
                        .add("year",String.valueOf(pay.getYear()))
                        .add("month",String.valueOf(pay.getMonth()))
                        .add("day",String.valueOf(pay.getDay()))
                        .add("isPri",pay.isPrivate()?"True":"False")
                        .add("fun",String.valueOf(1))
                        .build();
                Request request = new Request.Builder()
                        .url("http://118.25.71.102:10000/add_pay/")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String rd = response.body().string();
                    Log.d(TAG, "get response : "+rd);
                    if (rd.isEmpty()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (n==1)
                                    Toast.makeText(MainActivity.this,"啊咧咧~下次再上传~",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        if (rd.equals("add_ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pay.setUploaded(true);
                                    pay.save();
                                    refreshSum();
                                    if (n==1)
                                        Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(n==1)
                                        Toast.makeText(MainActivity.this,"添加失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (n==1)
                                Toast.makeText(MainActivity.this,"啊咧咧~下次再上传~",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void deletePay(final Pay pay){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("id",String.valueOf(pay.getId()))
                        .add("item_spend",pay.getName())
                        .add("money",String.valueOf(pay.getMoney()))
                        .add("year",String.valueOf(pay.getYear()))
                        .add("month",String.valueOf(pay.getMonth()))
                        .add("day",String.valueOf(pay.getDay()))
                        .add("isPri",pay.isPrivate()?"True":"False")
                        .add("fun",String.valueOf(2))
                        .build();
                Request request = new Request.Builder()
                        .url("http://118.25.71.102:10000/add_pay/")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String rd = response.body().string();
                    Log.d(TAG, "get response : "+rd);
                    if (rd.isEmpty()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        if (rd.equals("delete_ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshSum();
                                    Toast.makeText(MainActivity.this,"撤销成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"撤销失败",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void testDB(){
        List<Pay> pays = LitePal.findAll(Pay.class);
        String t="";
        for (Pay pay:pays){
            t+=pay.toString();
        }
        Log.d(TAG, "testDB: "+t);
    }
    private List<Pay> getDataFromDB(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        List<Pay> pays = LitePal.where("year=?",String.valueOf(year))
                                .where("month=?",String.valueOf(month))
                                .find(Pay.class);
        Collections.reverse(pays);
        return pays;
    }
    protected void fitWindows(){
        Window window = getWindow();//设置系统栏是否适应的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
    private float getSum(){
        float sum =0;
        for (Pay pay:pays){
            sum+=pay.getMoney();
        }
        return sum;
    }
    private void refreshSum(){
        sum.setText("总金额:"+String.valueOf(getSum())+"元");
    }
}
