package com.udacity.pradeepkumarr.popmovies.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.udacity.pradeepkumarr.popmovies.R;
import com.udacity.pradeepkumarr.popmovies.fragments.MovieDetailActivityFragment;

/**
 * @author pradeepkumarr
 */
public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.

            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", getIntent().getData());

            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_fragment_container, fragment)
                    .commit();

            /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Movie Details!! More features like trailers and reviews added in Project II", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        }

     }


}
