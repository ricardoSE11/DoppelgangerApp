package com.example.rshum.instaclone.Share;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rshum.instaclone.Filters.BlackWhiteFilter;
import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.Permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import static android.app.Activity.RESULT_OK;

/**
 * Created by rshum on 12/09/2017.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //The first one is the one that is displayed (Title)
    public String[] datos = {"Filtros" ,
                    "1. Averaging" , //1
                    "2. Desaturation" ,
                    "3. Decomposition (Max)" ,
                    "4. Decomposition (Min)" ,
                    "5. GaussianBlur" ,
                    "6. Original"};
    //constants
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 5;

    final BlackWhiteFilter blackWhiteFilter = new BlackWhiteFilter();

    public Spinner listaFiltros;
    public ImageView displayedPhoto;
    public Button btnLaunchCamera;
    public Button savePicture;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started.");

        //ActivityCompat.requestPermissions(EditStageActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getContext() , android.R.layout.simple_spinner_item , datos);

        listaFiltros = (Spinner)view.findViewById(R.id.listaFiltros);
        savePicture = (Button) view.findViewById(R.id.btnSave);
        displayedPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        btnLaunchCamera = (Button) view.findViewById(R.id.btnLaunchCamera);

        listaFiltros.setAdapter(adaptador);



        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launching camera.");

                if (((ShareActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                    if (((ShareActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);

                    } else {
                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Log.d(TAG, "onActivityResult: donde taking a photo");
                Log.d(TAG, "onActivityResult: attempting to navigate to share screen");
                //navigating to the final share screen to publish photo

                final Bitmap cameraImage = (Bitmap) data.getExtras().get("data");
                displayedPhoto.setImageBitmap(cameraImage);

                savePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveImage(cameraImage);
                    }
                });

            }

            listaFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position)
                    {
                        case 1:
                        {
                            Bitmap final_image = blackWhiteFilter.Averaging(displayedPhoto.getDrawingCache());
                            displayedPhoto.setImageBitmap(final_image);
                            break;}

                        case 2:
                            Bitmap final_image2 = blackWhiteFilter.Desaturation(displayedPhoto.getDrawingCache());
                            displayedPhoto.setImageBitmap(final_image2);
                            break;

                        case 3:
                            Bitmap final_image3 = blackWhiteFilter.MAX(displayedPhoto.getDrawingCache());
                            displayedPhoto.setImageBitmap(final_image3);
                            break;

                        case 4:
                            Bitmap final_image4 = blackWhiteFilter.MIN(displayedPhoto.getDrawingCache());
                            displayedPhoto.setImageBitmap(final_image4);
                            break;

                        case 5:
                            Bitmap final_image5 = blackWhiteFilter.applyGaussianBlur(displayedPhoto.getDrawingCache());
                            displayedPhoto.setImageBitmap(final_image5);
                            break;

                        case 6:
                            Bitmap final_image6 = blackWhiteFilter.applyEmboss(displayedPhoto.getDrawingCache());
                            displayedPhoto.setImageBitmap(final_image6);
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }



    }

    public void takePicture()
    {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureName = getPictureName();
        File imageFile = new File(pictureDirectory , pictureName);
        Uri pictureUri = Uri.fromFile(imageFile);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT , pictureUri);

        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }


    //Gives a unique name to the picture
    private String getPictureName()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = sdf.format(new Date());
        return "InstaPOO" + timeStamp + ".jpg";
    }


    public void saveImage(Bitmap bitmapToSave)
    {
        boolean success = false;
        //Gets the root where the External Storage Directory is
        String root = Environment.getExternalStorageDirectory().toString();
        //TimeStamp (Unique number to identify the pictures)
        String imageName = getPictureName();
        File myDir = new File(root);
        //Creates a Directory/folder (I`m guessing)
        myDir.mkdirs();
        //Folder name: "Image-InstaPOOyyyyMMdd_HHmmss.jpg"
        String fname = "Image-" + imageName + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapToSave.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (success) {
            Toast.makeText(getContext(), "Image saved with success",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(),
                    "Error during image saving", Toast.LENGTH_LONG).show();

        }
    }



}
//end of class

