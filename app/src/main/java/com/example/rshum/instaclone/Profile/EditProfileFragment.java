package com.example.rshum.instaclone.Profile;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by rshum on 13/09/2017.
 */

public class EditProfileFragment extends Fragment {

    private static final String TAG = "EditProfileFragment";

    private ImageView mProfilePhoto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container , false);
        mProfilePhoto = (ImageView)view.findViewById(R.id.profile_photo);


        setProfileImage();

        //back arrow for navigatin back to "ProfileActivity"
        ImageView backArrow = (ImageView)view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back to ProfileActivity");
                getActivity().finish();
            }
        });

        return view;
    }

    private void setProfileImage()
    {
        Log.d(TAG, "setProfileImage: setting profile image");
        String imgURL = "www.mariodelafuente.org/putolinux/wp-content/uploads/2017/03/antivirus-para-android.png";
        UniversalImageLoader.setImage(imgURL , mProfilePhoto , null , "http://");


    }
}
