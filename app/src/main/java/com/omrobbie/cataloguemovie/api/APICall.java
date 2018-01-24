/*
 * Created by omrobbie.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 10/3/17 9:52 AM.
 */

package com.omrobbie.cataloguemovie.api;

import com.omrobbie.cataloguemovie.mvp.model.detail.DetailModel;
import com.omrobbie.cataloguemovie.mvp.model.search.SearchModel;
import com.omrobbie.cataloguemovie.mvp.model.upcoming.UpcomingModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by omrobbie on 28/09/2017.
 */

public interface APICall {

    @GET("movie/popular?")
    Call<SearchModel> getPopularMovie(@Query("page") int page);

    @GET("search/movie")
    Call<SearchModel> getSearchMovie(@Query("page") int page, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<DetailModel> getDetailMovie(@Path("movie_id") String movie_id);

    @GET("movie/upcoming")
    Call<UpcomingModel> getUpcomingMovie();

}
