package com.ecocovoit.ecocovoit.entities.rides;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.RemoteEntity;
import com.ecocovoit.ecocovoit.entities.Ride;
import com.ecocovoit.ecocovoit.entities.RideProvider;
import com.ecocovoit.ecocovoit.entities.RideUser;
import com.ecocovoit.ecocovoit.entities.TransportMean;
import com.ecocovoit.ecocovoit.entities.geo.DirectionPath;
import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.ecocovoit.ecocovoit.entities.parties.User;
import com.ecocovoit.ecocovoit.entities.transportmeans.Car;
import com.ecocovoit.ecocovoit.utils.DateUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

@DatabaseTable(tableName = "CarRides")
public class CarRide implements Ride, RemoteEntity, LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "CarRidesId";

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

    @DatabaseField(columnName = FIELD_PROVIDING_USER, foreign = true, foreignColumnName = User.FIELD_ID)
    private User providingUser;
    public static final String FIELD_PROVIDING_USER = "providingUser";
    public static final String JSON_KEY_PROVIDING_USER = "providingUser";

    @DatabaseField(columnName = FIELD_USING_USER, foreign = true, foreignColumnName = User.FIELD_ID)
    private User usingUser;
    public static final String FIELD_USING_USER = "usingUser";
    public static final String JSON_KEY_USING_USER = "usingUser";

    @DatabaseField(columnName = FIELD_CAR, foreign = true, foreignColumnName = Car.FIELD_ID)
    private Car car;
    public static final String FIELD_CAR = "car";
    public static final String JSON_KEY_CAR = "car";

    @DatabaseField(columnName = FIELD_DIRECTION_PATH, foreign = true, foreignColumnName = DirectionPath.FIELD_ID)
    private DirectionPath directionPath;
    public static final String FIELD_DIRECTION_PATH = "directionPath";
    public static final String JSON_KEY_DIRECTION_PATH = "directionPath";

    @DatabaseField(columnName = FIELD_REMOTE_ID)
    private int remoteId;
    public static final String FIELD_REMOTE_ID = "remoteId";
    public static final String JSON_KEY_REMOTE_ID = "id";

    public CarRide() {
        this.id = NO_ID;
    }

    public CarRide(Location startLocation, Date startTime, Location endLocation, Date endTime, User providingUser, User usingUser, Car car, DirectionPath directionPath, int remoteId) {
        this.id = NO_ID;
        this.startLocation = startLocation;
        this.startTime = startTime;
        this.endLocation = endLocation;
        this.endTime = endTime;
        this.providingUser = providingUser;
        this.usingUser = usingUser;
        this.car = car;
        this.directionPath = directionPath;
        this.remoteId = remoteId;
    }

    public CarRide(JSONObject jsonObject) throws JSONException, ParseException {
        this.id = NO_ID;
        this.remoteId = jsonObject.getInt(JSON_KEY_REMOTE_ID);
        this.car = new Car(jsonObject.getJSONObject(JSON_KEY_CAR));
        this.directionPath = new DirectionPath(jsonObject.getJSONObject(JSON_KEY_DIRECTION_PATH));
        this.startLocation = new Location(jsonObject.getJSONObject(JSON_KEY_START_LOCATION));
        this.startTime = DateUtils.serverStringToDate(jsonObject.getString(JSON_KEY_START_TIME));
        this.endLocation = new Location(jsonObject.getJSONObject(JSON_KEY_END_LOCATION));
        this.endTime = DateUtils.serverStringToDate(jsonObject.getString(JSON_KEY_END_TIME));
        this.usingUser = new User(jsonObject.getJSONObject(JSON_KEY_USING_USER));
        this.providingUser = new User(jsonObject.getJSONObject(JSON_KEY_PROVIDING_USER));
    }

    @Override
    public int getId() {
        return id;
    }

    public DirectionPath getDirectionPath() {
        return directionPath;
    }

    public void setStartLocation(Location startLocation) {
        this.startLocation = startLocation;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndLocation(Location endLocation) {
        this.endLocation = endLocation;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setProvidingUser(User providingUser) {
        this.providingUser = providingUser;
    }

    public void setUsingUser(User usingUser) {
        this.usingUser = usingUser;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setDirectionPath(DirectionPath directionPath) {
        this.directionPath = directionPath;
    }

    @Override
    public Location getStartLocation() {
        return startLocation;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public Location getEndLocation() {
        return endLocation;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public RideProvider getRideProvider() {
        return providingUser;
    }

    @Override
    public RideUser getRideUser() {
        return usingUser;
    }

    @Override
    public TransportMean getTransportMean() {
        return car;
    }

    @Override
    public int getRemoteId() {
        return this.remoteId;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_REMOTE_ID, this.remoteId);
        jsonObject.put(JSON_KEY_CAR, this.car.toJson());
        jsonObject.put(JSON_KEY_DIRECTION_PATH, this.directionPath.toJson());
        jsonObject.put(JSON_KEY_END_LOCATION, this.endLocation.toJson());
        jsonObject.put(JSON_KEY_END_TIME, DateUtils.dateToServerString(this.endTime));
        jsonObject.put(JSON_KEY_START_LOCATION, this.startLocation);
        jsonObject.put(JSON_KEY_START_TIME, DateUtils.dateToServerString(this.startTime));
        jsonObject.put(JSON_KEY_USING_USER, this.usingUser.toJson());
        jsonObject.put(JSON_KEY_PROVIDING_USER, this.providingUser.toJson());
        return jsonObject;
    }
}
