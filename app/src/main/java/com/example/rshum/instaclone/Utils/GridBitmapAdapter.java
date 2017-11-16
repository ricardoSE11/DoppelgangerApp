package com.example.rshum.instaclone.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GridBitmapAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<Bitmap> bitmaps;

    // 1 (Constructor)
    public GridBitmapAdapter(Context context, ArrayList<Bitmap> bitmaps) {
        this.mContext = context;
        this.bitmaps = bitmaps;
    }

    //2 (Numero de celdas que hay que renderizar)
    @Override
    public int getCount() {
        return 0;
    }

    //3 (No es esencial)
    @Override
    public Object getItem(int i) {
        return null;
    }

    //4 ((No es esencial))
    @Override
    public long getItemId(int i) {
        return 0;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView dummyTextView = new TextView(mContext);
        dummyTextView.setText(String.valueOf(position));
        return dummyTextView;
    }

}
