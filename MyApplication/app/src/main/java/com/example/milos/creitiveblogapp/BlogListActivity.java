package com.example.milos.creitiveblogapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutionException;

/*
Second activity where we display the main content of the app
 */
public class BlogListActivity extends Activity {

    ListView list;

    List<String> blogPageIds = new ArrayList<>();
    List<String> blogPageTitles = new ArrayList<>();
    List<String> blogPageImageUrls = new ArrayList<>();
    List<String> blogPageDescriptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context context = getApplicationContext();
        SessionClass session = new SessionClass();
        String token = session.getToken(context);
        //checks if we have token and if we do call the url
        if (!token.isEmpty()) {
            try {
                JSONArray dataObject = new AuthenticationClass(context, "blogList", "").execute().get();

                try {
                    //converting from jasonArray into list
                    JSONArray blogDataJsonArray = dataObject.getJSONArray(1);
                    for (int i = 0; i < blogDataJsonArray.length(); i++) {
                        JSONObject blogDataJsonObject = blogDataJsonArray.getJSONObject(i);
                        blogPageIds.add(blogDataJsonObject.getString("id"));
                        blogPageTitles.add(blogDataJsonObject.getString("title"));
                        blogPageImageUrls.add(blogDataJsonObject.getString("image_url"));
                        blogPageDescriptions.add(blogDataJsonObject.getString("description"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent(BlogListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_blog_list);

        BlogListClass adapter = new BlogListClass(this, blogPageTitles, blogPageImageUrls, blogPageDescriptions);
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(BlogListActivity.this, BlogScreenActivity.class);
                intent.putExtra("id", blogPageIds.get(position));
                startActivity(intent);
                finish();
            }
        });
    }
}


