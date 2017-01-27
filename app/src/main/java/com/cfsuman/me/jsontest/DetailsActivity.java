package com.cfsuman.me.jsontest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;

public class DetailsActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private TextView mTV1;
    private ImageView mIVAppBar;

    private CoordinatorLayout mCLayout;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCToolbarLayout;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = DetailsActivity.this;

        // Get the widget reference from XML layout
        mTV1 = (TextView) findViewById(R.id.tv_1);
        mIVAppBar = (ImageView) findViewById(R.id.iv_appbar);
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        // Set the support action bar
        setSupportActionBar(mToolbar);
        // Enable up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the intent
        Intent intent = getIntent();
        String stream = intent.getStringExtra("json_stream");
        int index = intent.getIntExtra("json_array_index",0);
        //Toast.makeText(mContext,""+index,Toast.LENGTH_SHORT).show();

        // Process the JSON data
        try{
            // Get the full HTTP data as JSONArray
            JSONArray mRootArray = new JSONArray(stream);

            // Get the current array element as JSONObject
            JSONObject arrayElement = mRootArray.getJSONObject(index);

            // Get the current user details
            String id = arrayElement.getString("id");
            //holder.mTextViewLabel.setText("\n"+holder.mTextViewLabel.getText()+id);
            String created_at = arrayElement.getString("created_at");
            //holder.mTextViewLabel.setText("\n"+holder.mTextViewLabel.getText()+created_at);
            int width = arrayElement.getInt("width");
            //holder.mTextViewLabel.setText("\n"+holder.mTextViewLabel.getText()+width);
            int height = arrayElement.getInt("height");
            //holder.mTextViewLabel.setText("\n"+holder.mTextViewLabel.getText()+height);
            String color = arrayElement.getString("color");
            //holder.mTextViewLabel.setText("\n"+holder.mTextViewLabel.getText()+color);
            int likes = arrayElement.getInt("likes");
            //holder.mTextViewLabel.setText("\n"+holder.mTextViewLabel.getText()+likes);
            Boolean liked_by_user = arrayElement.getBoolean("liked_by_user");

            // Get the JSONObject 'user'
            JSONObject user = arrayElement.getJSONObject("user");

            // Get current users data
            String user_id = user.getString("id");
            String user_username = user.getString("username");
            String user_name = user.getString("name");

            JSONObject user_profile_images = user.getJSONObject("profile_image");
            // Get profile images in various size
            String user_profile_image_small = user_profile_images.getString("small");
            String user_profile_image_medium = user_profile_images.getString("medium");
            String user_profile_image_large = user_profile_images.getString("large");

            JSONObject user_links = user.getJSONObject("links");
            // Get user links
            String user_link_self = user_links.getString("self");
            String user_link_html = user_links.getString("html");
            String user_link_photos = user_links.getString("photos");
            String user_link_likes = user_links.getString("likes");

            // Get current user collections array
            JSONArray current_user_collections_array = arrayElement.getJSONArray("current_user_collections");
            // Here we skip the processing of this array elements

            JSONObject urls = arrayElement.getJSONObject("urls");
            // Get urls
            String url_raw = urls.getString("raw");
            String url_full = urls.getString("full");
            String url_regular = urls.getString("regular");
            String url_small = urls.getString("small");
            String url_thumb = urls.getString("thumb");

            // Get categories array
            JSONArray categories_array = arrayElement.getJSONArray("categories");
            String categories = "";
            // Loop thorough the array elements
            for(int i=0;i<categories_array.length();i++){
                if(categories.length()>0){
                    categories +=", ";
                }

                JSONObject category = categories_array.getJSONObject(i);
                int category_id = category.getInt("id");
                String category_title = category.getString("title");
                int category_photo_count = category.getInt("photo_count");

                // Get JSONObject links
                JSONObject category_links = category.getJSONObject("links");
                String category_link_self = category_links.getString("self");
                String category_link_photos = category_links.getString("photos");

                categories +=category_title;
            }

            JSONObject links = arrayElement.getJSONObject("links");
            // Get urls
            String link_self = links.getString("self");
            String link_html = links.getString("html");
            String link_download = links.getString("download");

            // Initialize a string
            String summary = "ID : "+ id
                    + "\nCREATED AT : "+ created_at
                    + "\nWIDTH : " + width
                    + "\nHEIGHT : " + height
                    + "\nCOLOR : " + color
                    + "\nLIKES : " + likes
                    + "\nLIKED BY USER : " + liked_by_user
                    +"\nMore data goes here....";
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(summary);

            // Set a title for collapsing toolbar layout
            mCToolbarLayout.setTitle(user_name);

            // Display summary
            mTV1.setText(summary);

            /*
                    Download the image from online and set it as
                    ImageView image programmatically.
                */
            new DownLoadImageTask(mIVAppBar).execute(url_full);
        }catch(JSONException e){
            e.printStackTrace();
        }

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(mCLayout,"FAB clicked.",Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }
}
