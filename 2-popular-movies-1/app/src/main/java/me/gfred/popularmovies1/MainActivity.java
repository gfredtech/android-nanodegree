package me.gfred.popularmovies1;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.gfred.popularmovies1.models.Movie;

public class MainActivity extends AppCompatActivity {
    List<Movie> movies;
    String popularMoviesJson;

    @BindView(R.id.movie_recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makePopularMoviesQuery();
        // Log.i("JSON", popularMoviesJson)

        try {

                movies = JsonUtils.parseListMovies(popularMoviesJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerAdapter adapter = new RecyclerAdapter(this, movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);




    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String jsonResults = null;
            try {
                jsonResults = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResults;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null && !s.equals("")) {
                    popularMoviesJson = s;

            }
        }
    }

    private void makePopularMoviesQuery() {
        URL popularMovies = NetworkUtils.buildPopularMoviesQuery();
        new MovieQueryTask().execute(popularMovies);
        System.out.println(" function called");
    }
}
