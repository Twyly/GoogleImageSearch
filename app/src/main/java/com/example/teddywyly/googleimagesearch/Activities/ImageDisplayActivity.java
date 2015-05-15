package com.example.teddywyly.googleimagesearch.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageDisplayActivity extends AppCompatActivity {

    private ProgressBar pbImage;
    private ShareActionProvider miShareProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        ImageResult result = (ImageResult) getIntent().getSerializableExtra("result");
        getSupportActionBar().setTitle(Html.fromHtml(result.title));

        //TextView tvImageDescription = (TextView)findViewById(R.id.tvImageDescription);
        pbImage = (ProgressBar)findViewById(R.id.pbImage);
        //tvImageDescription.setText(Html.fromHtml(result.title));

        final ImageView ivFullImage = (ImageView)findViewById(R.id.ivFullImage);
        Picasso.with(this).load(result.fullUrl).into(ivFullImage, new Callback() {
            @Override
            public void onSuccess() {
                pbImage.setVisibility(View.GONE);
                setupShareIntent(ivFullImage);
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

        MenuItem item = menu.findItem(R.id.miShare);
        miShareProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(item);

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

    private void setupShareIntent(ImageView imageView) {
        // Fetch Bitmap Uri locally
        Uri bmpUri = getLocalBitmapUri(imageView); // see previous remote images section
        // Create share intent as described above
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        miShareProvider.setShareIntent(shareIntent);

    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    private Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
