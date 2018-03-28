package com.example.theja.zzzzzz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public  EditText username,password ;
    public TextView tve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnHit = (Button)findViewById(R.id.btnlog);
        username = (EditText)findViewById(R.id.et1);
        password = (EditText)findViewById(R.id.et2);
        tve = (TextView)findViewById(R.id.tverr);



        btnHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new JsonTask().execute("https://6w51sqrjib.execute-api.us-east-1.amazonaws.com/test//userdet?UserId=");

            }

        });


    }



public class JsonTask extends AsyncTask<String,String,String> {
    public static final String REQUEST_METHOD = "GET";
    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;

    @Override
    protected String doInBackground(String... strings) {

        HttpURLConnection connection=null;
        BufferedReader reader=null;
        String user = null,send; String pass = null;
        String un="noooooooooooooo------------";
        un = username.getText().toString();
        strings[0] = strings[0] + un;
        //System.out.println(username.getText().toString());
       // System.out.println(strings[0]);


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

            //StringBuffer buffer  = new StringBuffer();
            String line  ="";
            while((line =reader.readLine())!= null){
                stringBuilder.append(line);

            }
            String result=stringBuilder.toString();

            JSONObject json = new JSONObject(result);

            //JSONArray jsonArray = json.getJSONArray("Items");


                //JSONObject jsonObject = json.getJSONObject(0);
                JSONObject jo = json.getJSONObject("Item");
                user = jo.getString("UserId");

                pass = jo.getString("Password");


                if(user.equals(username.getText().toString())){


                    if (pass.equals(password.getText().toString())) {
                        send = "Logging in...";
                                System.out.println(send);
                    }else{
                        send = "Incorrect username or pasword";
                        System.out.println(send);
                    }
                }else{
                    send = "Incorrect username or pasword";
                    System.out.println(send);
                }
            System.out.println(send);
                if(send.equals("Logging in...")){
                     Intent i = new Intent(MainActivity.this,Main2Activity.class);
                     i.putExtra("user", un);
                     startActivity(i);
                }


            return send;






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
        tve.setText(s);


    }
}
}



