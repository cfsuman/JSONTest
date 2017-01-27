package com.cfsuman.me.jsontest;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String urlString;
    private Context mContext;
    private Activity mActivity;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        // Get the widget reference from XML layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        // Specify a layout for RecyclerView
        mLayoutManager = new GridLayoutManager(mContext,1);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        urlString = "http://pastebin.com/raw/wgkJgazE";
        new DownloadJSONStreamTask().execute(urlString);

        // Set a Refresh Listener for the SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh the data
                mAdapter.notifyDataSetChanged();

                // Calls setRefreshing(false) when it is finish
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // Download the JSON
    private class DownloadJSONStreamTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute(){
            mSwipeRefreshLayout.setRefreshing(true);
        }

        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){
            // Stop progress indicator when update finish
            mSwipeRefreshLayout.setRefreshing(false);

            if(stream!=null){
                mAdapter = new ProfilesAdapter(mContext, stream);
                mRecyclerView.setAdapter(mAdapter);
                //Toast.makeText(mContext,"Downloaded.",Toast.LENGTH_SHORT).show();
            }else {
                //Toast.makeText(mContext,"Download failed.",Toast.LENGTH_SHORT).show();
            }
        } // onPostExecute() end
    } // ProcessJSON class end
} // Activity end
