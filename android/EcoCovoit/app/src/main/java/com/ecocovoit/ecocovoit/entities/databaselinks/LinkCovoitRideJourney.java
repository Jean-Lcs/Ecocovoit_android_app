package com.ecocovoit.ecocovoit.entities.databaselinks;

import com.ecocovoit.ecocovoit.entities.LocalEntity;
import com.ecocovoit.ecocovoit.entities.rides.Covoit;
import com.ecocovoit.ecocovoit.entities.rides.Journey;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LinkCovoitRideJourneys")
public class LinkCovoitRideJourney implements LocalEntity {

    @DatabaseField(columnName = FIELD_ID, generatedId = true, canBeNull = false, unique = true)
    private int id;
    public static final String FIELD_ID = "LinkCovoitRideJourneysId";

    @DatabaseField(columnName = FIELD_COVOIT, foreign = true, foreignColumnName = Covoit.FIELD_ID)
    private Covoit covoit;
    public static final String FIELD_COVOIT = "covoit";

    @DatabaseField(columnName = FIELD_JOURNEY, foreign = true, foreignColumnName = Journey.FIELD_ID)
    private Journey journey;
    public static final String FIELD_JOURNEY = "journey";

    public LinkCovoitRideJourney() {}

    public LinkCovoitRideJourney(Covoit covoit, Journey journey) {
        this.covoit = covoit;
        this.journey = journey;
    }

    @Override
    public int getId() {
        return id;
    }

    public Covoit getCovoit() {
        return covoit;
    }

    public void setCovoit(Covoit covoit) {
        this.covoit = covoit;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }
}
