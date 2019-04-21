package com.example.listmovieapps.service

import com.example.listmovieapps.model.Movie
import com.example.listmovieapps.model.MovieResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("movie/latest")
    fun getMovieLatest(
        @Query("api_key") apiKey : String) : Call<Movie>
    @GET("movie/popular")
    fun getPopularMovie(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ) : Call<MovieResponse>
    @GET("search/movie")
    fun getMovieSearch(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("query") keyword: String
    ) : Call<MovieResponse>
    /*@GET("movie/now_playing")
    fun getNowPlaying(@Query("api_key") apiKey: String) : Call<MovieResponse>*/
}