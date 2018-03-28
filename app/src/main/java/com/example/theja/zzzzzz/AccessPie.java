package com.example.theja.zzzzzz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class AccessPie extends AppCompatActivity {
    String t1,t2,t3,t4,t5="Other";;
    Button web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_pie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        web = (Button)findViewById(R.id.btnWeb);

        Intent intent = getIntent();
        int noerrCount = intent.getExtras().getInt("noerrCount");
        int noKey = intent.getExtras().getInt("noKey");
        int noTag = intent.getExtras().getInt("noTag");
        int errServer = intent.getExtras().getInt("errServer");
        int other = intent.getExtras().getInt("other");
        String attribute = intent.getExtras().getString("attribute");

        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        if(attribute.equals("ErrorCode")){
            t1="success";
            t2= "Wrong Key";
            t3="Wrong Tagset";
            t4= "Server Error";

        }else if(attribute.equals("HTTPstates")){
            t1="Ok";
            t2="Not Modified";
             t3="Not Found";
             t4="Bad Request";

        }else if(attribute.equals("ByteSent")){
            t1="1-100";
            t2="101-200";
             t3="201-300";
             t4="301-400";

        }else if(attribute.equals("Operation")){
            t1="Chrome";
             t2="Gecko";
             t3="S3 Console";
              t4="";

        }else if(attribute.equals("UserAgent")){
            t1="Analytics";
            t2="Cors";
            t3="Encryption";
             t4="Logging Status";

        }







        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(noerrCount, t1));
        yvalues.add(new PieEntry(noKey , t2));
        yvalues.add(new PieEntry(noTag, t3));
        yvalues.add(new PieEntry(errServer, t4));
        yvalues.add(new PieEntry(other, t5));

        PieDataSet dataSet = new PieDataSet(yvalues, "Attibute Type");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);




        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);


        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goWeb  = new Intent(AccessPie.this,webView.class);

                startActivity(goWeb);


            }

        });

    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
