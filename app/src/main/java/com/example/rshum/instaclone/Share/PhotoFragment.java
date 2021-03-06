package com.example.rshum.instaclone.Share;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.Permissions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

/**
 * Created by rshum on 12/09/2017.
 */

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    //constants
    private static final int PHOTO_FRAGMENT_NUM = 1;
    private static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 5;

    //UI
    public Button btnSearch;
    public ImageView displayedPhoto;
    public Button btnLaunchCamera;
    public Button savePicture;

    private String mSelectedImage;

    //Cliente de Servidor
    private static AsyncHttpClient client = new AsyncHttpClient();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        Log.d(TAG, "onCreateView: started.");

        //ActivityCompat.requestPermissions(EditStageActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        savePicture = (Button) view.findViewById(R.id.btnSave);
        displayedPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        btnLaunchCamera = (Button) view.findViewById(R.id.btnLaunchCamera);


        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: launching(inicializando) camera.");

                if (((ShareActivity) getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                    if (((ShareActivity) getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "onClick: starting(empezando) camera");
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
                Log.d(TAG, "onActivityResult: Ya se tomo la foto");
                Log.d(TAG, "onActivityResult: Intentando navegar en Share screen");
                //navigating to the final share screen to publish photo

                final Bitmap cameraImage;
                cameraImage = (Bitmap) data.getExtras().get("data");

                displayedPhoto.setImageBitmap(cameraImage);

                /*savePicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveImage(cameraImage);
                    }
                });*/


                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try
                        {
                            Intent intent = new Intent(getActivity() , NextActivity.class);
                            intent.putExtra(getString(R.string.selected_bitmap) , cameraImage);

                            // --- POST: ---
                            String value = bitmapTo64Base(cameraImage);
                            String url = "http://192.168.1.61:50628/api/Img";
                            client.setConnectTimeout(40000);
                            AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    System.out.println("El POST fue exitoso");
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                    System.out.println("HUBO UN ERROR EN LA OPERACION");
                                }
                            };
                            RequestParams params = new RequestParams();
                            params.put("StrImagen",value.toString());
                            client.addHeader("StrImagen","olasoyotro");
                            client.post(url , params , responseHandler);
                            // --- POST ---
                            startActivity(intent);
                        }
                        catch(NullPointerException e)
                        {
                            Log.d(TAG , "onActivityResult: NullPointerException: " + e.getMessage());
                        }
                    }
                });

            }
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
            Toast.makeText(getContext(), "La imagen fue guardada con éxito ",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(),
                    "No se pudo guardar la imagen", Toast.LENGTH_LONG).show();

        }
    }


    public String bitmapTo64Base(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG , 100 , baos);
        byte[] b = baos.toByteArray();
        String encodeImage = Base64.encodeToString(b , Base64.DEFAULT);
        return encodeImage;
    }


    class HttpRequestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            String urlString = "http://192.168.1.61:50628/api/Img/0";
            URL url = null;

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
                System.out.println(response.toString());
                return response.toString();


            }
            catch (IOException e) {
                e.printStackTrace();
            }

            return response.toString();
        }

        @Override
        protected void onPostExecute(String greeting)
        {
            if (greeting != null)
                Log.d(TAG , "intento de HttpRequestTask: " + greeting);

            else
                Log.d(TAG , "el String es nulo");
            //Aqui recibimos las imagenes
        }
    }

    //Metodos de POST
    public File bitmapToFile(Bitmap bitmap) throws Exception
    {
        File file = new File("path");
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
        return file;
    }

}
//end of class

