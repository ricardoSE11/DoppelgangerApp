package com.example.rshum.instaclone.Utils;

import android.os.Environment;


public class FilePaths {
    //"storage/emulated/0/picture"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICTURES = ROOT_DIR + "/Pictures";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

}
