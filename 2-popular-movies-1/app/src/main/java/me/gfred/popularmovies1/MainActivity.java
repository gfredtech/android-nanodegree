package me.gfred.popularmovies1;

import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import org.json.JSONException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies1.models.Movie;
import me.gfred.popularmovies1.utils.JsonUtils;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.MovieClickListener {
    ArrayList<Movie> movies;
    String popularMoviesJson;

    @BindView(R.id.movie_recyclerview)
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        popularMoviesJson = null;

        Intent intent = getIntent();
        if(intent.hasExtra("movies_data")) {
            popularMoviesJson = intent.getExtras().getString("movies_data");
        }
        System.out.println(popularMoviesJson);

        if(popularMoviesJson != null) {

            try {
                movies = JsonUtils.parseListMovies(popularMoviesJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        RecyclerAdapter adapter = new RecyclerAdapter(this, movies, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onMovieClicked(Movie movie) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);

    }


}
