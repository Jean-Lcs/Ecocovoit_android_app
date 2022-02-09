package com.ecocovoit.ecocovoit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ecocovoit.ecocovoit.entities.Ride;
import com.ecocovoit.ecocovoit.entities.databaselinks.LinkCarUser;
import com.ecocovoit.ecocovoit.entities.databaselinks.LinkCovoitRideJourney;
import com.ecocovoit.ecocovoit.entities.databaselinks.LinkLocationDirectionPath;
import com.ecocovoit.ecocovoit.entities.geo.DirectionPath;
import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.ecocovoit.ecocovoit.entities.parties.User;
import com.ecocovoit.ecocovoit.entities.rides.CarRide;
import com.ecocovoit.ecocovoit.entities.rides.Covoit;
import com.ecocovoit.ecocovoit.entities.rides.Journey;
import com.ecocovoit.ecocovoit.entities.transportmeans.Car;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends OrmLiteSqliteOpenHelper {

    private static final String databaseName = "EcoCovoit";
    private static final int databaseVersion = 0;

    private static final String TAG = "Error Database";

    public DatabaseManager(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Location.class);
            TableUtils.createTable(connectionSource, DirectionPath.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Car.class);
            TableUtils.createTable(connectionSource, CarRide.class);
            TableUtils.createTable(connectionSource, Covoit.class);
            TableUtils.createTable(connectionSource, Journey.class);
            TableUtils.createTable(connectionSource, LinkLocationDirectionPath.class);
            TableUtils.createTable(connectionSource, LinkCovoitRideJourney.class);
        }
        catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, LinkLocationDirectionPath.class, true);
            TableUtils.dropTable(connectionSource, LinkCovoitRideJourney.class, true);
            TableUtils.dropTable(connectionSource, Journey.class, true);
            TableUtils.dropTable(connectionSource, Covoit.class, true);
            TableUtils.dropTable(connectionSource, CarRide.class, true);
            TableUtils.dropTable(connectionSource, Car.class, true);
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, DirectionPath.class, true);
            TableUtils.dropTable(connectionSource, Location.class, true);

            onCreate(database);
        }
        catch (Exception e) {
            Log.e(TAG, "onUpgrade: ", e);
        }
    }

    //*********************** COVOIT
    public void createCovoit(Covoit covoit) {
        try {
            Dao<Covoit, Integer> dao = this.getDao(Covoit.class);
            dao.create(covoit);
        }
        catch (Exception e) {
            Log.e(TAG, "createCovoit : ", e);
        }
    }
    public void updateCovoit(Covoit covoit) {
        try {
            Dao<Covoit, Integer> dao = this.getDao(Covoit.class);
            dao.update(covoit);
        }
        catch (Exception e) {
            Log.e(TAG, "updateCovoit : ", e);
        }
    }
    public void deleteCovoit(Covoit covoit) {
        try {
            Dao<Covoit, Integer> dao = this.getDao(Covoit.class);
            dao.delete(covoit);
        }
        catch (Exception e) {
            Log.e(TAG, "deleteCovoit : ", e);
        }
    }
    public List<Covoit> getAllCovoits() {
        try {
            Dao<Covoit, Integer> dao = this.getDao(Covoit.class);
            return dao.queryForAll();
        }
        catch (Exception e) {
            Log.e(TAG, "getCovoits : ", e);
            return new ArrayList<>();
        }
    }
    public Covoit getCovoit(int id) {
        try {
            Dao<Covoit, Integer> dao = this.getDao(Covoit.class);
            return dao.queryForId(id);
        }
        catch (Exception e) {
            Log.e(TAG, "getCovoit : ", e);
            return null;
        }
    }

    //*********************** CAR RIDE
    public void createCarRide(CarRide carRide) {
        try {
            Dao<CarRide, Integer> dao = this.getDao(CarRide.class);
            dao.create(carRide);
        }
        catch (Exception e) {
            Log.e(TAG, "createCarRide : ", e);
        }
    }
    public void updateCarRide(CarRide carRide) {
        try {
            Dao<CarRide, Integer> dao = this.getDao(CarRide.class);
            dao.update(carRide);
        }
        catch (Exception e) {
            Log.e(TAG, "updateCarRide : ", e);
        }
    }
    public void deleteCarRide(CarRide carRide) {
        try {
            Dao<CarRide, Integer> dao = this.getDao(CarRide.class);
            dao.delete(carRide);
        }
        catch (Exception e) {
            Log.e(TAG, "deleteCarRide : ", e);
        }
    }
    public List<CarRide> getAllCarRides() {
        try {
            Dao<CarRide, Integer> dao = this.getDao(CarRide.class);
            return dao.queryForAll();
        }
        catch (Exception e) {
            Log.e(TAG, "getCarRides : ", e);
            return new ArrayList<>();
        }
    }
    public CarRide getCarRide(int id) {
        try {
            Dao<CarRide, Integer> dao = this.getDao(CarRide.class);
            return dao.queryForId(id);
        }
        catch (Exception e) {
            Log.e(TAG, "getCarRide : ", e);
            return null;
        }
    }

    //*********************** USER
    public void createUser(User user) {
        try {
            Dao<User, Integer> dao = this.getDao(User.class);
            dao.create(user);

            syncCarForUser(user);
        }
        catch (Exception e) {
            Log.e(TAG, "createUser : ", e);
        }
    }
    public void updateUser(User user) {
        try {
            Dao<User, Integer> dao = this.getDao(User.class);
            dao.update(user);

            syncCarForUser(user);
        }
        catch (Exception e) {
            Log.e(TAG, "updateUser : ", e);
        }
    }
    public void deleteUser(User user) {
        try {
            Dao<User, Integer> dao = this.getDao(User.class);
            dao.delete(user);

            deleteCarByUser(user);
        }
        catch (Exception e) {
            Log.e(TAG, "deleteUser : ", e);
        }
    }
    public List<User> getAllUsers() {
        try {
            Dao<User, Integer> dao = this.getDao(User.class);
            List<User> users = dao.queryForAll();
            for(User user : users) {
                fillUserWithCar(user);
            }
            return users;
        }
        catch (Exception e) {
            Log.e(TAG, "getUsers : ", e);
            return new ArrayList<>();
        }
    }
    public User getUser(int id) {
        try {
            Dao<User, Integer> dao = this.getDao(User.class);
            User user = dao.queryForId(id);
            fillUserWithCar(user);
            return user;
        }
        catch (Exception e) {
            Log.e(TAG, "getUser : ", e);
            return null;
        }
    }

    //*********************** CAR
    public void createCar(Car car) {
        try {
            Dao<Car, Integer> dao = this.getDao(Car.class);
            dao.create(car);
        }
        catch (Exception e) {
            Log.e(TAG, "createCar : ", e);
        }
    }
    public void updateCar(Car car) {
        try {
            Dao<Car, Integer> dao = this.getDao(Car.class);
            dao.update(car);
        }
        catch (Exception e) {
            Log.e(TAG, "updateCar : ", e);
        }
    }
    public void deleteCar(Car car) {
        try {
            Dao<Car, Integer> dao = this.getDao(Car.class);
            dao.delete(car);
        }
        catch (Exception e) {
            Log.e(TAG, "deleteCar : ", e);
        }
    }
    public List<Car> getAllCars() {
        try {
            Dao<Car, Integer> dao = this.getDao(Car.class);
            return dao.queryForAll();
        }
        catch (Exception e) {
            Log.e(TAG, "getCars : ", e);
            return new ArrayList<>();
        }
    }
    public Car getCar(int id) {
        try {
            Dao<Car, Integer> dao = this.getDao(Car.class);
            return dao.queryForId(id);
        }
        catch (Exception e) {
            Log.e(TAG, "getCar : ", e);
            return null;
        }
    }

    //*********************** DIRECTION PATH
    public void createDirectionPath(DirectionPath directionPath) {
        try {
            Dao<DirectionPath, Integer> dao = this.getDao(DirectionPath.class);
            dao.create(directionPath);

            this.syncLocationForDirectionPath(directionPath);
        }
        catch (Exception e) {
            Log.e(TAG, "createDirectionPath : ", e);
        }
    }
    public void updateDirectionPath(DirectionPath directionPath) {
        try {
            Dao<DirectionPath, Integer> dao = this.getDao(DirectionPath.class);
            dao.update(directionPath);

            this.syncLocationForDirectionPath(directionPath);
        }
        catch (Exception e) {
            Log.e(TAG, "updateDirectionPath : ", e);
        }
    }
    public void deleteDirectionPath(DirectionPath directionPath) {
        try {
            Dao<DirectionPath, Integer> dao = this.getDao(DirectionPath.class);
            dao.delete(directionPath);

            this.deleteLocationByDirectionPath(directionPath);
        }
        catch (Exception e) {
            Log.e(TAG, "deleteDirectionPath : ", e);
        }
    }
    public List<DirectionPath> getAllDirectionPaths() {
        try {
            Dao<DirectionPath, Integer> dao = this.getDao(DirectionPath.class);
            List<DirectionPath> paths = dao.queryForAll();
            for (DirectionPath path : paths) {
                fillDirectionPathWithLocation(path);
            }
            return paths;
        }
        catch (Exception e) {
            Log.e(TAG, "getAllDirectionPaths : ", e);
            return new ArrayList<>();
        }
    }
    public DirectionPath getDirectionPath(int id) {
        try {
            Dao<DirectionPath, Integer> dao = this.getDao(DirectionPath.class);
            DirectionPath path = dao.queryForId(id);

            fillDirectionPathWithLocation(path);
            return path;
        }
        catch (Exception e) {
            Log.e(TAG, "getDirectionPath : ", e);
            return null;
        }
    }

    //*********************** JOURNEY
    public void createJourney(Journey journey) {
        try {
            Dao<Journey, Integer> dao = this.getDao(Journey.class);
            dao.create(journey);

            this.syncCovoitForJourney(journey);
        }
        catch (Exception e) {
            Log.e(TAG, "createJourney : ", e);
        }
    }
    public void updateJourney(Journey journey) {
        try {
            Dao<Journey, Integer> dao = this.getDao(Journey.class);
            dao.update(journey);

            this.syncCovoitForJourney(journey);
        }
        catch (Exception e) {
            Log.e(TAG, "updateJourney : ", e);
        }
    }
    public void deleteJourney(Journey journey) {
        try {
            Dao<Journey, Integer> dao = this.getDao(Journey.class);
            dao.delete(journey);

            this.deleteCovoitByJourney(journey);
        }
        catch (Exception e) {
            Log.e(TAG, "deleteJourney : ", e);
        }
    }
    public List<Journey> getAllJourneys() {
        try {
            Dao<Journey, Integer> dao = this.getDao(Journey.class);
            List<Journey> journeys = dao.queryForAll();
            for (Journey journey : journeys) {
                fillJourneyWithCovoit(journey);
            }
            return journeys;
        }
        catch (Exception e) {
            Log.e(TAG, "getAllDirectionPaths : ", e);
            return new ArrayList<>();
        }
    }
    public Journey getJourney(int id) {
        try {
            Dao<Journey, Integer> dao = this.getDao(Journey.class);
            Journey journey = dao.queryForId(id);

            fillJourneyWithCovoit(journey);
            return journey;
        }
        catch (Exception e) {
            Log.e(TAG, "getJourney : ", e);
            return null;
        }
    }

    //*********************** LINK COVOIT RIDE WITH JOURNEY
    private void syncCovoitForJourney(Journey journey) {
        try {
            Dao<LinkCovoitRideJourney, Integer> dao = this.getDao(LinkCovoitRideJourney.class);

            this.deleteCovoitByJourney(journey);

            for(Ride ride : journey.getTrajets()) {
                if(ride instanceof Covoit) {
                    LinkCovoitRideJourney link = new LinkCovoitRideJourney((Covoit)ride, journey);
                    dao.update(link);
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "syncCovoitForJourney: ", e);
        }
    }

    private void deleteCovoitByJourney(Journey journey) {
        try {
            Dao<LinkCovoitRideJourney, Integer> dao = this.getDao(LinkCovoitRideJourney.class);
            for(Ride ride : journey.getTrajets()) {
                if(ride instanceof Covoit) {
                    LinkCovoitRideJourney link = new LinkCovoitRideJourney((Covoit)ride, journey);
                    dao.delete(link);
                }
            }
        }
        catch (Exception e) {
            Log.e(TAG, "deleteCovoitByJourney: ", e);
        }
    }

    private void fillJourneyWithCovoit(Journey journey) {
        try {
            Dao<LinkCovoitRideJourney, Integer> dao = this.getDao(LinkCovoitRideJourney.class);
            List<LinkCovoitRideJourney> links = dao.queryForEq(
                    LinkCovoitRideJourney.FIELD_JOURNEY,
                    journey
            );
            List<Covoit> covoits = new ArrayList<>();
            for (LinkCovoitRideJourney link : links) {
                covoits.add(link.getCovoit());
            }
            journey.getTrajets().addAll(covoits);
        }
        catch (Exception e) {
            Log.e(TAG, "fillJourneyWithCovoit: ", e);
        }
    }

    //*********************** LINK LOCATION WITH DIRECTION PATH
    private void syncLocationForDirectionPath(DirectionPath directionPath) {
        try {
            Dao<LinkLocationDirectionPath, Integer> dao = this.getDao(LinkLocationDirectionPath.class);

            this.deleteLocationByDirectionPath(directionPath);

            for(Location loc : directionPath.getPath()) {
                LinkLocationDirectionPath link = new LinkLocationDirectionPath(loc, directionPath);
                dao.update(link);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "createLinkLocationForDirectionPath: ", e);
        }
    }

    private void deleteLocationByDirectionPath(DirectionPath directionPath) {
        try {
            Dao<LinkLocationDirectionPath, Integer> dao = this.getDao(LinkLocationDirectionPath.class);
            for(Location loc : directionPath.getPath()) {
                LinkLocationDirectionPath link = new LinkLocationDirectionPath(loc, directionPath);
                dao.delete(link);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "createLinkLocationForDirectionPath: ", e);
        }
    }

    private void fillDirectionPathWithLocation(DirectionPath directionPath) {
        try {
            Dao<LinkLocationDirectionPath, Integer> dao = this.getDao(LinkLocationDirectionPath.class);
            List<LinkLocationDirectionPath> links = dao.queryForEq(
                    LinkLocationDirectionPath.FIELD_DIRECTION_PATH,
                    directionPath
            );
            List<Location> path = new ArrayList<>();
            for (LinkLocationDirectionPath link : links) {
                path.add(link.getLocation());
            }
            directionPath.setPath(path);
        }
        catch (Exception e) {
            Log.e(TAG, "fillDirectionPathWithLocation: ", e);
        }
    }

    //*********************** LINK CAR WITH USER
    private void syncCarForUser(User user) {
        try {
            Dao<LinkCarUser, Integer> dao = this.getDao(LinkCarUser.class);

            this.deleteCarByUser(user);

            for(Car car : user.getCars()) {
                LinkCarUser link = new LinkCarUser(car, user);
                dao.update(link);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "syncCarForUser: ", e);
        }
    }

    private void deleteCarByUser(User user) {
        try {
            Dao<LinkCarUser, Integer> dao = this.getDao(LinkCarUser.class);
            for(Car car : user.getCars()) {
                LinkCarUser link = new LinkCarUser(car, user);
                dao.delete(link);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "deleteCarByUser: ", e);
        }
    }

    private void fillUserWithCar(User user) {
        try {
            Dao<LinkCarUser, Integer> dao = this.getDao(LinkCarUser.class);
            List<LinkCarUser> links = dao.queryForEq(
                    LinkCarUser.FIELD_USER,
                    user
            );
            List<Car> cars = new ArrayList<>();
            for (LinkCarUser link : links) {
                cars.add(link.getCar());
            }
            user.setCars(cars);
        }
        catch (Exception e) {
            Log.e(TAG, "fillUserWithCar: ", e);
        }
    }

}
