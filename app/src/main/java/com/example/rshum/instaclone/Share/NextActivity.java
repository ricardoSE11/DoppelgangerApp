package com.example.rshum.instaclone.Share;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.UniversalImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.security.AccessController.getContext;

/**
 * Created by rshum on 13/09/2017.
 */

public class NextActivity extends AppCompatActivity  {

    private static final String TAG = "NextActivity";

    //Variables
    private String mAppend = "file:/";
    private String imgUrl;
    private Bitmap bitmap;
    public ImageView imageSave;

    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        //Is very important to verify Views names
        imageSave = (ImageView)findViewById(R.id.imageSave);


        ImageView backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });


        setImage();

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
            Toast.makeText(getApplicationContext(), "Image saved with success",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "Error during image saving", Toast.LENGTH_LONG).show();

        }
    }

    //Gets the image url from the incoming intent and displays the chosen image
    private void setImage(){
        intent = getIntent();
        ImageView image = (ImageView)findViewById(R.id.imageSave);

        //Logica utilizada para el boton de Share (Tener en cuenta)
        if(intent.hasExtra(getString(R.string.selected_image)))
        {
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            Log.d(TAG , "setImage: got new image url:" + imgUrl);
            UniversalImageLoader.setImage( imgUrl , image , null , mAppend);
        }
        
        else if (intent.hasExtra(getString(R.string.selected_bitmap)))
        {
            bitmap = (Bitmap)intent.getParcelableExtra(getString(R.string.selected_bitmap));
            Log.d(TAG , "setImage: got new bitmap");
            image.setImageBitmap(bitmap);
        }


        /*Bitmap bitmap = BitmapFactory.decodeFile(stringPrueba);
        return bitmap;*/
    }

    //Couldn´t be written, for unknown reasons.
    //private boolean isRootTask()

}
