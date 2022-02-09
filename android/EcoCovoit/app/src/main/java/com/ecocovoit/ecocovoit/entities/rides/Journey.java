package com.ecocovoit.ecocovoit.entities.rides;

import com.ecocovoit.ecocovoit.entities.MultiRideProvider;
import com.ecocovoit.ecocovoit.entities.Ride;
import com.ecocovoit.ecocovoit.entities.RideProvider;
import com.ecocovoit.ecocovoit.entities.RideUser;
import com.ecocovoit.ecocovoit.entities.TransportMean;
import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.ecocovoit.ecocovoit.entities.parties.User;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "Journeys")
public class Journey implements Ride {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "JourneysId";

    @DatabaseField(columnName = FIELD_USING_USER, foreign = true, foreignColumnName = User.FIELD_ID)
    private User usingUser;
    public static final String FIELD_USING_USER = "usingUser";
    public static final String JSON_KEY_USING_USER = "usingUser";

    @DatabaseField(columnName = FIELD_REMOTE_ID)
    private int remoteId;
    public static final String FIELD_REMOTE_ID = "remoteId";
    public static final String JSON_KEY_REMOTE_ID = "id";

    private List<Ride> trajets;
    public static final String JSON_KEY_TRAJETS = "trajets";

    public Journey() {
        this.id = NO_ID;
        this.trajets = new ArrayList<>();
    }

    public Journey(User usingUser, int remoteId, List<Ride> trajets) {
        this.id = NO_ID;
        this.usingUser = usingUser;
        this.remoteId = remoteId;
        this.trajets = trajets;
    }

    public Journey(JSONObject jsonObject) throws JSONException, ParseException {
        this.remoteId = jsonObject.getInt(JSON_KEY_REMOTE_ID);
        this.usingUser = new User(jsonObject.getJSONObject(JSON_KEY_USING_USER));

        this.trajets = new ArrayList<>();
        JSONArray array = jsonObject.getJSONArray(JSON_KEY_TRAJETS);
        for(int i=0; i<array.length(); i++) {
            Covoit covoit = new Covoit(array.getJSONObject(i));
            this.trajets.add(covoit);
        }
    }

    public void setTrajets(List<Ride> trajets) {
        this.trajets = trajets;
    }

    @Override
    public int getId() {
        return id;
    }

    public List<Ride> getTrajets() {
        return trajets;
    }

    public User getUsingUser() {
        return usingUser;
    }

    @Override
    public Location getStartLocation() {
        if(trajets == null || trajets.size() == 0) {
            return null;
        }
        else {
            return trajets.get(0).getStartLocation();
        }
    }

    @Override
    public Date getStartTime() {
        if(trajets == null || trajets.size() == 0) {
            return null;
        }
        else {
            return trajets.get(0).getStartTime();
        }
    }

    @Override
    public Location getEndLocation() {
        if(trajets == null || trajets.size() == 0) {
            return null;
        }
        else {
            return trajets.get(0).getEndLocation();
        }
    }

    @Override
    public Date getEndTime() {
        if(trajets == null || trajets.size() == 0) {
            return null;
        }
        else {
            return trajets.get(0).getEndTime();
        }
    }

    @Override
    public RideProvider getRideProvider() {
        return new JourneyRideProvider(trajets);
    }

    @Override
    public RideUser getRideUser() {
        return usingUser;
    }

    @Override
    public TransportMean getTransportMean() {
        return null;
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

        JSONArray jsonTrajets = new JSONArray();
        for(Ride ride : this.trajets) {
            jsonTrajets.put(ride.toJson());
        }
        jsonObject.put(JSON_KEY_TRAJETS, jsonTrajets);

        return jsonObject;
    }

    public static class JourneyRideProvider implements MultiRideProvider {

        private List<RideProvider> providers;

        public JourneyRideProvider() {
            this.providers = new ArrayList<>();
        }

        public JourneyRideProvider(List<Ride> trajets) {
            this();
            for(Ride r : trajets) {
                this.providers.add(r.getRideProvider());
            }
        }

        @Override
        public List<RideProvider> getProviders() {
            return providers;
        }

        @Override
        public void setProviders(List<RideProvider> providers) {
            this.providers = providers;
        }
    }
}
