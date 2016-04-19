package com.udacity.pradeepkumarr.popmovies.adaptors;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.pradeepkumarr.popmovies.R;
import com.udacity.pradeepkumarr.popmovies.domain.Movie;

import java.util.List;

/**
 * @author pradeepkumarr
 */
public class MovieGridAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieGridAdapter.class.getSimpleName();
    public static final String image_poster_url = "http://image.tmdb.org/t/p/w185/";

    public MovieGridAdapter(Activity context, List<Movie> movies) {

        super(context, 0, movies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the Movie object from the ArrayAdapter at the appropriate position
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_grid_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.poster_image);

        Picasso.with(getContext())
                .load(image_poster_url +movie.getPoster_path())
                .placeholder(R.drawable.popmovies)
                .fit()
                .into(iconView);

        return convertView;
    }

}
