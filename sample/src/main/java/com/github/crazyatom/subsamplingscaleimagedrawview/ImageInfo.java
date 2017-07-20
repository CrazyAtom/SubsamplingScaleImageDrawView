package com.github.crazyatom.subsamplingscaleimagedrawview;

import android.graphics.BitmapFactory;

/**
 * Created by crazy on 2017-07-07.
 */

public class ImageInfo {

    public String path;
    public String preview_path;

    public ImageInfo(String path, String preview_path) {
        this.path = path;
        this.preview_path = preview_path;
    }

    public int getWidth() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        return options.outWidth;
    }

    public int getHeight() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        return options.outHeight;
    }
}
