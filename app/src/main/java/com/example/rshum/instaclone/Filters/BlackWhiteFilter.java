package com.example.rshum.instaclone.Filters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.os.Build;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by rshum on 14/09/2017.
 */

public class BlackWhiteFilter {
    private static final String TAG = "BlackWhiteFilter";

    public Bitmap Averaging(Bitmap src){
        Bitmap finalImage = Bitmap.createBitmap(src.getWidth() , src.getHeight(), src.getConfig());
        Log.d(TAG, "Averaging: width and height HELLLOOO: " + "Width: " + src.getWidth() + ", " + "Height:" + src.getHeight() );
        //Bitmap x =((BitmapDrawable)arepa.getDrawable()).getBitmap();
        int A,R,G,B;
        int colorPixel;
        int width = src.getWidth();
        int height = src.getHeight();

        for (int i=0; i < height; i++){
            for (int x=0; x < width; x++){
                colorPixel = src.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                R=(R+G+B)/3;
                G=B=R;
                finalImage.setPixel(i, x, Color.argb(A, R, G, B));
            }
        }

        return finalImage;
    }


    public Bitmap Desaturation(Bitmap src){
        Bitmap finalImage = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

        int A,R,G,B;
        int colorPixel;
        int width = src.getWidth();
        int height = src.getHeight();

        for (int i=0; i<height; i++){
            for (int x=0; x<width; x++){
                colorPixel = src.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                R=((max((max(R, B)), G)+ min((min(R, B)), G))/2);
                G=B=R;
                finalImage.setPixel(i, x, Color.argb(A, R, G, B));
            }
        }

        return finalImage;
    }


    public Bitmap MAX(Bitmap src){
        Bitmap finalImage = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        //Bitmap x =((BitmapDrawable)arepa.getDrawable()).getBitmap();
        int A,R,G,B;
        int colorPixel;
        int width = src.getWidth();
        int height = src.getHeight();

        for (int i=0; i<height; i++){
            for (int x=0; x<width; x++){
                colorPixel = src.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                R=max((max(R, B)), G);
                G=B=R;
                finalImage.setPixel(i, x, Color.argb(A, R, G, B));
            }
        }

        return finalImage;
    }
    public Bitmap MIN(Bitmap src){
        Bitmap finalImage = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        //Bitmap x =((BitmapDrawable)arepa.getDrawable()).getBitmap();
        int A,R,G,B;
        int colorPixel;
        int width = src.getWidth();
        int height = src.getHeight();

        for (int i=0; i<height; i++){
            for (int x=0; x<width; x++){
                colorPixel = src.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                R=min((min(R, B)), G);
                G=B=R;
                finalImage.setPixel(i, x, Color.argb(A, R, G, B));
            }
        }

        return finalImage;
    }


    public Bitmap applyGaussianBlur(Bitmap src) {
        //set gaussian blur configuration
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };
        // create instance of Convolution matrix
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        // Apply Configuration
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        //return out put bitmap
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }
    public Bitmap applyEmboss(Bitmap src) {
        //set gaussian blur configuration
        double[][] GaussianBlurConfig = new double[][] {
                { -2, -1, 0 },
                { -1, 1, 1 },
                { 0, 1, 2 }
        };
        // create instance of Convolution matrix
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        // Apply Configuration
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
        //return out put bitmap
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }
}