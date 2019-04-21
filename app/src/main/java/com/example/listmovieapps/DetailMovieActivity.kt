package com.example.listmovieapps

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail_movie.*
import android.widget.Toast
import com.example.listmovieapps.handler.DataBaseHandler
import com.like.LikeButton
import com.like.OnLikeListener

class DetailMovieActivity : AppCompatActivity() {

    var likeButton:LikeButton?=null
    var idMovie:Int?=null
    var originalTitle:String?=null
    var vote:String?=null
    var popularity:String?=null
    var language:String?=null
    var overview:String?=null
    var posterPath:String?=null
    var backdrop:String?=null
    var date:String?=null
    var idFavorite:Int?=null
    var bundle:Bundle?=null

    private val colId = "id_movie"
    private val colTitle = "original_title"
    private val colVote = "vote_average"
    private val colPopularity = "popularity"
    private val colLanguage = "original_language"
    private val colOverview = "overview"
    private val colPoster = "poster_path"
    private val colBackdrop = "backdrop_path"
    private val colDate = "release_date"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        var db = DataBaseHandler(applicationContext)

        likeButton = findViewById(R.id.favoriteButton)

        bundle = intent.extras
        idMovie = bundle?.getInt("id_movie")
        originalTitle = bundle?.getString("original_title")
        vote = bundle?.getString("vote_average")
        popularity = bundle?.getString("popularity")
        language = bundle?.getString("original_language")
        overview = bundle?.getString("overview")
        posterPath = bundle?.getString("poster_path")
        backdrop = bundle?.getString("backdrop_path")
        date = bundle?.getString("release_date")

        val getIdM = db.getId(idMovie.toString())
        var idMl = 0
        if(getIdM.moveToFirst()){
            do {
                idMl = getIdM.getInt(getIdM.getColumnIndex("much"))
            } while (getIdM.moveToNext())
        }

        if(idMl>0){
            favoriteButton.isLiked = true
        }


        getFavorite()

        val imageUrl1 = StringBuilder()
        imageUrl1.append(applicationContext.getString(R.string.base_path_poster)).append(posterPath)
        val imageUrl2 = StringBuilder()
        imageUrl2.append(applicationContext.getString(R.string.base_path_poster)).append(backdrop)

        setTitle.setText(originalTitle)
        setVote.setText(vote)
        setPopularity.setText(popularity)
        setLanguage.setText(language)
        setOverview.setText(overview)
        setDate.setText(date)
        Glide.with(applicationContext).load(imageUrl1.toString()).into(setImage)
        Glide.with(applicationContext).load(imageUrl2.toString()).into(setBackground)

        likeButton?.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                addFavorite()
            }

            override fun unLiked(likeButton: LikeButton) {
                removeFavorite()
            }
        })


    }

    private fun getFavorite(){

    }

    private fun addFavorite() {
        var db = DataBaseHandler(applicationContext)
        var value = ContentValues()
        value.put(colId,idMovie)
        value.put(colTitle,originalTitle)
        value.put(colVote,vote)
        value.put(colPopularity,popularity)
        value.put(colLanguage,language)
        value.put(colOverview,overview)
        value.put(colPoster,posterPath)
        value.put(colBackdrop,backdrop)
        value.put(colDate,date)
        val success = db.insert(value)
        if (success > 0){
            Toast.makeText(applicationContext,"Add To Your Favorite List",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext,"Error id Movie : "+idMovie+" vote : "+vote,Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFavorite(){
        var db = DataBaseHandler(applicationContext)
        val getId = arrayOf(idMovie.toString())
        val success = db.delete("id_movie=?",getId)
        if (success > 0 ){
            Toast.makeText(applicationContext,"Remove From Your Favorite List",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext,"Error",Toast.LENGTH_SHORT).show()
        }
    }
}
