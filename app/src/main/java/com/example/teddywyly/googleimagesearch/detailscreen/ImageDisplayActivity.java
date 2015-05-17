package com.example.teddywyly.googleimagesearch.detailscreen;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.teddywyly.googleimagesearch.helpers.DeviceDimensionsHelper;
import com.example.teddywyly.googleimagesearch.helpers.ErrorHelper;
import com.example.teddywyly.googleimagesearch.searchscreen.ImageResult;
import com.example.teddywyly.googleimagesearch.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uk.co.senab.photoview.PhotoViewAttacher;


public class ImageDisplayActivity extends AppCompatActivity {

    private ProgressBar pbImage;
    private ShareActionProvider miShareProvider;
    private ImageResult result;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        result = (ImageResult) getIntent().getParcelableExtra("result");

        getSupportActionBar().setTitle(Html.fromHtml(result.title));

        pbImage = (ProgressBar)findViewById(R.id.pbImage);
        final ImageView ivFullImage = (ImageView)findViewById(R.id.ivFullImage);

        float ratio = (float)result.imageWidth/result.imageHeight;
        int width = DeviceDimensionsHelper.getDisplayWidth(this);
        int height = (int)(width/ratio);

        Picasso.with(this).load(result.fullUrl).resize(width, height).into(ivFullImage, new Callback() {
            @Override
            public void onSuccess() {
                if (mAttacher == null) {
                    mAttacher = new PhotoViewAttacher(ivFullImage);
                } else {
                    mAttacher.update();
                }
                pbImage.setVisibility(View.GONE);
                setupShareIntent(ivFullImage);
            }

            @Override
            public void onError() {
                ErrorHelper.showErrorAlert(ImageDisplayActivity.this, ErrorHelper.ErrorType.GENERIC);
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
        if (id == R.id.miDetail) {
            showDescriptionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDescriptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml(result.title));
        AlertDialog dialog = builder.show();
        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void setupShareIntent(ImageView imageView) {
        // Fetch Bitmap Uri locally
        if (miShareProvider != null) {
            Uri bmpUri = getLocalBitmapUri(imageView); // see previous remote images section
            // Create share intent as described above
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Attach share event to the menu item provider
            miShareProvider.setShareIntent(shareIntent);
        }

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
