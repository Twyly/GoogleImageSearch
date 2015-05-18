package com.example.teddywyly.googleimagesearch.searchscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.teddywyly.googleimagesearch.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by teddywyly on 5/11/15.
 */
public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

    private class ViewHolder {
        ImageView thumbnail;
    }

    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
            viewHolder.thumbnail = (ImageView)convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Clear Previous Results
        viewHolder.thumbnail.setImageResource(0);

        // Paint View
        Picasso.with(getContext()).load(imageInfo.thumbUrl).placeholder(R.drawable.geometry).into(viewHolder.thumbnail);

        return convertView;
    }
}
