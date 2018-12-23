package com.example.mars;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;


public class Activity_ImageList extends AppCompatActivity implements ApiRequestResponse {

    Adapter adapter;
    TextView noData;
    ProgressBar progressBar;
    GridView gridView;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagelist);
        RegisterListener.registerListener(Activity_ImageList.this);
         gridView = findViewById(R.id.grid_view);
        noData = findViewById(R.id.no_data);
        progressBar = findViewById(R.id.pbHeaderProgress);
        NetworkOperations.callApi(Constant.UPLOAD_PROFILE_IMAGE_API, Activity_ImageList.this);
        adapter =new Adapter(Activity_ImageList.this);
        progressBar.setVisibility(View.VISIBLE);

        if(Jsonparser.publicList==null)
        {
            noData.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void success(String response) {
        try {
            Jsonparser.parseJsontags(response);
            if (response != null && Jsonparser.publicList != null && Jsonparser.publicList.size() > 0) {
                progressBar.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.INVISIBLE);
                gridView.setAdapter(adapter);
            }

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void failure(String response) {

    }
}
