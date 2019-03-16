package com.example.kingqi.paykeep;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;
import java.util.Date;

public class EditPay extends AppCompatActivity {

    private TextView date ;
    private TextInputEditText item_spend , money;
    private RadioButton yes,no;
    private ImageButton button;
    private Pay pay;
    private static final String TAG = "EditPay";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pay);
        init();
    }
    private void init(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Pay");
        setSupportActionBar(toolbar);

        date=(TextView)findViewById(R.id.date);
        item_spend = (TextInputEditText)findViewById(R.id.item_spend);
        money = (TextInputEditText)findViewById(R.id.money);
        yes = (RadioButton) findViewById(R.id.yes);
        no = (RadioButton)findViewById(R.id.no);
        no.setChecked(true);
        button = (ImageButton)findViewById(R.id.ok);

        pay = new Pay();

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        pay.setYear(year);
        int month = calendar.get(Calendar.MONTH)+1;
        pay.setMonth(month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        pay.setDay(day);
        date.setText(year+"/"+month+"/"+day);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickTime();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item_spend.getText().toString().trim().isEmpty()){
                    Toast.makeText(EditPay.this,"买了啥呀！？",Toast.LENGTH_SHORT).show();
                    return;
                }
                String t = money.getText().toString().trim();
                if (t.isEmpty()){
                    Toast.makeText(EditPay.this,"多少钱呀！？",Toast.LENGTH_SHORT).show();
                    return;
                }
                pay.setName(item_spend.getText().toString());
                pay.setMoney(Double.valueOf(t));
                if (yes.isChecked())
                    pay.setPrivate(true);
                else
                    pay.setPrivate(false);
                Intent intent = new Intent();
                intent.putExtra("pay",pay);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
    private void pickTime(){
        TimePickerView timePickerView = new TimePickerBuilder(EditPay.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date d, View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);
                int year = calendar.get(Calendar.YEAR);
                pay.setYear(year);
                int month = calendar.get(Calendar.MONTH)+1;
                pay.setMonth(month);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                pay.setDay(day);
                date.setText(year+"/"+month+"/"+day);
            }
        }).build();
        timePickerView.show();
    }
}
