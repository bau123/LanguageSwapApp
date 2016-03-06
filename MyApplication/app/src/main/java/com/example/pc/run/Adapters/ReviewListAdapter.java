package com.example.pc.run.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.pc.run.Objects.Review;
import com.example.pc.run.R;

import java.util.ArrayList;

/**
 * Created by Joss on 05/03/2016.
 */
public class ReviewListAdapter extends BaseAdapter{
    private Activity context;

    private ArrayList<Review> reviews = new ArrayList<>();

    public ReviewListAdapter(Activity context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_reviewlist_item, null);
            viewHolder = new ViewHolder();

            viewHolder.review = (TextView)convertView.findViewById(R.id.reviewView);
            viewHolder.reviewer = (TextView)convertView.findViewById(R.id.authorView);
            viewHolder.rating = (TextView)convertView.findViewById(R.id.ratingView);

            viewHolder.review.setText(reviews.get(position).getComment());
            viewHolder.reviewer.setText(reviews.get(position).getReviewer());
            viewHolder.rating.setText("Rating: " + reviews.get(position).getRating());

            /*
                TODO: add OnClickListener to show full review
             */

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView review;
        public TextView reviewer;
        public TextView rating;
    }
}
