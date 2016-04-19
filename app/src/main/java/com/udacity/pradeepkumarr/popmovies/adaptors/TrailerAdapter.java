package com.udacity.pradeepkumarr.popmovies.adaptors;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.pradeepkumarr.popmovies.domain.Trailer;
import com.udacity.pradeepkumarr.popmovies.R;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {

	public TrailerAdapter(Activity context, List<Trailer> trailers) {
		super(context,0, trailers);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			convertView =
					LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie_trailer, null);
		}

		Trailer trailer = getItem(position);
		ImageView thumbnail = (ImageView) convertView.findViewById(R.id.trailer_image);
		TextView trailerName = (TextView) convertView.findViewById(R.id.trailerName);
		trailerName.setText(trailer.getName());
			Picasso.with(getContext())
					.load(trailer.getThumbnail())
					.placeholder(R.drawable.popmovies)
					.error(R.drawable.popmovies)
					.fit()
					.into(thumbnail);

			return convertView;
		}
	}

