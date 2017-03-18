package com.example.milos.creitiveblogapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.ExecutionException;

/*
This activity shows the last activity where we are passing information
from the previous activity to display on this one. However, there is a problem
reading the font and this is the error that I am getting when trying to
access this activity Font from origin 'https://www.creitive.com'
has been blocked from loading by Cross-Origin Resource Sharing policy
 */
public class BlogScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        Context context = getApplicationContext();
        SessionClass session = new SessionClass();
        String token = session.getToken(context);

        if(!token.isEmpty()){
            try {
                JSONArray dataObject = new AuthenticationClass(context, "blogPage", getIntent().getExtras().getString("id")).execute().get();
                try {
                    //getting information and putting that information into webview
                    String pageInfoHtml = dataObject.getJSONObject(1).getString("content");
                    WebView webview = new WebView(this);
                    webview.loadData(pageInfoHtml, "text/html", null);
                    setContentView(webview);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }else{
            intent = new Intent(BlogScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_blog_screen);
    }

}
