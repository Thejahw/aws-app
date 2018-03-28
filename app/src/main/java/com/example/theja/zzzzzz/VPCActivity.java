package com.example.theja.zzzzzz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class VPCActivity extends AppCompatActivity {
    Button next2;
    int noerrCount,errorCount= 0,noKey,noTag,errServer,other;
    String attributeVPC;
    String day,user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpc);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        next2=(Button)findViewById(R.id.next2);

        Intent intent = getIntent();
        day = intent.getExtras().getString("day");
        user = intent.getExtras().getString("user");

        final Spinner spinAttrVPC = (Spinner)findViewById(R.id.spattrVPC);

        ArrayAdapter<String> VPCadupter = new ArrayAdapter<String>(VPCActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.attrChooseVPC ));
        VPCadupter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAttrVPC.setAdapter(VPCadupter  );
        final String text = spinAttrVPC.getSelectedItem().toString();
        System.out.println(text);



        spinAttrVPC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                attributeVPC = adapterView.getItemAtPosition(i).toString();
                System.out.println(attributeVPC);
                new ChartTaskA().execute("https://zfp1gizcmh.execute-api.us-east-1.amazonaws.com/test/getvpcdata?UserId="+user+"&attr="+attributeVPC+"&LogType=VPC%20logs%20"+day);
                noerrCount = noKey = noTag = errServer = other = 0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        System.out.println("https://zfp1gizcmh.execute-api.us-east-1.amazonaws.com/test/getvpcdata?UserId="+user+"&attr="+attributeVPC+"&LogType=VPC%20logs%20"+day);





        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goVPCPie  = new Intent(VPCActivity.this,VPCPie.class);
                goVPCPie.putExtra("noerrCount", noerrCount);
                goVPCPie.putExtra("noKey", noKey);
                goVPCPie.putExtra("noTag", noTag);
                goVPCPie.putExtra("errServer", errServer);
                goVPCPie.putExtra("other", other);
                goVPCPie.putExtra("attribute", attributeVPC);

                startActivity(goVPCPie);


            }

        });



    }

    public class ChartTaskA extends AsyncTask<String,String,String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... strings) {
            BarChart barChartVPC = (BarChart) findViewById(R.id.barVPC);
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            String action= null;
            HttpURLConnection connection=null;
            BufferedReader reader=null;



            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setReadTimeout(CONNECTION_TIMEOUT);
                connection.connect();

                InputStream stream  = connection.getInputStream();
                reader  = new BufferedReader(new InputStreamReader(stream));
                StringBuilder stringBuilder = new StringBuilder();


                String line  ="";
                while((line =reader.readLine())!= null){
                    stringBuilder.append(line);

                }
                String result=stringBuilder.toString();




                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    action = jsonArray.getString(i);




                    if(attributeVPC.equals("action")){

                        if(action.equals("ACCEPT")){
                            noerrCount ++;
                        }else{
                        noKey ++;

                        }


                    }else if(attributeVPC.equals("dstport" )||attributeVPC.equals( "srcport")){
                        System.out.println(action);

                            int type =Integer.valueOf(action);
                            if(type>=0&& type<=1023){
                                noerrCount ++;//Well known
                            }
                            if (type>1023&& type<=49151) {
                                noKey ++;//registered

                            }
                            if (type>49151&& type<=65535) {
                                noTag ++;//Dynamic

                            }






                    }else if(attributeVPC.equals("bytes")){
                        System.out.println(attributeVPC);
                        System.out.println(action);
                        if(action.equals("-")){
                            other ++;
                        }else{
                            int type =Integer.valueOf(action);
                            if(type>=0&& type<=100){
                                noerrCount ++;
                            }
                            if (type>100&& type<=200) {
                                noKey ++;

                            }
                            if (type>200&& type<=300) {
                                noTag ++;

                            }
                            if (type>300&& type<=400) {
                                errServer ++;
                            }
                        }



                    }else if(attributeVPC.equals("logstatus")){
                        if(action.equals("OK")){
                            noerrCount ++;
                        }else{
                            errorCount ++;


                        }other=jsonArray.length()-noerrCount-noKey-noTag-errServer;

                    }

                }






//----------chart things----------------------
                barEntries.add(new BarEntry(1,noerrCount));
                barEntries.add(new BarEntry(2,noKey));
                barEntries.add(new BarEntry(1,noTag));
                barEntries.add(new BarEntry(2,errServer));
                barEntries.add(new BarEntry(1,other));


                BarDataSet barDataSet =new BarDataSet(barEntries,"Errors");

                BarData data = new BarData(barDataSet);
                barChartVPC.setData(data);

                barChartVPC.setTouchEnabled(true);
                barChartVPC.setDragEnabled(true);
                barChartVPC.setScaleEnabled(true);
                barChartVPC.setFitBars(true);



                return null;






            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




            return null;

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
