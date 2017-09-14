package com.example.rshum.instaclone.Share;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.UniversalImageLoader;

/**
 * Created by rshum on 13/09/2017.
 */

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    //Variables
    private String mAppend = "file:/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        //Is very important to verify Views names
        ImageView backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });

        /*TextView save = (TextView)findViewById(R.id.tvSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");
            }
        });*/


        setImage();
    }

    //Gets the image url from the incoming intent and displays the chosen image
    private void setImage(){
        Intent intent = getIntent();
        ImageView image = (ImageView)findViewById(R.id.imageSave);
        //UniversalImageLoader can handle Null, but what we could have done is check if the String is null
        UniversalImageLoader.setImage(intent.getStringExtra(getString(R.string.selected_image)) , image , null , mAppend);
    }



}
