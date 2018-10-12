package com.levqo.githubparsing.api;

import com.levqo.githubparsing.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by user on 10.10.2018.
 */

public interface Service {
    @GET("/repositories")
    Call<List<Item>> getItem(@Query("since") int page);
}
