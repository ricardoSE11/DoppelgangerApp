package com.fabianbarahona.pruebafiltros;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.widget.ImageView;

import static java.lang.Integer.*;

public interface IFiltroBN {
    public Bitmap setPixels();
}

public abstract class filtro{
    public Bitmap original =((BitmapDrawable)ImageView.getDrawable()).getBitmap();
    Bitmap filteredImage = null;
    int A,R,G,B;
    int colorPixel;
    int width =original.getWidth();
    int height = original.getHeight();

    public void setBitmapOriginal(Bitmap org){
        this.original=org;
    }
}

public class filtroAveraging extends filtro implements IFiltroBN{
    public filtroAveraging(Bitmap x){
        setBitmapOriginal(x);
    }
    @Override
    public Bitmap setPixels() {
        for (int i = 0; i < height; i++) {
            for (int x = 0; x < width; x++) {
                colorPixel = original.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                R = (R + G + B) / 3;
                G = B = R;
                filteredImage.setPixel(i, x, Color.argb(A, R, G, B));
                }
            }

            return filteredImage;
        }
    }
public class filtroDesaturarion extends filtro implements IFiltroBN{
    public filtroDesaturarion(Bitmap x){
        setBitmapOriginal(x);
    }
    @Override
    public Bitmap setPixels() {
        for (int i = 0; i < height; i++) {
            for (int x = 0; x < width; x++) {
                colorPixel = original.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                R = (max((max(R, B)), G)+ min((min(R, B)), G))/2;
                G = B = R;
                filteredImage.setPixel(i, x, Color.argb(A, R, G, B));
            }
        }
        return filteredImage;
    }

    //return finalImage;
}

public class filtroMAX extends filtro implements IFiltroBN{
    public filtroMAX(Bitmap x){
        setBitmapOriginal(x);
    }
    public Bitmap setPixels() {
        for (int i = 0; i < height; i++) {
            for (int x = 0; x < width; x++) {
                colorPixel = original.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);

                R = max((max(R, B)), G);
                G = B = R;
                filteredImage.setPixel(i, x, Color.argb(A, R, G, B));
            }
        }
        return filteredImage;
    }
}
public class filtroMIN extends filtro implements IFiltroBN{
    public filtroMIN(Bitmap x){
        setBitmapOriginal(x);
    }
    public Bitmap setPixels() {
        for (int i = 0; i < height; i++) {
            for (int x = 0; x < width; x++) {
                colorPixel = original.getPixel(i, x);
                A = Color.alpha(colorPixel);
                R = Color.red(colorPixel);
                G = Color.green(colorPixel);
                B = Color.blue(colorPixel);


                R = min((min(R, B)), G);

                G = B = R;
                filteredImage.setPixel(i, x, Color.argb(A, R, G, B));
            }
        }
        return filteredImage;
    }
}