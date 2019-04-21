package com.example.listmovieapps.model

data class MovieResponse(var page : Int,
                         val results : ArrayList<Movie>,
                         val totalResult : Int,
                         val totalPage : Int)