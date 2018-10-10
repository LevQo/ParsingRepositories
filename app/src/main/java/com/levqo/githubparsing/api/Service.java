package com.levqo.githubparsing.api;

import com.levqo.githubparsing.model.Item;
import com.levqo.githubparsing.model.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by user on 10.10.2018.
 */

public interface Service {
    @GET("/repositories")
    Call<List<Item>> getItem();
}
