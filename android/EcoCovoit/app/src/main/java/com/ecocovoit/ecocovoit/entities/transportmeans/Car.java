package com.ecocovoit.ecocovoit.entities.transportmeans;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.RemoteEntity;
import com.ecocovoit.ecocovoit.entities.TransportMean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONException;
import org.json.JSONObject;

@DatabaseTable(tableName = "Cars")
public class Car implements TransportMean, RemoteEntity, LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "CarsId";

    @DatabaseField(columnName = FIELD_NAME)
    private String name;
    public static final String FIELD_NAME = "name";
    public static final String JSON_KEY_NAME = "name";

    @DatabaseField(columnName = FIELD_REMOTE_ID)
    private int remoteId;
    public static final String FIELD_REMOTE_ID = "remoteId";
    public static final String JSON_KEY_REMOTE_ID = "id";

    public Car() {
        this.id = NO_ID;
    }

    public Car(String name) {
        this.name = name;
    }

    public Car(String name, int remoteId) {
        this.id = NO_ID;
        this.name = name;
        this.remoteId = remoteId;
    }

    public Car(JSONObject jsonObject) throws JSONException {
        this.id = NO_ID;
        this.name = jsonObject.getString(JSON_KEY_NAME);
        this.remoteId = jsonObject.getInt(JSON_KEY_REMOTE_ID);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getRemoteId() {
        return remoteId;
    }

    public String getName() {
        return name;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_NAME, this.name);
        jsonObject.put(JSON_KEY_REMOTE_ID, this.remoteId);

        return jsonObject;
    }

    @Override
    public int getCarbonFootPrint() {
        return 0;
    }
}
