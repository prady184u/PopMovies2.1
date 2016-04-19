package com.udacity.pradeepkumarr.popmovies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.udacity.pradeepkumarr.popmovies.R;
import com.udacity.pradeepkumarr.popmovies.domain.Movie;
import com.udacity.pradeepkumarr.popmovies.fragments.MovieDetailActivityFragment;
import com.udacity.pradeepkumarr.popmovies.fragments.MovieMainFragment;

/**
 * @author pradeepkumarr
 */
public class MainActivity extends AppCompatActivity implements MovieMainFragment.Callbacks{//} implements MovieMainFragment.Callbacks{ //implements PopupMenu.OnMenuItemClickListener {
    public boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (findViewById(R.id.details_fragment_container) != null) {
            mTwoPane = true;
           getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment_container, new MovieDetailActivityFragment())
                    .commit();

        } else {
            mTwoPane = false;
        }

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Welcome to Pop Movies!!, Default results is by popularity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

**/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       if (id == R.id.action_filter) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", movie);
            arguments.putBoolean("mTwoPane", mTwoPane);
            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, MovieDetailActivity.class);
            detailIntent.putExtra("movie", movie);
            detailIntent.putExtra("mTwoPane", mTwoPane);
            startActivity(detailIntent);
        }
    }



}
