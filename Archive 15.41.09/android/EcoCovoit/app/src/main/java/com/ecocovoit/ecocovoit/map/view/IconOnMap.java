package com.ecocovoit.ecocovoit.map.view;

import android.webkit.JavascriptInterface;

import com.ecocovoit.ecocovoit.entities.geo.Location;

import java.util.List;

public class IconOnMap {

    /**The url of the map.*/
    public String iconUrl;

    public int width;
    public int height;

    /**All location on the map where the same icon will be drawn.*/
    public List<Location> locations;

    public IconOnMap(String iconUrl, int width, int height, List<Location> locations) {
        this.iconUrl = iconUrl;
        this.width = width;
        this.height = height;
        this.locations = locations;
    }

    @JavascriptInterface
    public String getIconUrl() {
        return iconUrl;
    }

    @JavascriptInterface
    public int getWidth() {
        return width;
    }

    @JavascriptInterface
    public int getHeight() {
        return height;
    }

    @JavascriptInterface
    public int getLocationsCount() {
        return locations.size();
    }

    @JavascriptInterface
    public Location getLocation(int index) {
        return locations.get(index);
    }
}
