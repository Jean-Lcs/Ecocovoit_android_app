package com.ecocovoit.ecocovoit.entities.rides;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.RemoteEntity;
import com.ecocovoit.ecocovoit.entities.Ride;
import com.ecocovoit.ecocovoit.entities.RideProvider;
import com.ecocovoit.ecocovoit.entities.RideUser;
import com.ecocovoit.ecocovoit.entities.TransportMean;
import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.ecocovoit.ecocovoit.entities.parties.User;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

@DatabaseTable(tableName = "Covoits")
public class Covoit implements Ride, RemoteEntity, LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "CovoitsId";

    @DatabaseField(columnName = FIELD_CAR_RIDE, foreign = true, foreignColumnName = CarRide.FIELD_ID)
    private CarRide carRide;
    public static final String FIELD_CAR_RIDE = "carRide";
    public static final String JSON_KEY_CAR_RIDE = "carRide";

    @DatabaseField(columnName = FIELD_USING_USER, foreign = true, foreignColumnName = User.FIELD_ID)
    private User usingUser;
    public static final String FIELD_USING_USER = "usingUser";
    public static final String JSON_KEY_USING_USER = "usingUser";

    @DatabaseField(columnName = FIELD_REMOTE_ID)
    private int remoteId;
    public static final String FIELD_REMOTE_ID = "remoteId";
    public static final String JSON_KEY_REMOTE_ID = "id";

    public Covoit() {
        this.id = NO_ID;
    }

    public Covoit(CarRide carRide, User usingUser, int remoteId) {
        this.id = NO_ID;
        this.carRide = carRide;
        this.usingUser = usingUser;
        this.remoteId = remoteId;
    }

    public Covoit(JSONObject jsonObject) throws JSONException, ParseException {
        this.id = NO_ID;
        this.remoteId = jsonObject.getInt(JSON_KEY_REMOTE_ID);
        this.usingUser = new User(jsonObject.getJSONObject(JSON_KEY_USING_USER));
        this.carRide = new CarRide(jsonObject.getJSONObject(JSON_KEY_CAR_RIDE));
    }

    @Override
    public int getId() {
        return id;
    }

    public CarRide getCarRide() {
        return carRide;
    }

    @Override
    public Location getStartLocation() {
        return carRide.getStartLocation();
    }

    @Override
    public Date getStartTime() {
        return carRide.getStartTime();
    }

    @Override
    public Location getEndLocation() {
        return carRide.getEndLocation();
    }

    @Override
    public Date getEndTime() {
        return carRide.getEndTime();
    }

    @Override
    public RideProvider getRideProvider() {
        return carRide.getRideProvider();
    }

    @Override
    public RideUser getRideUser() {
        return usingUser;
    }

    @Override
    public TransportMean getTransportMean() {
        return carRide.getTransportMean();
    }

    @Override
    public int getRemoteId() {
        return this.remoteId;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_REMOTE_ID, this.remoteId);
        jsonObject.put(JSON_KEY_USING_USER, this.usingUser.toJson());
        jsonObject.put(JSON_KEY_CAR_RIDE, this.carRide.toJson());
        return jsonObject;
    }
}
