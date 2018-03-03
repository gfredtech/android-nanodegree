package me.gfred.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import me.gfred.popularmovies1.models.Movie;

/**
 * Created by Gfred on 3/3/2018.
 */

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Movie movie = null;

        Intent intent = getIntent();
        if(intent.hasExtra("movie")) {
            movie = (Movie) intent.getSerializableExtra("movie");
        }

        if (movie != null) {
            populateUI(movie);
        }





    }

    void populateUI(Movie movie) {
        TextView title = findViewById(R.id.original_title_tv);
        TextView overview = findViewById(R.id.overview_tv);
        TextView vote = findViewById(R.id.vote_average_tv);
        TextView releaseDate = findViewById(R.id.release_date_tv);

        title.setText(movie.getOriginalTitle());
        overview.setText(movie.getOverview());
        vote.setText(String.valueOf(movie.getVoteAverage()));
        releaseDate.setText(movie.getReleaseDate());
    }
}
