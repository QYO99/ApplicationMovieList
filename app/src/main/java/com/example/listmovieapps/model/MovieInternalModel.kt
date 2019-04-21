package com.example.listmovieapps.model

class MovieInternalModel {
    var idMovie : Int?=null
    var originalTitle : String?=null
    var vote : String?=null
    var popularity : String?=null
    var language : String?=null
    var overview : String?=null
    var posterPath : String?=null
    var backdrop : String?=null
    var date : String?=null

    constructor(idMovie : Int,originalTitle : String,vote : String,popularity : String,language : String,overview : String,posterPath : String,
                backdrop : String, date : String){
        this.idMovie = idMovie
        this.originalTitle= originalTitle
        this.vote = vote
        this.popularity= popularity
        this.language = language
        this.overview = overview
        this.posterPath = posterPath
        this.backdrop = backdrop
        this.date = date
    }
}