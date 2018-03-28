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

public class VPCPie extends AppCompatActivity {
String l1="",l2="",l3="",l4="",l5="Other";
    Button web2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpcpie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        web2 = (Button)findViewById(R.id.btnWeb2);

        Intent intent = getIntent();
        int noerrCount = intent.getExtras().getInt("noerrCount");
        int noKey = intent.getExtras().getInt("noKey");
        int noTag = intent.getExtras().getInt("noTag");
        int errServer = intent.getExtras().getInt("errServer");
        int other = intent.getExtras().getInt("other");
        String attribute = intent.getExtras().getString("attribute");

        if(attribute.equals("action")){
            l1="Accept";
            l2= "Reject";


        }else if(attribute.equals("dstport")||attribute.equals("srcport")){
            l1="Well Known";
            l2="Registered";
            l3="Dynamic";


        }else if(attribute.equals("bytes")){
            l1="1-100";
            l2="101-200";
            l3="201-300";
            l4="301-400";

        }else if(attribute.equals("logstatus")){
            l1="OK";
            l2="Not Ok";


        }




        PieChart pieChart2 = (PieChart) findViewById(R.id.piechart2);
        pieChart2.setUsePercentValues(true);

        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
        yvalues.add(new PieEntry(noerrCount, l1));
        yvalues.add(new PieEntry(noKey, l2));
        yvalues.add(new PieEntry(noTag, l3));
        yvalues.add(new PieEntry(errServer, l4));
        yvalues.add(new PieEntry(other, l5));



        PieDataSet dataSet = new PieDataSet(yvalues, "Attribute Type");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);




        PieData data2 = new PieData(dataSet);
        data2.setValueFormatter(new PercentFormatter());
        pieChart2.setData(data2);

        web2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goWeb  = new Intent(VPCPie.this,webView.class);

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
