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

public class AccessActivity extends AppCompatActivity {
    Button next;
    int noerrCount= 0,noKey= 0,noTag= 0,errServer= 0,other= 0;
    String attribute="ErrorCode",user,day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        day = intent.getExtras().getString("day");
        user = intent.getExtras().getString("user");

        next=(Button)findViewById(R.id.next1);
        final Spinner spinAttrAccess = (Spinner)findViewById(R.id.spattrAccess);

        ArrayAdapter<String> accessAduptor = new ArrayAdapter<String>(AccessActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.attrChoose ));
        accessAduptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinAttrAccess.setAdapter(accessAduptor  );
        final String text = spinAttrAccess.getSelectedItem().toString();
        System.out.println(text);



        spinAttrAccess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                attribute = adapterView.getItemAtPosition(i).toString();
                System.out.println(attribute);
                new ChartTask().execute("https://zfp1gizcmh.execute-api.us-east-1.amazonaws.com/test/getaccessdata?UserId="+user+"&attr="+attribute+"&LogType=Server%20access%20logs%20"+day);
                noerrCount = noKey = noTag = errServer = other = 0;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent goAccessPie  = new Intent(AccessActivity.this,AccessPie.class);
                goAccessPie.putExtra("noerrCount", noerrCount);
                goAccessPie.putExtra("noKey", noKey);
                goAccessPie.putExtra("noTag", noTag);
                goAccessPie.putExtra("errServer", errServer);
                goAccessPie.putExtra("other", other);
                goAccessPie.putExtra("attribute", attribute);
                startActivity(goAccessPie);


            }

        });



        System.out.println("https://zfp1gizcmh.execute-api.us-east-1.amazonaws.com/test/getaccessdata?UserId="+user+"&attr=ErrorCode&LogType=Server%20access%20logs%20"+day);






    }

    public class ChartTask extends AsyncTask<String,String,String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;


        @Override
        protected String doInBackground(String... strings) {
            BarChart barChart = (BarChart) findViewById(R.id.bargraph);
            ArrayList<BarEntry> barEntries = new ArrayList<>();

            HttpURLConnection connection=null;
            BufferedReader reader=null;
            String errortype= null;


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
                System.out.println(jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    errortype = jsonArray.getString(i);


                    if(attribute.equals("ErrorCode")){

                        if(errortype.equals("-")){
                        noerrCount ++;
                        }
                        if (errortype.equals("NoSuchKey")) {
                        noKey ++;

                        }
                        if (errortype.equals("NoSuchTagSet")) {
                        noTag ++;

                        }
                        if (errortype.equals("ServerSideEncryptionConfigurationNotFoundError")) {
                        errServer ++;

                        }//other=jsonArray.length()-noerrCount-noKey-noTag-errServer;

                    }else if(attribute.equals("HTTPstates")){
                        if(errortype.equals("200")){
                            noerrCount ++;
                        }
                        if (errortype.equals("304")) {
                            noKey ++;

                        }
                        if (errortype.equals("404")) {
                            noTag ++;

                        }
                        if (errortype.equals("400")) {
                            errServer ++;

                        }//other=jsonArray.length()-noerrCount-noKey-noTag-errServer;




                    }else if(attribute.equals("ByteSent")){
                        if(errortype.equals("-")){
                            other ++;
                        }else{
                        int type =Integer.valueOf(errortype);
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
                        }//other=jsonArray.length()-noerrCount-noKey-noTag-errServer;


                    }else if(attribute.equals("UserAgent")){
                        if(errortype.equals("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36")){
                            noerrCount ++;
                        }
                        if (errortype.equals("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0")) {
                            noKey ++;

                        }
                        if (errortype.equals("S3Console/0.4, aws-internal/3")) {
                            noTag ++;

                        }
                        if (errortype.equals("")) {
                            errServer ++;

                        }//other=other+jsonArray.length()-noerrCount-noKey-noTag-errServer;




                    }else if(attribute.equals("Operation")){
                        if(errortype.equals("REST.GET.ANALYTICS")){
                            noerrCount ++;
                        }
                        if (errortype.equals("REST.GET.CORS")) {
                            noKey ++;

                        }
                        if (errortype.equals("REST.GET.ENCRYPTION")) {
                            noTag ++;

                        }
                        if (errortype.equals("REST.GET.LOGGING_STATUS")) {
                            errServer ++;

                        }other=jsonArray.length()-noerrCount-noKey-noTag-errServer;


                    }


                }
                System.out.println(noerrCount);
                System.out.println(noKey);
                System.out.println(noTag);
                System.out.println(errServer);
                System.out.println(other);



//----------chart things----------------------
                barEntries.add(new BarEntry(1,noerrCount,"aaaa"));
                barEntries.add(new BarEntry(2,noKey,"bbbbb"));
                barEntries.add(new BarEntry(3,noTag,"cccccc"));
                barEntries.add(new BarEntry(4,errServer,"ddddddd"));
                barEntries.add(new BarEntry(5,other,"eeeeee"));
                BarDataSet barDataSet =new BarDataSet(barEntries,"Errors");

                BarData data = new BarData(barDataSet);


                barChart.setData(data);

                barChart.setTouchEnabled(true);
                barChart.setDragEnabled(true);
                barChart.setScaleEnabled(true);
                barChart.setFitBars(true);

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

