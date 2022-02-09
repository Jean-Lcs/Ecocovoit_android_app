package com.ecocovoit.ecocovoit.entities.databaselinks;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.geo.DirectionPath;
import com.ecocovoit.ecocovoit.entities.geo.Location;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LinkLocationDirectionPaths")
public class LinkLocationDirectionPath implements LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "LinkLocationDirectionPathsId";

    @DatabaseField(columnName = FIELD_LOCATION, foreign = true, foreignColumnName = Location.FIELD_ID, foreignAutoCreate = true)
    private Location location;
    public static final String FIELD_LOCATION = "location";

    @DatabaseField(columnName = FIELD_DIRECTION_PATH, foreign = true, foreignColumnName = DirectionPath.FIELD_ID)
    private DirectionPath directionPath;
    public static final String FIELD_DIRECTION_PATH = "directionPath";

    public LinkLocationDirectionPath() { }

    public LinkLocationDirectionPath(Location location, DirectionPath directionPath) {
        this.location = location;
        this.directionPath = directionPath;
    }

    @Override
    public int getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public DirectionPath getDirectionPath() {
        return directionPath;
    }

    public void setDirectionPath(DirectionPath directionPath) {
        this.directionPath = directionPath;
    }
}
