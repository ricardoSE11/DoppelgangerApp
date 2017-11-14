package com.example.rshum.instaclone.Share;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.FilePaths;
import com.example.rshum.instaclone.Utils.FileSearch;
import com.example.rshum.instaclone.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by rshum on 12/09/2017.
 */

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    //Constants
    private static final int NUM_GRID_COLUMNS = 3;
    //widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar mProgressBar;
    private Spinner directorySpinner;

    //vars
    private ArrayList<String> directories;
    private String mAppend = "file:/";
    private String mSelectedImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container , false);
        galleryImage = (ImageView)view.findViewById(R.id.galleryImageView);
        gridView = (GridView)view.findViewById(R.id.gridView);
        directorySpinner = (Spinner)view.findViewById(R.id.spinnerDirectory);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        directories = new ArrayList<>();
        Log.d(TAG, "onCreateView: started");

        ImageView shareClose = (ImageView)view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the gallery fragment");
                getActivity().finish();
            }
        });

        //Aqui esta el textView que envía la imagen seleccionada a la siguiente Activity.
        TextView nextScreen = (TextView)view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navegando a la Share screen final.");
                Intent intent = new Intent(getActivity() , NextActivity.class);
                intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                startActivity(intent);

            }
        });

        init();
        return view;
    }

    private void init(){
        FilePaths filePaths = new FilePaths();
        //check for other folder inside "/storage/emulated/0/pictures"
        if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }

        directories.add(filePaths.CAMERA);
        directories.add(filePaths.ROOT_DIR);

        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++){
            int index = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index);
            directoryNames.add(string);

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item , directoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: selected: " + directories.get(position));

                //set up our image grid for the directory choser
                setupGridView(directories.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupGridView(String selectedDirectory){
        Log.d(TAG, "setupGridView: directorio escogido: " + selectedDirectory);
        final ArrayList<String> imgULRs = FileSearch.getFilePaths(selectedDirectory);

        //set the grid column width
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //use grid adapter to adapt images to gridView file://
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview , mAppend , imgULRs);
        gridView.setAdapter(adapter);

        //set the first image to be displayed when the activity fragment is inflated
        setImage(imgULRs.get(0) , galleryImage , mAppend);
        mSelectedImage = imgULRs.get(0);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selecciono una imagen: " + imgULRs.get(position));

                setImage(imgULRs.get(position) , galleryImage , mAppend);
                mSelectedImage = imgULRs.get(position);
            }
        });
    }

    private void setImage(String imgULR, ImageView image , String append){
        Log.d(TAG, "setImage: seteando una imagen");

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgULR, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}

