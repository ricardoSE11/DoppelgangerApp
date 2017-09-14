package com.example.rshum.instaclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.BottomNavigationViewHelper;
import com.example.rshum.instaclone.Utils.GridImageAdapter;
import com.example.rshum.instaclone.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by rshum on 12/09/2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext = ProfileActivity.this;

    private ProgressBar mProgressBar;

    private ImageView profilePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG , "onCreate: started.");

        setupBottomNavigationView();
        setupToolBar();
        setupActtivityWidgets();
        setProfileImage();

        tempGridSetup();
    }

    private void tempGridSetup()
    {
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://ih0.redbubble.net/image.73789920.5043/flat,800x800,075,t.u5.jpg");
        imgURLs.add("https://res.cloudinary.com/teepublic/image/private/s--I_t454YZ--/t_Preview/b_rgb:0195c3,c_limit,f_jpg,h_630,q_90,w_630/v1498233447/production/designs/1689060_1.jpg");
        imgURLs.add("http://img07.deviantart.net/6122/i/2014/051/5/c/meeseeks_by_michaelogicalm-d77a0fl.png");
        imgURLs.add("https://cdn.shopify.com/s/files/1/1103/6548/products/meeseeks-calligram-03.jpg?v=1486079062");

        setupImageGrid(imgURLs);

    }

    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridView = (GridView)findViewById(R.id.gridView);

        int gridWith = getResources().getDisplayMetrics().widthPixels;
        int imageWidtth = gridWith/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidtth);

        GridImageAdapter adapter = new GridImageAdapter(mContext , R.layout.layout_grid_imageview , "" , imgURLs);
        gridView.setAdapter(adapter);
    }

    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: setting profile photo");
        String imgULR = "http://www.mariodelafuente.org/putolinux/wp-content/uploads/2017/03/antivirus-para-android.png";
        UniversalImageLoader.setImage(imgULR ,profilePhoto , mProgressBar , "");
    }

    private void setupActtivityWidgets(){
        mProgressBar = (ProgressBar)findViewById(R.id.profileProgressBar);
        mProgressBar.setVisibility(View.GONE);
        profilePhoto = (ImageView)findViewById(R.id.profile_photo);
    }

    private void setupToolBar()
    {
        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = (ImageView)findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent (mContext , AccountSettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    // We create this method so we can use it on other Activities
    private void setupBottomNavigationView(){
        Log.d(TAG , "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext , bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }


}
