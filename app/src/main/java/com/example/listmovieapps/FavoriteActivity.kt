package com.example.listmovieapps

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.BaseAdapter
import android.widget.ListView
import com.bumptech.glide.Glide
import com.example.listmovieapps.handler.DataBaseHandler
import com.example.listmovieapps.model.MovieInternalModel
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.movie_list.view.*

class FavoriteActivity : AppCompatActivity() {

    var listData = ArrayList<MovieInternalModel>()
    var lView: ListView?=null
    var imm:InputMethodManager?=null

    private val colId = "id_movie"
    private val colTitle = "original_title"
    private val colVote = "vote_average"
    private val colPopularity = "popularity"
    private val colLanguage = "original_language"
    private val colOverview = "overview"
    private val colPoster = "poster_path"
    private val colBackdrop = "backdrop_path"
    private val colDate = "release_date"

    var movieName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        lView = findViewById(R.id.rvMovies)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        btnMain.setOnClickListener {
            openMain()
        }

        search_bar.setOnClickListener { openSearch() }

        searchBg.setOnClickListener{ closeSearch() }

        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    fun loadData(){
        search_bar.setText("Search Movie")
        var dbManager = DataBaseHandler(applicationContext)
        val cursor = dbManager.selectData()

        listData.clear()
        if(cursor.moveToFirst()){
            do {
                val id = cursor.getInt(cursor.getColumnIndex(colId))
                val originalTitle = cursor.getString(cursor.getColumnIndex(colTitle))
                val vote = cursor.getString(cursor.getColumnIndex(colVote))
                val popularity = cursor.getString(cursor.getColumnIndex(colPopularity))
                val language = cursor.getString(cursor.getColumnIndex(colLanguage))
                val overview = cursor.getString(cursor.getColumnIndex(colOverview))
                val posterPath = cursor.getString(cursor.getColumnIndex(colPoster))
                val backdrop = cursor.getString(cursor.getColumnIndex(colBackdrop))
                val date = cursor.getString(cursor.getColumnIndex(colDate))

                listData.add(MovieInternalModel(id,originalTitle, vote, popularity, language, overview, posterPath, backdrop, date))
            } while (cursor.moveToNext())
        }
        var mobilAdapter = MovieAdapter(applicationContext,listData)
        lView?.adapter = mobilAdapter

    }

    inner class MovieAdapter : BaseAdapter{

        private var movieList = ArrayList<MovieInternalModel>()
        private var context:Context?= null

        constructor(context: Context, movieList:ArrayList<MovieInternalModel>):super(){
            this.context = context
            this.movieList = movieList
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var view = layoutInflater.inflate(R.layout.movie_list,null)
            val myData = movieList[position]

            val imageUrl = StringBuilder()
            imageUrl.append(view.context.getString(R.string.base_path_poster)).append(myData.posterPath)
            view.mvTitle.setText(myData.originalTitle)
            view.date.setText(myData.date)
            view.rating.setText(myData.vote)
            view.overview.setText(myData.overview)
            Glide.with(view.context).load(imageUrl.toString()).into(view.mvPoster)

            view.card_movies.setOnClickListener {
                loadMovie(myData)
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return movieList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return movieList.size
        }
    }

    private fun loadMovie(movie: MovieInternalModel){        val intent = Intent(applicationContext,DetailMovieActivity::class.java)
        intent.putExtra(colId, movie?.idMovie)
        intent.putExtra(colTitle, movie?.originalTitle)
        intent.putExtra(colVote, movie?.vote)
        intent.putExtra(colPopularity, movie?.popularity)
        intent.putExtra(colLanguage, movie?.language)
        intent.putExtra(colOverview, movie?.overview)
        intent.putExtra(colPoster, movie?.posterPath)
        intent.putExtra(colBackdrop, movie?.backdrop)
        intent.putExtra(colDate, movie?.date)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun openMain(){
        finish()
    }

    fun openSearch(){
        searchBg.visibility = View.VISIBLE
        searchMain.visibility = View.VISIBLE

        searchText.requestFocus();
        searchText.setOnEditorActionListener(){ v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                movieName = searchText.text.toString()
                if(movieName.equals("")){
                    searchBg.visibility = View.GONE
                    searchMain.visibility = View.GONE
                    searchText.clearFocus()
                    loadData()
                } else {
                    searchBg.visibility = View.GONE
                    searchMain.visibility = View.GONE
                    searchText.clearFocus()
                    search_bar.setText(movieName)
                    imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)

                    var dbManager = DataBaseHandler(applicationContext)
                    val cursor = dbManager.searchData(movieName)

                    listData.clear()
                    if(cursor.moveToFirst()){
                        do {
                            val id = cursor.getInt(cursor.getColumnIndex(colId))
                            val originalTitle = cursor.getString(cursor.getColumnIndex(colTitle))
                            val vote = cursor.getString(cursor.getColumnIndex(colVote))
                            val popularity = cursor.getString(cursor.getColumnIndex(colPopularity))
                            val language = cursor.getString(cursor.getColumnIndex(colLanguage))
                            val overview = cursor.getString(cursor.getColumnIndex(colOverview))
                            val posterPath = cursor.getString(cursor.getColumnIndex(colPoster))
                            val backdrop = cursor.getString(cursor.getColumnIndex(colBackdrop))
                            val date = cursor.getString(cursor.getColumnIndex(colDate))

                            listData.add(MovieInternalModel(id,originalTitle, vote, popularity, language, overview, posterPath, backdrop, date))
                        } while (cursor.moveToNext())
                    }
                    var mobilAdapter = MovieAdapter(applicationContext,listData)
                    lView?.adapter = mobilAdapter
                }
                true
            }
            false
        }
        imm?.showSoftInput(searchText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun closeSearch(){
        searchBg.visibility = View.GONE
        searchMain.visibility = View.GONE
        searchText.clearFocus()
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
    }
}
