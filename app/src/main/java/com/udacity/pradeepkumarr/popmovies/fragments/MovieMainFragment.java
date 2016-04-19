package com.udacity.pradeepkumarr.popmovies.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.udacity.pradeepkumarr.popmovies.adaptors.MovieGridAdapter;
import com.udacity.pradeepkumarr.popmovies.R;
import com.udacity.pradeepkumarr.popmovies.activities.MovieDetailActivity;
import com.udacity.pradeepkumarr.popmovies.asynctask.FetchFavoritesMovieTask;
import com.udacity.pradeepkumarr.popmovies.asynctask.FetchMovieDetailsTask;
import com.udacity.pradeepkumarr.popmovies.common.MovieUtil;
import com.udacity.pradeepkumarr.popmovies.db.MovieContract;
import com.udacity.pradeepkumarr.popmovies.db.MovieDbHelper;
import com.udacity.pradeepkumarr.popmovies.domain.Movie;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieMainFragment extends Fragment {

    public static final String SELECTED_KEY= "SELECTED_KEY";
    private GridView grid;
    public MovieMainFragment() {
    }

    private  MovieDbHelper helper ;
    private MovieGridAdapter movieGridAdapter;

    private ArrayList<Movie> moviesList = new ArrayList<Movie>();
    private String sort_by;
    private String current_sort_by;
    private int mPosition = ListView.INVALID_POSITION;
    private boolean reFetchFlag = false;
    private long movieID;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        /*if(savedInstanceState != null ){
            moviesList = savedInstanceState.getParcelableArrayList("movies");
            sort_by = savedInstanceState.getString("sort_by");
        }else{
            moviesList = new ArrayList<Movie>();

            sort_by = PreferenceManager.
                    getDefaultSharedPreferences(getActivity()).
                    getString(getString(R.string.pref_result_key), getString(R.string.pref_result_default));
            // Toast.makeText(getActivity(), "First sort by : " + sort_by,Toast.LENGTH_SHORT).show();
            fetchMovieAsyncTask(sort_by);
        }*/


    }



    @Override
    public void onStart() {
        super.onStart();
        sort_by = PreferenceManager.
                getDefaultSharedPreferences(getActivity()).
                getString(getString(R.string.pref_result_key), getString(R.string.pref_result_default));
        if(sort_by != null && !sort_by.equals("fav"))
            fetchMovieAsyncTask(sort_by);
        else
            fetchFavMovies();
    }
//

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        outState.putParcelableArrayList("movies", moviesList);
        outState.putString("sort_by", sort_by);
        outState.putInt("mPosition",mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void fetchMovieAsyncTask(String sort_by) {

        if(MovieUtil.isOnline(getActivity())) {
            new MovieDetailsTask().execute(sort_by);
            //new FetchMovieDetailsTask().execute(sort_by);
        }else {
            MovieUtil.showDialogWhenOffline(getActivity(), sort_by);
        }
    }

    public void fetchFavMovies() {
         helper = new MovieDbHelper(getActivity());
         new FavMovieDetailsTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getCursor());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_filter){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if(savedInstanceState != null ){
            moviesList = savedInstanceState.getParcelableArrayList("movies");
            sort_by = savedInstanceState.getString("sort_by");
        }
        movieGridAdapter = new MovieGridAdapter(getActivity(), moviesList);

        // Get a reference to the ListView, and attach this adapter to it.
        grid = (GridView) rootView.findViewById(R.id.movies_grid);
        grid.setAdapter(movieGridAdapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = movieGridAdapter.getItem(i);
                //Toast.makeText(getActivity(), " Movie : " + movie.getTitle() + " rating: " + movie.getVote_average(), Toast.LENGTH_SHORT).show();
                //movieGridAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                /*intent.putExtra("overview", movie.getOverview());
                intent.putExtra("poster_path", movie.getPoster_path());
                intent.putExtra("release_date", movie.getRelease_date());
                intent.putExtra("title", movie.getTitle());
                intent.putExtra("rating", Double.toString(movie.getVote_average()));
                intent.putExtra("id", movie.getId());
                intent.putExtra("backgroundPosterPath", movie.getBackdrop_path());*/
                intent.putExtra("movie", movie);

               ((Callbacks) getActivity())
                        .onItemSelected(movie);
                startActivity(intent);

                mPosition = i;
            }
        });


        return rootView;
    }

    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        void onItemSelected(Movie movie);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
            // If we don't need to restart the loader, and there's a desired position to restore
            // to, do so now.
            grid.setSelection(mPosition);
        }
    }
    private void addMoviesToAdaptor(ArrayList<Movie> moviesList) {
        if(moviesList != null){
            movieGridAdapter.clear();
            for(Movie movie : moviesList){
                movieGridAdapter.add(movie);
            }

            if (moviesList.size() != 0) {
                movieGridAdapter.notifyDataSetChanged();
            }
        }


    }


    private class MovieDetailsTask extends FetchMovieDetailsTask {

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesList) {
            // showDialog("Downloaded " + result + " bytes");
            addMoviesToAdaptor(moviesList);
        }
    }

    private class FavMovieDetailsTask extends FetchFavoritesMovieTask {

        @Override
        protected void onPostExecute(ArrayList<Movie> moviesList) {
            // showDialog("Downloaded " + result + " bytes");
            addMoviesToAdaptor(moviesList);
        }
    }

    private Cursor getCursor() {

        SQLiteDatabase db = helper.getReadableDatabase();


        String[] projection = {
                MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_TITLE,
                MovieContract.MovieEntry.COLUMN_POSTER_URL,
                MovieContract.MovieEntry.COLUMN_BACKDROP_URL,
                MovieContract.MovieEntry.COLUMN_VOTE_COUNT,
                MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                MovieContract.MovieEntry.COLUMN_SYNOPSIS,
                MovieContract.MovieEntry.COLUMN_RATING
        };

       // String order = MovieContract.MovieEntry.RATING + " DESC";

        return db.query(MovieContract.MovieEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,"1");
    }



}
