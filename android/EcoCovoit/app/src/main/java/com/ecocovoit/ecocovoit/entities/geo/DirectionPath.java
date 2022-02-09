package com.ecocovoit.ecocovoit.entities.geo;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.RemoteEntity;
import com.ecocovoit.ecocovoit.entities.Transferable;
import com.ecocovoit.ecocovoit.utils.DateUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "DirectionPaths")
public class DirectionPath implements RemoteEntity, Transferable, LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "DirectionPathsId";

    @DatabaseField(columnName = FIELD_START_LOCATION, foreign = true, foreignColumnName = Location.FIELD_ID, foreignAutoCreate = true)
    private Location startLocation;
    public static final String FIELD_START_LOCATION = "startLocation";
    public static final String JSON_KEY_START_LOCATION = "startLocation";

    @DatabaseField(columnName = FIELD_START_TIME)
    private Date startTime;
    public static final String FIELD_START_TIME = "startTime";
    public static final String JSON_KEY_START_TIME = "startTime";

    @DatabaseField(columnName = FIELD_END_LOCATION, foreign = true, foreignColumnName = Location.FIELD_ID, foreignAutoCreate = true)
    private Location endLocation;
    public static final String FIELD_END_LOCATION = "endLocation";
    public static final String JSON_KEY_END_LOCATION = "endLocation";

    @DatabaseField(columnName = FIELD_END_TIME)
    private Date endTime;
    public static final String FIELD_END_TIME = "endTime";
    public static final String JSON_KEY_END_TIME = "endTime";

    @DatabaseField(columnName = FIELD_REMOTE_ID)
    private int remoteId;
    public static final String FIELD_REMOTE_ID = "remoteId";
    public static final String JSON_KEY_REMOTE_ID = "id";

    private List<Location> path;
    public static final String JSON_KEY_PATH = "path";

    public DirectionPath() {
        this.id = NO_ID;
        this.path = new ArrayList<>();
    }

    public DirectionPath(Location startLocation, Date startTime, Location endLocation, Date endTime, int remoteId, List<Location> path) {
        this.id = NO_ID;
        this.startLocation = startLocation;
        this.startTime = startTime;
        this.endLocation = endLocation;
        this.endTime = endTime;
        this.remoteId = remoteId;
        this.path = path;
    }

    public DirectionPath(JSONObject jsonObject) throws JSONException, ParseException {
        this.id = NO_ID;

        this.remoteId = jsonObject.getInt(JSON_KEY_REMOTE_ID);
        this.startLocation = new Location(jsonObject.getJSONObject(JSON_KEY_START_LOCATION));
        this.startTime = DateUtils.serverStringToDate(jsonObject.getString(JSON_KEY_START_TIME));
        this.endLocation = new Location(jsonObject.getJSONObject(JSON_KEY_END_LOCATION));
        this.endTime = DateUtils.serverStringToDate(jsonObject.getString(JSON_KEY_END_TIME));

        this.path = new ArrayList<>();
        JSONArray array = jsonObject.getJSONArray(JSON_KEY_PATH);
        for(int i=0; i<array.length(); i++) {
            Location location = new Location(array.getJSONObject(i));
            this.path.add(location);
        }
    }

    public void setPath(List<Location> path) {
        this.path = path;
    }

    @Override
    public int getId() {
        return id;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Location getEndLocation() {
        return endLocation;
    }

    public Date getEndTime() {
        return endTime;
    }

    public List<Location> getPath() {
        return path;
    }

    @Override
    public int getRemoteId() {
        return remoteId;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_REMOTE_ID, this.remoteId);
        jsonObject.put(JSON_KEY_START_LOCATION, this.startLocation.toJson());
        jsonObject.put(JSON_KEY_START_TIME, DateUtils.dateToServerString(this.startTime));
        jsonObject.put(JSON_KEY_END_LOCATION, this.endLocation.toJson());
        jsonObject.put(JSON_KEY_END_TIME, DateUtils.dateToServerString(this.endTime));

        JSONArray path = new JSONArray();
        for(Location location : this.path) {
            path.put(location.toJson());
        }
        jsonObject.put(JSON_KEY_PATH, path);

        return jsonObject;
    }
}
