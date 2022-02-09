package com.ecocovoit.ecocovoit.communication.request;

import com.ecocovoit.ecocovoit.entities.TransportMean;
import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.ecocovoit.ecocovoit.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class DirectionPathRequest extends RequestFromConnectedUser {

    private Location startLocation;
    public static final String REQUEST_JSON_KEY_START_LOCATION = "startLocation";

    private Date startTime;
    public static final String REQUEST_JSON_KEY_START_TIME = "startTime";

    private Location endLocation;
    public static final String REQUEST_JSON_KEY_END_LOCATION = "endLocation";

    private Date endTime;
    public static final String REQUEST_JSON_KEY_END_TIME = "endTime";

    private TransportMean transportMean;

    public DirectionPathRequest(int token, Location startLocation, Date startTime, Location endLocation, Date endTime) throws JSONException {
        super(token);
        this.startLocation = startLocation;
        this.startTime = startTime;
        this.endLocation = endLocation;
        this.endTime = endTime;
    }

    @Override
    protected void fillJsonObject(JSONObject jsonObject) throws JSONException {
        jsonObject.put(REQUEST_JSON_KEY_START_LOCATION, this.startLocation.toJson());
        jsonObject.put(REQUEST_JSON_KEY_START_TIME, DateUtils.dateToServerString(this.startTime));
        jsonObject.put(REQUEST_JSON_KEY_END_LOCATION, this.endLocation.toJson());
        jsonObject.put(REQUEST_JSON_KEY_END_TIME, DateUtils.dateToServerString(this.endTime));
    }
}
