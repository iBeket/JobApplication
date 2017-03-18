package com.example.milos.creitiveblogapp;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
/*
the purpose of this class is to implement all the data that we have
and to put them on the appropriate place on the screen using separate
layout that corespondents to the desirable design
 */

public class BlogListClass  extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> title;
    private final List<String> images;
    private final List<String> description;

    public BlogListClass(Activity context, List<String> titles, List<String> images, List<String> descriptions) {
        super(context, R.layout.list_layout, titles);
        this.context = context;
        this.title = titles;
        this.images = images;
        this.description = descriptions;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listView = inflater.inflate(R.layout.list_layout, null, true);

        TextView txtTile = (TextView) listView.findViewById(R.id.txtTitle);
        ImageView imageView = (ImageView) listView.findViewById(R.id.ImageView);
        TextView txtInfo = (TextView) listView.findViewById(R.id.txtInfo);
        txtTile.setText(title.get(position));
        Picasso.with(context).load(images.get(position)).into(imageView);
        txtInfo.setText(Html.fromHtml(description.get(position)));

        return listView;
    }

}


