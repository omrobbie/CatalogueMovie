package com.omrobbie.cataloguemovie.api;

import com.omrobbie.cataloguemovie.mvp.model.search.SearchModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by omrobbie on 28/09/2017.
 */

public interface APICall {

    @GET("movie/popular?")
    Call<SearchModel> getPopularMovie(@Query("page") int page);

}
