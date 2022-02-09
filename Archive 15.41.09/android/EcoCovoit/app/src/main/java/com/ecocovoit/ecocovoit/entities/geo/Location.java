package com.ecocovoit.ecocovoit.entities.geo;

import android.webkit.JavascriptInterface;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.RemoteEntity;
import com.ecocovoit.ecocovoit.entities.Transferable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable(tableName = "Locations")
public class Location implements RemoteEntity, Transferable, LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "LocationsId";

    @DatabaseField(columnName = FIELD_LATITUDE)
    private double latitude;
    public static final String FIELD_LATITUDE = "latitude";
    public static final String JSON_KEY_LATITUDE = "latitude";

    @DatabaseField(columnName = FIELD_LONGITUDE)
    private double longitude;
    public static final String FIELD_LONGITUDE = "longitude";
    public static final String JSON_KEY_LONGITUDE = "longitude";

    @DatabaseField(columnName = FIELD_OSM_NODE_ID)
    private int osmNodeId;
    public static final String FIELD_OSM_NODE_ID = "osmNodeId";
    public static final String JSON_KEY_OSM_NODE_ID = "osmNodeId";

    public Location() {
        this.id = NO_ID;
    }

    public Location(double latitude, double longitude, int osmNodeId) {
        this.id = NO_ID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.osmNodeId = osmNodeId;
    }

    public Location(JSONObject jsonObject) throws JSONException {
        this.id = NO_ID;
        this.osmNodeId = jsonObject.getInt(JSON_KEY_OSM_NODE_ID);
        this.latitude = jsonObject.getLong(JSON_KEY_LATITUDE);
        this.longitude = jsonObject.getLong(JSON_KEY_LONGITUDE);
    }

    @Override
    public int getId() {
        return id;
    }

    @JavascriptInterface
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @JavascriptInterface
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getOsmNodeId() {
        return osmNodeId;
    }

    public void setOsmNodeId(int osmNodeId) {
        this.osmNodeId = osmNodeId;
    }

    @Override
    public int getRemoteId() {
        return this.osmNodeId;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_OSM_NODE_ID, this.osmNodeId);
        jsonObject.put(JSON_KEY_LATITUDE, this.latitude);
        jsonObject.put(JSON_KEY_LONGITUDE, this.longitude);
        return jsonObject;
    }
}
