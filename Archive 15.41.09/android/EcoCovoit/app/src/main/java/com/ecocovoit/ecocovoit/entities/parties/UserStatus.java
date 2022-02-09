package com.ecocovoit.ecocovoit.entities.parties;

import android.content.Context;

public enum UserStatus {
    NEW_USER;

    private String name;

    private static final int RESOURCE_NAME_NEW_USER = 0;

    UserStatus() {
        this.name = null;
    }

    /**Return the name according to R.string resource.
     * It may change according to the language of the host device.
     * Must be use only to display to the user.*/
    String getName(Context context) {
        if(this.name == null) {
            switch (this) {
                case NEW_USER:
                    this.name = context.getString(RESOURCE_NAME_NEW_USER);
            }
        }
        return this.name;
    }
}
