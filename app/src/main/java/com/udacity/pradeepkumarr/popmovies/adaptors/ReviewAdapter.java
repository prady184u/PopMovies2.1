package com.udacity.pradeepkumarr.popmovies.adaptors;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.pradeepkumarr.popmovies.domain.Review;

import java.util.List;
import com.udacity.pradeepkumarr.popmovies.R;

public class ReviewAdapter extends ArrayAdapter<Review> {
	public ReviewAdapter(Activity context, List<Review> reviews) {
		super(context,0, reviews);
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {

		if (convertView == null) {
			convertView =
					LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie_review, null);
		}
		TextView author = (TextView) convertView.findViewById(R.id.authtextView);
		TextView content = (TextView) convertView.findViewById(R.id.conttextViewTV);
		Review review = getItem(position);
		author.setText(review.getAuthor());
		content.setText(review.getContent());

			return convertView;
		}
	}

