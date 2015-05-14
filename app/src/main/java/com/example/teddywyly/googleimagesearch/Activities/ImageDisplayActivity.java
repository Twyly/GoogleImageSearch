package com.example.teddywyly.googleimagesearch.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.teddywyly.googleimagesearch.Models.ImageResult;
import com.example.teddywyly.googleimagesearch.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class ImageDisplayActivity extends AppCompatActivity {

    private ProgressBar pbImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        getSupportActionBar().hide();
        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        TextView tvImageDescription = (TextView)findViewById(R.id.tvImageDescription);
        pbImage = (ProgressBar)findViewById(R.id.pbImage);
        tvImageDescription.setText(Html.fromHtml(result.title));

        ImageView ivFullImage = (ImageView)findViewById(R.id.ivFullImage);
        Picasso.with(this).load(result.fullUrl).into(ivFullImage, new Callback() {
            @Override
            public void onSuccess() {
                pbImage.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                // Handle Error
                pbImage.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
