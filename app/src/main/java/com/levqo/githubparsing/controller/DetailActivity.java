package com.levqo.githubparsing.controller;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.levqo.githubparsing.R;

public class DetailActivity extends AppCompatActivity {

    TextView detailTitle, detailDescription, detailAuthor, detailLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailTitle = (TextView) findViewById(R.id.detail_title);
        detailDescription = (TextView) findViewById(R.id.description);
        detailAuthor = (TextView) findViewById(R.id.author);
        detailLink = (TextView) findViewById(R.id.link);

        String name = getIntent().getExtras().getString("name");
        String description = getIntent().getExtras().getString("description");
        String link = getIntent().getExtras().getString("html_url");

        detailTitle.setText(name);
        detailDescription.setText(description);
        detailLink.setText(link);
    }

}
