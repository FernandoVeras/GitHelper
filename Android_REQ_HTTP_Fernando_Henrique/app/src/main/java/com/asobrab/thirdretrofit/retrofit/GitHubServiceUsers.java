package com.asobrab.thirdretrofit.retrofit;

import com.asobrab.thirdretrofit.model.Contributor;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fernando.henrique on 27/10/2016.
 */

public interface GitHubServiceUsers {
    @GET("users/{user}")
    Call<Contributor> repoDetailContributors(
            @Path("user") String user);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
