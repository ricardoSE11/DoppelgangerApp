package com.example.rshum.instaclone.Share;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
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

import com.addicticks.net.httpsupload.HttpsFileUploader;
import com.addicticks.net.httpsupload.HttpsFileUploaderConfig;
import com.addicticks.net.httpsupload.HttpsFileUploaderResult;
import com.addicticks.net.httpsupload.UploadItemFile;
import com.example.rshum.instaclone.R;
import com.example.rshum.instaclone.Utils.FilePaths;
import com.example.rshum.instaclone.Utils.FileSearch;
import com.example.rshum.instaclone.Utils.GridImageAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

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

    //Cliente de servidor
    private static AsyncHttpClient client = new AsyncHttpClient();

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

                // --- POST ---
                String value = post();
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
                // --- o ---

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
        Log.d(TAG, "setImage: seteando una imagen" + imgULR);

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
                //System.out.println(response.toString());
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
                Log.d(TAG , "Respuesta de HttpRequestTask: " + greeting);

            else
                Log.d(TAG , "el String es nulo");
            //Aqui recibimos las imagenes
        }
    }

    //Codigo para POST

    public String bitmapTo64Base(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG , 100 , baos);
        byte[] b = baos.toByteArray();
        String encodeImage = Base64.encodeToString(b , Base64.DEFAULT);
        return encodeImage;
    }

    public String post()
    {
        galleryImage.buildDrawingCache();
        Bitmap bitmap = galleryImage.getDrawingCache();
        if (bitmap != null)
        {
            String base64DeImagen = bitmapTo64Base(bitmap);
            //System.out.println(base64DeImagen);
            Log.d(TAG , "Intentando pasar la imagen a base64: " + base64DeImagen.replaceAll("\\s+",""));
            return base64DeImagen;

        }

        else
            Log.d(TAG , "el bitmap es nulo");
        return  null;
    }

    }

