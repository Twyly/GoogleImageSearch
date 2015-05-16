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

    }

//    public ImageResultsAdapter(Activity activity, ArrayList<ImageResult> list) {
//        super(activity, list);
//    }
//
//    @Override
//    public View getDataRow(int position, View convertView, ViewGroup parent) {
//
//        ImageResult imageInfo = getItem(position);
//        if (convertView == null) {
//            convertView = LayoutInflater.from(mActivity).inflate(R.layout.item_image_result, parent, false);
//        }
//
//        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
//        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
//
//        // Clear Previous Results
//        ivImage.setImageResource(0);
//
//        // Paint View
//        tvTitle.setText(Html.fromHtml(imageInfo.title));
//        Picasso.with(mActivity).load(imageInfo.thumbUrl).into(ivImage);
//
//        return convertView;
//    }

    public ImageResultsAdapter(Context context, List<ImageResult> images) {
        super(context, R.layout.item_image_result, images);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageResult imageInfo = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent, false);
        }

        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
        //TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);

        // Clear Previous Results
        ivImage.setImageResource(0);


        // Paint View
        //tvTitle.setText(Html.fromHtml(imageInfo.title));
        Picasso.with(getContext()).load(imageInfo.thumbUrl).placeholder(R.drawable.geometry).into(ivImage);

        return convertView;
    }
}
