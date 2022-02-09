package com.ecocovoit.ecocovoit.entities.parties;

import android.graphics.Bitmap;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.RemoteEntity;
import com.ecocovoit.ecocovoit.entities.RideProvider;
import com.ecocovoit.ecocovoit.entities.RideUser;
import com.ecocovoit.ecocovoit.entities.Transferable;
import com.ecocovoit.ecocovoit.entities.Utils;
import com.ecocovoit.ecocovoit.entities.transportmeans.Car;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "Users")
public class User implements RideUser, RideProvider, RemoteEntity, Transferable, LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "UsersId";

    @DatabaseField(columnName = FIELD_NAME)
    private String name;
    public static final String FIELD_NAME = "name";
    public static final String JSON_KEY_NAME = "name";

    @DatabaseField(columnName = FIELD_LOGIN)
    private String login;
    public static final String FIELD_LOGIN = "login";
    public static final String JSON_KEY_LOGIN = "login";

    @DatabaseField(columnName = FIELD_EMAIL)
    private String email;
    public static final String FIELD_EMAIL = "email";
    public static final String JSON_KEY_EMAIL = "email";

    @DatabaseField(columnName = FIELD_REMOTE_ID)
    private int remoteId;
    public static final String FIELD_REMOTE_ID = "remoteId";
    public static final String JSON_KEY_REMOTE_ID = "id";

    @DatabaseField(columnName = FIELD_BADGE)
    private UserBadge badge;
    public static final String FIELD_BADGE = "badge";
    public static final String JSON_KEY_BADGE = "badge";

    @DatabaseField(columnName = FIELD_STATUS)
    private UserStatus status;
    public static final String FIELD_STATUS = "status";
    public static final String JSON_KEY_STATUS = "status";

    @DatabaseField(columnName = FIELD_DESCRIPTION)
    private String description;
    public static final String FIELD_DESCRIPTION = "description";
    public static final String JSON_KEY_DESCRIPTION = "description";

    @DatabaseField(columnName = FIELD_SMOKER)
    private boolean smoker;
    public static final String FIELD_SMOKER = "smoker";
    public static final String JSON_KEY_SMOKER = "smoker";

    @DatabaseField(columnName = FIELD_PHOTO_STRING)
    private String photoString;
    public static final String FIELD_PHOTO_STRING = "photoString";
    public static final String JSON_KEY_PHOTO_STRING = "photoString";

    private Bitmap photo;

    private List<Car> cars;
    public static final String JSON_KEY_CARS = "cars";

    public User() {
        this.id = NO_ID;
        this.cars = new ArrayList<>();
    }

    public User(
            String name, String login, String email, int remoteId,
            UserBadge badge, UserStatus status,
            String description, boolean smoker, Bitmap photo, List<Car> cars) {
        this.id = NO_ID;
        this.name = name;
        this.login = login;
        this.email = email;
        this.remoteId = remoteId;
        this.badge = badge;
        this.status = status;
        this.description = description;
        this.smoker = smoker;
        this.cars = cars;

        this.photo = photo;
    }

    public User(JSONObject jsonObject) throws JSONException {
        this.id = NO_ID;
        this.remoteId = jsonObject.getInt(JSON_KEY_REMOTE_ID);
        this.name = jsonObject.getString(JSON_KEY_NAME);
        this.login = jsonObject.getString(JSON_KEY_LOGIN);
        this.email = jsonObject.getString(JSON_KEY_EMAIL);
        this.badge = UserBadge.valueOf(jsonObject.getString(JSON_KEY_BADGE));
        this.status = UserStatus.valueOf(jsonObject.getString(JSON_KEY_STATUS));
        this.description = jsonObject.getString(JSON_KEY_DESCRIPTION);
        this.smoker = jsonObject.getBoolean(JSON_KEY_SMOKER);
        this.photoString = jsonObject.getString(JSON_KEY_PHOTO_STRING);

        this.cars = new ArrayList<>();
        JSONArray array = jsonObject.getJSONArray(JSON_KEY_CARS);
        for(int i=0; i<array.length(); i++) {
            Car car = new Car(array.getJSONObject(i));
            this.cars.add(car);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int getRemoteId() {
        return this.remoteId;
    }

    public void setBadge(UserBadge badge) {
        this.badge = badge;
    }

    public UserBadge getBadge() {
        return badge;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
        this.photoString = Utils.getStringFromImage(photo);
    }

    public Bitmap getPhoto() {
        if(photo == null) {
            photo = Utils.getImageFromString(this.photoString);
        }
        return photo;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_KEY_REMOTE_ID, this.remoteId);
        jsonObject.put(JSON_KEY_NAME, this.name);
        jsonObject.put(JSON_KEY_LOGIN, this.login);
        jsonObject.put(JSON_KEY_EMAIL, this.email);
        jsonObject.put(JSON_KEY_BADGE, this.badge.name());
        jsonObject.put(JSON_KEY_STATUS, this.status.name());
        jsonObject.put(JSON_KEY_DESCRIPTION, this.description);
        jsonObject.put(JSON_KEY_SMOKER, this.smoker);
        jsonObject.put(JSON_KEY_PHOTO_STRING, this.photoString);

        JSONArray cars = new JSONArray();
        for(Car car : this.cars) {
            cars.put(car.toJson());
        }
        jsonObject.put(JSON_KEY_CARS, cars);

        return jsonObject;
    }
}
