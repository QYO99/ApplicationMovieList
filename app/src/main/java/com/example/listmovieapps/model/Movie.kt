package com.example.listmovieapps.model

import com.google.gson.annotations.SerializedName

data class Movie(@SerializedName("id") val idMovie : Int?,
                 @SerializedName("original_title") val originalTitle : String?,
                 @SerializedName("vote_average") val vote : String?,
                 @SerializedName("popularity") val popularity : String?,
                 @SerializedName("original_language") val language : String?,
                 @SerializedName("overview") val overview : String?,
                 @SerializedName("poster_path") val posterPath : String?,
                 @SerializedName("backdrop_path") val backdrop : String?,
                 @SerializedName("release_date") val date : String?)
