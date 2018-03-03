package me.gfred.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gfred.popularmovies1.models.Movie;
import me.gfred.popularmovies1.utils.NetworkUtils;
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

        if(isOnline()) {
            makePopularMoviesQuery();
        }

        else {
            Toast.makeText(this,
                    "Please enable your internet connection first and try again",
                    Toast.LENGTH_LONG).show();
               finish();
        }

        while(popularMoviesJson == null) {

        }


        try {
            movies = JsonUtils.parseListMovies(popularMoviesJson);
        } catch (JSONException e) {
            e.printStackTrace();
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

    public class PopularMoviesQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];

            String jsonResults = null;
            try {
                jsonResults = NetworkUtils.getResponseFromHttpUrl(url);
                Log.v("JSON", jsonResults);
                popularMoviesJson = jsonResults;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResults;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null && !s.equals("")) {


            }
        }
    }

    private void makePopularMoviesQuery() {
        URL popularMovies = NetworkUtils.buildPopularMoviesQuery();
        new PopularMoviesQueryTask().execute(popularMovies);

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
