package com.example.listmovieapps.adapter

import android.content.Intent

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.listmovieapps.DetailMovieActivity
import com.example.listmovieapps.R
import com.example.listmovieapps.model.Movie
import kotlinx.android.synthetic.main.movie_list.view.*

class MovieAdapter(val movies : ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies.get(position))
    }

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list, parent, false)
        return MovieViewHolder(view)
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var view : View = itemView
        private var movie : Movie? = null

        override fun onClick(p0: View?) {
            val intent = Intent(view.context,DetailMovieActivity::class.java)
            intent.putExtra("id_movie", movie?.idMovie)
            intent.putExtra("original_title", movie?.originalTitle)
            intent.putExtra("vote_average", movie?.vote)
            intent.putExtra("popularity", movie?.popularity)
            intent.putExtra("original_language", movie?.language)
            intent.putExtra("overview", movie?.overview)
            intent.putExtra("poster_path", movie?.posterPath)
            intent.putExtra("backdrop_path", movie?.backdrop)
            intent.putExtra("release_date", movie?.date)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            view.context.startActivity(intent)
        }

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            this.movie = movie
            val imageUrl = StringBuilder()
            imageUrl.append(view.context.getString(R.string.base_path_poster)).append(movie.posterPath)
            view.mvTitle.setText(movie.originalTitle)
            view.date.setText(movie.date)
            view.rating.setText(movie.vote)
            view.overview.setText(movie.overview)
            Glide.with(view.context).load(imageUrl.toString()).into(view.mvPoster)
        }
    }
}