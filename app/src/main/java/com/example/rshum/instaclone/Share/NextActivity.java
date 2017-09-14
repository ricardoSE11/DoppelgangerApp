package com.example.rshum.instaclone.Share;

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

import com.example.rshum.instaclone.Filters.BlackWhiteFilter;
import com.example.rshum.instaclone.Filters.IFiltroBN;
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

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    public String[] datos = {"Filtros" ,
            "1. Averaging" , //1
            "2. Desaturation" ,
            "3. Decomposition (Max)" ,
            "4. Decomposition (Min)" ,
            "5. GaussianBlur" ,
            "6. Original"};

    //Variables
    private String mAppend = "file:/";

    public Spinner listaFiltros;
    public Button savePicture;
    public ImageView imageSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , datos);

        listaFiltros = (Spinner)findViewById(R.id.listaFiltros);

        listaFiltros.setAdapter(adaptador);

        //Is very important to verify Views names
        imageSave = (ImageView)findViewById(R.id.imageSave);
        savePicture = (Button) findViewById(R.id.btnSave);
        ImageView backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });
        setImage();

        final BlackWhiteFilter blackWhiteFilter = new BlackWhiteFilter();
        final Bitmap usefulBit = setImage();

        listaFiltros.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 1:
                        blackWhiteFilter.Averaging(usefulBit);
                        break;

                    case 2:
                        blackWhiteFilter.Desaturation(usefulBit);
                        break;

                    case 3:
                        blackWhiteFilter.MAX(usefulBit);
                        break;

                    case 4:
                        blackWhiteFilter.MIN(usefulBit);
                        break;

                    case 5:
                        blackWhiteFilter.applyGaussianBlur(usefulBit);
                        break;

                    case 6:
                        blackWhiteFilter.applyEmboss(usefulBit);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        savePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(setImage());
            }
        });

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
    private Bitmap setImage(){
        Intent intent = getIntent();
        ImageView image = (ImageView)findViewById(R.id.imageSave);
        //UniversalImageLoader can handle Null, but what we could have done is check if the String is null
        UniversalImageLoader.setImage(intent.getStringExtra(getString(R.string.selected_image)) , image , null , mAppend);
        String stringPrueba = intent.getStringExtra(getString(R.string.selected_image));
        Bitmap bitmap = BitmapFactory.decodeFile(stringPrueba);
        return bitmap;
    }



}
