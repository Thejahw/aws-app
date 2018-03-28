package com.example.theja.zzzzzz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Set;

public class Main2Activity extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    String val,date,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateView = (TextView) findViewById(R.id.tv);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(year, month+1, day);

        Intent intent = getIntent();
        user = intent.getExtras().getString("user");






        final Spinner spinLogType = (Spinner)findViewById(R.id.spLgtype);
        Button pg2 = (Button)findViewById(R.id.btnpg2);

        ArrayAdapter<String> myAdupter = new ArrayAdapter<String>(Main2Activity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.logTypes ));
        myAdupter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinLogType.setAdapter(myAdupter  );
        final String text = spinLogType.getSelectedItem().toString();
        System.out.println(text);

        spinLogType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                val = adapterView.getItemAtPosition(i).toString();
                System.out.println(val);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        pg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(val);
                if(spinLogType.getSelectedItem().toString().equals("VPC Log")){
                Intent goVpc  = new Intent(Main2Activity.this,VPCActivity.class);
                    goVpc.putExtra("day", date);
                    goVpc.putExtra("user", user);
                    startActivity(goVpc);
                }else if(spinLogType.getSelectedItem().toString().equals("Access Log")){
                    Intent goAccess  = new Intent(Main2Activity.this,AccessActivity.class);
                    goAccess.putExtra("day", date);
                    goAccess.putExtra("user", user);
                    startActivity(goAccess);
                }
            }

        });
    }
   /* @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }*/

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate(arg1, arg2+1, arg3);
                }
            };
    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        date = year+"-"+month+"-"+day;
       // System.out.println(day+"/"+month+"/"+year);
    }
}
