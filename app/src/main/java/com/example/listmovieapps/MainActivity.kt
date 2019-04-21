package com.example.listmovieapps

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.example.listmovieapps.adapter.MovieAdapter
import com.example.listmovieapps.handler.DataBaseHandler
import com.example.listmovieapps.model.Movie
import com.example.listmovieapps.model.MovieResponse
import com.example.listmovieapps.service.ApiClient
import com.example.listmovieapps.service.ApiInterface
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG : String = MainActivity::class.java.canonicalName

    var page: Int = 1
    val numberList : MutableList<String> = ArrayList()
    /*var pagee = 1*/
    var isLoading = false
    var status = 1
    val limit = 10

    var imm:InputMethodManager?=null

    private lateinit var movies : ArrayList<Movie>
    var movieName:String = ""
    lateinit var adapter:MovieAdapter
    var apiKey:String = ""
    var apiInterface:ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dbManager = DataBaseHandler(applicationContext)
        /*setSupportActionBar(toolbar)*/

        /*rvMovies.layoutManager = GridLayoutManager(applicationContext, 2)*/
        movies = ArrayList()
        adapter = MovieAdapter(movies)

        layoutManager = LinearLayoutManager(this)

        rvMovies.layoutManager = layoutManager

        apiKey = getString(R.string.api_key)

        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        backPage.visibility = View.GONE
        nextPage.visibility = View.GONE

        getPopularMovies(apiInterface, apiKey, page)

        search_bar.setOnClickListener { openSearch() }

        searchBg.setOnClickListener{ closeSearch() }

        rvMovies.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if(dy>0){
                    var visibleItemCount:Int = layoutManager.childCount
                    var pastvisibleItem:Int = layoutManager.findFirstVisibleItemPosition()
                    val total:Int = layoutManager.itemCount
                    if(!isLoading){
                        if((visibleItemCount+pastvisibleItem)<= total){
                            backPage.visibility = View.GONE
                            nextPage.visibility = View.GONE
                        }
                        if((visibleItemCount+pastvisibleItem)>= total){
                            if(page==1){
                                backPage.visibility = View.GONE
                                nextPage.visibility = View.VISIBLE
                            } else {
                                backPage.visibility = View.VISIBLE
                                nextPage.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }




        })

        backPage.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        nextPage.setOnClickListener {
            page++
            if(status==1){ getPopularMovies(apiInterface, apiKey, page) }
            else if(status == 2) { getSearchMovie(apiInterface, apiKey, page, movieName)}
        }

        backPage.setOnClickListener {
            page--
            if(status==1){ getPopularMovies(apiInterface, apiKey, page) }
            else if(status == 2) { getSearchMovie(apiInterface, apiKey, page, movieName)}
        }

        btnFav.setOnClickListener {
            openFav()
        }

    }

    fun getPopularMovies(apiInterface: ApiInterface, apiKey : String, page :Int) {
        isLoading = true
        status = 1
        progressBar.visibility = View.VISIBLE
        val call : Call<MovieResponse> = apiInterface.getPopularMovie(apiKey,page)
        Handler().postDelayed({
            call.enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                    Log.d("$TAG", "Gagal Fetch Popular Movie")
                }

                override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                    movies = response!!.body()!!.results
                    Log.d("$TAG", "Movie size ${movies.size}")
                    rvMovies.adapter = MovieAdapter(movies)
                }

            })
            isLoading = false
            progressBar.visibility = View.GONE
            backPage.visibility = View.GONE
            nextPage.visibility = View.GONE
        }, 4000)
    }

    fun getSearchMovie(apiInterface: ApiInterface, apiKey : String, page :Int, keyword : String) {
        isLoading = true
        search_bar.setText(movieName)
        progressBar.visibility = View.VISIBLE
        val call : Call<MovieResponse> = apiInterface.getMovieSearch(apiKey,page,keyword)
        Handler().postDelayed({
            call.enqueue(object : Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                    Log.d("$TAG", "Failed Fetch Movie")
                }

                override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                    movies = response!!.body()!!.results
                    Log.d("$TAG", "Movie size ${movies.size}")
                    rvMovies.adapter = MovieAdapter(movies)
                }

            })
            isLoading = false
            progressBar.visibility = View.GONE
            backPage.visibility = View.GONE
            nextPage.visibility = View.GONE
        }, 4000)
    }

    fun openSearch(){
        searchBg.visibility = View.VISIBLE
        searchMain.visibility = View.VISIBLE
        status = 2

        searchText.requestFocus();
        searchText.setOnEditorActionListener(){ v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                movieName = searchText.text.toString()
                if(movieName.equals("")){
                    searchBg.visibility = View.GONE
                    searchMain.visibility = View.GONE
                    searchText.clearFocus()
                    val pagee = 1
                    search_bar.setText("Search Movie")
                    getPopularMovies(apiInterface, apiKey, pagee)
                } else {
                    val pagee = 1
                    searchBg.visibility = View.GONE
                    searchMain.visibility = View.GONE
                    searchText.clearFocus()
                    imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
                    getSearchMovie(apiInterface, apiKey, pagee, movieName)
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

    fun openFav(){
        val intent = Intent(applicationContext,FavoriteActivity::class.java)
        startActivity(intent)
    }

    /*fun getNowPlaying(apiInterface: ApiInterface, apiKey : String) {
        val call : Call<MovieResponse> = apiInterface.getPopularMovie(apiKey)
        call.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>?, t: Throwable?) {
                Log.d("$TAG", "Gagal Fetch Popular Movie")
            }

            override fun onResponse(call: Call<MovieResponse>?, response: Response<MovieResponse>?) {
                movies = response!!.body()!!.results
                Log.d("$TAG", "Movie size ${movies.size}")
                rvMovies.adapter = MovieAdapter(movies)
            }

        })
    }*/

    /*fun getLatestMovie(apiInterface: ApiInterface, apiKey : String) : Movie? {
        var movie : Movie? = null
        val call : Call<Movie> = apiInterface.getMovieLatest(apiKey)
        call.enqueue(object : Callback<Movie> {
            override fun onFailure(call: Call<Movie>?, t: Throwable?) {
                Log.d("$TAG", "Gagal Fetch Popular Movie")
            }

            override fun onResponse(call: Call<Movie>?, response: Response<Movie>?) {
                if (response != null) {
                    var originalTitle : String? = response.body()?.originalTitle
                    var posterPath : String? = response.body()?.posterPath

                    collapseToolbar.title = originalTitle
                    if (posterPath == null) {
                        collapseImage.setImageResource(R.drawable.icon_no_image)
                    } else {
                        val imageUrl = StringBuilder()
                        imageUrl.append(getString(R.string.base_path_poster)).append(posterPath)
                        Glide.with(applicationContext).load(imageUrl.toString()).into(collapseImage)
                    }
                }
            }

        })

        return movie
    }*/


}
