package com.levqo.githubparsing.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String clientId = "c18ec35dacf8d1e15bcc";
    private String clientSecret = "84a73c4bad797d621fc74f48d15ad1126e1aa17d";
    private String redirectUri = "parsingrepotest://callback";

    private static final String TAG = "MainActivity";

    private final String KEY_RECYCLER_STATE = "recycler_state";

    Parcelable listState;

    private RecyclerView recyclerView;
    TextView disconnected;
    private Item item;
    private SwipeRefreshLayout swipeContainer;
    private ItemAdapter recyclerViewAdapter;

    private String idLastItem;
    private int idPage = 0;

    private boolean loaded = false;

    private ProgressBar progressBar;

    private LinearLayoutManager layoutManager;
    List<Item> itemList = null;


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
                startActivity(intent);
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(layoutManager != null) {
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                    if (lastVisibleItemPosition == recyclerViewAdapter.getItemCount() - 1) {
                        idLastItem = itemList.get(itemList.size() - 1).getId();
                        idPage = Integer.parseInt(idLastItem);
                        pagination();
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        listState = layoutManager.onSaveInstanceState();
        state.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if (state != null)
            listState = state.getParcelable(KEY_RECYCLER_STATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            layoutManager.onRestoreInstanceState(listState);
        }
    }

    private void initViews() {
        disconnected = (TextView) findViewById(R.id.disconnected);
        disconnected.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);

        progressBar = findViewById(R.id.progressBar);
        loadJSON();
    }

    private void pagination() {
        progressBar.setVisibility(View.VISIBLE);
        Client client = new Client();
        Service apiService = Client.getClient().create(Service.class);
        retrofit2.Call<List<Item>> call = apiService.getItem(idPage);
        call.enqueue(new retrofit2.Callback<List<Item>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Item>> call, Response<List<Item>> response) {
                for (Item item : response.body()) {
                    itemList.add(item);
                    recyclerViewAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<List<Item>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void loadJSON() {
        progressBar.setVisibility(View.VISIBLE);
        try {
            Client client = new Client();
            Service apiService = Client.getClient().create(Service.class);
            retrofit2.Call<List<Item>> call = apiService.getItem(0);
            call.enqueue(new retrofit2.Callback<List<Item>>() {
                @Override
                public void onResponse(retrofit2.Call<List<Item>> call, Response<List<Item>> response) {
                    itemList = response.body();
                    layoutManager = new LinearLayoutManager(MainActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    if (!loaded) {
                        recyclerViewAdapter = new ItemAdapter(getApplicationContext(), itemList);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        loaded = true;
                    }
                    recyclerView.smoothScrollToPosition(0);

                    swipeContainer.setRefreshing(false);

                    progressBar.setVisibility(View.INVISIBLE);
                    disconnected.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(retrofit2.Call<List<Item>> call, Throwable t) {

                    disconnected.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    swipeContainer.setRefreshing(false);

                    call.cancel();
                    Log.d(TAG, t.toString());
                }
            });
        } catch (Exception e) {

        }
    }
}

