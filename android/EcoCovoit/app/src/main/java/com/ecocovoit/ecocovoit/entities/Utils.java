package com.ecocovoit.ecocovoit.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Utils {
    public static String getStringFromImage(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap getImageFromString(String image) {
        ByteArrayInputStream stream = new ByteArrayInputStream(image.getBytes());
        return BitmapFactory.decodeStream(stream);
    }
}
