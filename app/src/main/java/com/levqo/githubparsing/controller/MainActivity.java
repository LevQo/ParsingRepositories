package com.levqo.githubparsing.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.levqo.githubparsing.ItemAdapter;
import com.levqo.githubparsing.R;
import com.levqo.githubparsing.api.Client;
import com.levqo.githubparsing.api.Service;
import com.levqo.githubparsing.model.Item;
import com.levqo.githubparsing.model.ItemResponse;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String clientId = "c18ec35dacf8d1e15bcc";
    private String clientSecret = "84a73c4bad797d621fc74f48d15ad1126e1aa17d";
    private String redirectUri = "parsingrepotest://callback";

    private RecyclerView recyclerView;
    TextView disconnected;
    private Item item;
    private SwipeRefreshLayout swipeContainer;

    private LinearLayoutManager layoutManager;
    List<Item> itemList =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com/login/oauth/authorize" + "?client_id=" + clientId
                        + "&scope=repo&redirect_uri=" + redirectUri));
                startActivity(intent);;
            }
        });

        initViews();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
            }
        });
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        loadJSON();
    }

    private void loadJSON(){
        disconnected = (TextView) findViewById(R.id.disconnected);
        try{
            Client client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            retrofit2.Call<List<Item>> call = apiService.getItem();
            call.enqueue(new retrofit2.Callback<List<Item>>() {
                @Override
                public void onResponse(retrofit2.Call<List<Item>> call, Response<List<Item>> response) {
                    itemList = response.body();
                    layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    ItemAdapter recyclerViewAdapter = new ItemAdapter(getApplicationContext(), itemList);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    recyclerView.smoothScrollToPosition(0);
                    swipeContainer.setRefreshing(false);
                    recyclerViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(retrofit2.Call<List<Item>> call, Throwable t) {
                    disconnected.setVisibility(View.VISIBLE);
                    Log.d("111", t.toString());

                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

