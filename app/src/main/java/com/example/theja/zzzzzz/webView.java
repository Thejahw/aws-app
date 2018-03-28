package com.example.theja.zzzzzz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView wb = (WebView)findViewById(R.id.webHP);
        wb.setWebViewClient(new WebViewClient());
        wb.loadUrl("http://awsloganalysis.s3-website-us-east-1.amazonaws.com");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
