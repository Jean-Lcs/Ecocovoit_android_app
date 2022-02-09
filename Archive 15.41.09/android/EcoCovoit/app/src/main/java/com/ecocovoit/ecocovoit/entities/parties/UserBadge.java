package com.ecocovoit.ecocovoit.entities.parties;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public enum UserBadge {
    SILVER,
    BRONZE,
    GOLD;

    private String name;
    private Bitmap image;

    private static final int RESOURCE_BITMAP_SILVER = 0;
    private static final int RESOURCE_BITMAP_BRONZE = 0;
    private static final int RESOURCE_BITMAP_GOLD = 0;

    private static final int RESOURCE_NAME_SILVER = 0;
    private static final int RESOURCE_NAME_BRONZE = 0;
    private static final int RESOURCE_NAME_GOLD = 0;

    UserBadge() {
        this.name = null;
        this.image = null;
    }

    /**Return the name according to R.drawable bitmap image resource.
     * the result may change according to the host device screen capabilities.
     * Must be use only to display to the user.*/
    Bitmap getImage(Context context) {
        if(image == null) {
            switch (this) {
                case SILVER:
                    this.image = getBitmap(context, RESOURCE_BITMAP_SILVER);
                case BRONZE:
                    this.image = getBitmap(context, RESOURCE_BITMAP_BRONZE);
                case GOLD:
                    this.image = getBitmap(context, RESOURCE_BITMAP_GOLD);
            }
        }
        return this.image;
    }

    /**Return the name according to R.string resource.
     * It may change according to the language of the host device.
     * Must be use only to display to the user.*/
    String getName(Context context) {
        if(this.name == null) {
            switch (this) {
                case SILVER:
                    this.name = context.getString(RESOURCE_NAME_SILVER);
                case BRONZE:
                    this.name = context.getString(RESOURCE_NAME_BRONZE);
                case GOLD:
                    this.name = context.getString(RESOURCE_NAME_GOLD);
            }
        }
        return this.name;
    }

    private Bitmap getBitmap(Context context, int resourceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeResource(
                context.getResources(),
                resourceId, options
        );
    }
}
