package com.cfsuman.me.jsontest;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;


public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ViewHolder> {
    private Context mContext;
    private String mStream;
    private int mItemCount = 0;
    private JSONArray mRootArray;

    public ProfilesAdapter(Context context, String stream){
        mContext = context;
        mStream = stream;

        if(mStream!=null){
            try{
                // Get the full HTTP data as JSONArray
                mRootArray = new JSONArray(mStream);
                mItemCount = mRootArray.length();
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v){
            super(v);
            // Get the widget reference from the custom layout
            mCardView = (CardView) v.findViewById(R.id.card_view);
            mTextView = (TextView) v.findViewById(R.id.tv_profile_summary);
            mImageView = (ImageView) v.findViewById(R.id.iv_profile_image);
        }
    }

    @Override
    public ProfilesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(mContext).inflate(R.layout.profile_summary_view,parent,false);

        // Get the TextView reference from RecyclerView current item
        final TextView tv_label = (TextView) v.findViewById(R.id.tv_profile_summary);

        // Set a click listener for the current item of RecyclerView
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the RecyclerView current item text
                final String label = tv_label.getText().toString();

                // Display the RecyclerView clicked item label
                /*Toast.makeText(
                        mContext,
                        "Clicked : " + label,
                        Toast.LENGTH_SHORT
                ).show();*/

                // Initialize a new intent object to navigate
                Intent intent = new Intent(mContext,DetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("json_stream",mStream);
                intent.putExtra("json_array_index",tv_label.getId());
                mContext.startActivity(intent);
            }
        });

        ViewHolder vh = new ViewHolder(v);

        // Return the ViewHolder
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        /*
            Important in JSON DATA
            -------------------------
            * Square bracket ([) represents a JSON array
            * Curly bracket ({) represents a JSON object
            * JSON object contains key/value pairs
            * Each key is a String and value may be different data types
         */

        // The JSON tree
        /*-----------------------------
            ROOT --------- array
                ITEM
                    id
                    created_at
                    width
                    height
                    color
                    likes
                    liked_by_user
                    user ------------- object
                        id
                        username
                        name
                        profile_image ---------- object
                            small
                            medium
                            large
                        links -------------- object
                            self
                            html
                            photos
                            likes
                    current_user_collections ------------ array
                    urls ---------------------- object
                        raw
                        full
                        regular
                        small
                        thumb
                    categories ----------------------- array
                        ITEM
                            id
                            title
                            photo_count
                            links -------------- object
                                self
                                photo
                    links ----------------- object
                        self
                        photos
        -------------------------------*/

        // Process the JSON data
        try{
            // Get the current array element as JSONObject
            JSONObject arrayElement = mRootArray.getJSONObject(position);

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
            String summary = "@"+ id +"\n" +user_name +"\n   "+ likes +" likes \n"+ categories;
            SpannableStringBuilder ssBuilder = new SpannableStringBuilder(summary);

            // Initialize a new StyleSpan to display bold text
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

            // Apply the bold text style span
            ssBuilder.setSpan(
                    boldSpan, // Span to add
                    summary.indexOf(user_name), // Start of the span (inclusive)
                    summary.indexOf(user_name) + String.valueOf(user_name).length(), // End of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
            );

            // Initialize a new RelativeSizeSpan to display large size text
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(1.2f);
            // Apply the large size text to span
            ssBuilder.setSpan(
                    relativeSizeSpan, // Span to add
                    summary.indexOf(user_name), // Start of the span (inclusive)
                    summary.indexOf(user_name) + String.valueOf(user_name).length(), // End of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
            );

            // Initialize a new ImageSpan to display like/favorite image/icon
            ImageSpan favoriteImageSpan = new ImageSpan(mContext,R.drawable.ic_favorite_white_18dp);

            // Apply the favorite image to the span
            ssBuilder.setSpan(
                    favoriteImageSpan, // Span to add
                    summary.indexOf(user_name) + String.valueOf(user_name).length()+1, // Start of the span (inclusive)
                    summary.indexOf(user_name) + String.valueOf(user_name).length()+2, // End of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
            );

            // Initialize a new StyleSpan to display italic text
            StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
            // Apply the italic text style span
            ssBuilder.setSpan(
                    italicSpan, // Span to add
                    summary.indexOf(categories), // Start of the span (inclusive)
                    summary.indexOf(categories) + String.valueOf(categories).length(), // End of the span (exclusive)
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // Do not extend the span when text add later
            );


            // Display summary
            holder.mTextView.setText(ssBuilder);
            holder.mTextView.setId(position);

            /*
                    Download the image from online and set it as
                    ImageView image programmatically.
                */
            new DownLoadImageTask(holder.mImageView).execute(user_profile_image_large);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount(){
        // Count the items
        return mItemCount;
    }

    // Download image
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