package com.example.rshum.instaclone.Home;


import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rshum.instaclone.R;

/**
 * Created by rshum on 12/09/2017.
 */

public class CameraFragment extends Fragment {
    private static final String TAG = "CameraFragment";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container , false);

        return view;
    }
}

