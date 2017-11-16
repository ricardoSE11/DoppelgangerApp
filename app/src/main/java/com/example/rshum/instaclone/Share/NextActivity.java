package com.example.rshum.instaclone.Share;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.GridBitmapAdapter;
import com.example.rshum.instaclone.Utils.GridImageAdapter;
import com.example.rshum.instaclone.Utils.UniversalImageLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NextActivity extends AppCompatActivity  {

    private static final String TAG = "NextActivity";
    private static final String URL = " ";
    private static final int NUM_GRID_COLUMNS = 3;

    private Context mContext = NextActivity.this;

    //Variables
    private String mAppend = "file:/";
    private String imgUrl;
    private Bitmap bitmap;
    private Intent intent;
    private ArrayList<Bitmap> imagesBitmaps;

    //UI
    public ImageView imageSave;
    public GridView doppelgangers;
    public ProgressDialog mProgressDialog;

    //Prueba
    //String prueba;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        //Is very important to verify Views names
        imageSave = (ImageView)findViewById(R.id.imageSave);

        new HttpRequestTask().execute();
        System.out.println("Recibimos la imagen");


        ImageView backArrow = (ImageView)findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });


        setImage();
        //setUpBitmapGrid(imagesBitmaps);
        temporalGridSetup();

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

    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridView = (GridView)findViewById(R.id.gridView);

        int gridWith = getResources().getDisplayMetrics().widthPixels;
        int imageWidtth = gridWith/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidtth);

        GridImageAdapter adapter = new GridImageAdapter(mContext , R.layout.layout_grid_imageview , "" , imgURLs);
        gridView.setAdapter(adapter);

    }

    //NO FUNCIONA COMO SE ESPERA
    private void setUpBitmapGrid(ArrayList<Bitmap> bitmaps)
    {
        GridView gridView = (GridView)findViewById(R.id.gridView);
        GridBitmapAdapter bitmapAdapter = new GridBitmapAdapter(mContext , bitmaps);
        gridView.setAdapter(bitmapAdapter);
    }

    private void temporalGridSetup()
    {
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://ih0.redbubble.net/image.73789920.5043/flat,800x800,075,t.u5.jpg");
        imgURLs.add("https://res.cloudinary.com/teepublic/image/private/s--I_t454YZ--/t_Preview/b_rgb:0195c3,c_limit,f_jpg,h_630,q_90,w_630/v1498233447/production/designs/1689060_1.jpg");
        imgURLs.add("http://img07.deviantart.net/6122/i/2014/051/5/c/meeseeks_by_michaelogicalm-d77a0fl.png");
        imgURLs.add("https://cdn.shopify.com/s/files/1/1103/6548/products/meeseeks-calligram-03.jpg?v=1486079062");

        imgURLs.add("https://res.cloudinary.com/teepublic/image/private/s--I_t454YZ--/t_Preview/b_rgb:0195c3,c_limit,f_jpg,h_630,q_90,w_630/v1498233447/production/designs/1689060_1.jpg");
        imgURLs.add("http://img07.deviantart.net/6122/i/2014/051/5/c/meeseeks_by_michaelogicalm-d77a0fl.png");
        imgURLs.add("https://cdn.shopify.com/s/files/1/1103/6548/products/meeseeks-calligram-03.jpg?v=1486079062");
        imgURLs.add("https://res.cloudinary.com/teepublic/image/private/s--I_t454YZ--/t_Preview/b_rgb:0195c3,c_limit,f_jpg,h_630,q_90,w_630/v1498233447/production/designs/1689060_1.jpg");
        imgURLs.add("http://img07.deviantart.net/6122/i/2014/051/5/c/meeseeks_by_michaelogicalm-d77a0fl.png");
        imgURLs.add("https://cdn.shopify.com/s/files/1/1103/6548/products/meeseeks-calligram-03.jpg?v=1486079062");

        setupImageGrid(imgURLs);

    }

    //Couldn´t be written, for unknown reasons.
    //private boolean isRootTask()

    public void fetchImages()
    {
        Log.i(TAG , "Intentando traer imagenes del Servidor");
        mProgressDialog = ProgressDialog.show(mContext , "Imagenes similares" , "Obteniendo imagenes del servidor, por favor espere" , true , false);


    }

    public Bitmap getBase64Bitmap(String base64DeImagen) {
        if(base64DeImagen!=null)
        {
            byte[] image_data = Base64.decode(base64DeImagen, Base64.NO_WRAP);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 32; //32 pixles
            options.outWidth = 32; //32 pixles
            options.outMimeType = "png"; //this could be image/jpeg, image/png, etc

            return BitmapFactory.decodeByteArray(image_data, 0, image_data.length, options);
        }
        return null;
    }

    class HttpRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String urlString = "http://192.168.1.61:50628/api/Img/0";
            java.net.URL url = null;

            try
            {
                url = new URL(urlString);
            }

            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            HttpURLConnection con = null;
            try {
                con = (HttpURLConnection) url.openConnection();
            }

            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                con.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
            }
            catch (IOException e) {
                e.printStackTrace();
            }


            String output;
            StringBuffer response = new StringBuffer();

            try {
                while ((output = in.readLine()) != null) {
                    response.append(output);
                }
                in.close();
                //System.out.println(response.toString());
                return response.toString();


            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String requestAnswer)
        {
            if (requestAnswer != null)
            {
                Log.d(TAG , "Respuesta de HttpRequestTask: " + requestAnswer);
                Bitmap imgReceived = getBase64Bitmap(requestAnswer);
                imageSave.setImageBitmap(imgReceived);

            }

            else
                Log.d(TAG , "el String es nulo");
            //Aqui recibimos las imagenes
        }
    }

}//fin de clase
