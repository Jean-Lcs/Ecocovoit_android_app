package com.ecocovoit.ecocovoit.entities.databaselinks;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.parties.User;
import com.ecocovoit.ecocovoit.entities.transportmeans.Car;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LinkCarUsers")
public class LinkCarUser implements LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "LinkCarUsersId";

    @DatabaseField(columnName = FIELD_CAR, foreign = true, foreignColumnName = Car.FIELD_ID)
    private Car car;
    public static final String FIELD_CAR = "car";

    @DatabaseField(columnName = FIELD_USER, foreign = true, foreignColumnName = User.FIELD_ID)
    private User user;
    public static final String FIELD_USER = "user";

    public LinkCarUser() { }

    public LinkCarUser(Car car, User user) {
        this.car = car;
        this.user = user;
    }

    @Override
    public int getId() {
        return id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
