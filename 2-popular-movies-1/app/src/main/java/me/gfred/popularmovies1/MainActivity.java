package me.gfred.popularmovies1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.gfred.popularmovies1.models.Movie;

public class MainActivity extends AppCompatActivity {
    List<Movie> movies;

    @BindView(R.id.movie_recyclerview)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            movies = JsonUtils.parseListMovies("some string");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecyclerAdapter adapter = new RecyclerAdapter(this, movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

    }

}
