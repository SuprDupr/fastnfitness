package com.easyfitness.DAO.record.domain;

import com.easyfitness.enums.DistanceUnit;

import java.util.Date;

public class CardioRecord {

    private Date date;
    private String machine;
    private float distance;
    private long duration;
    private long profileId;
    private DistanceUnit distanceUnit;

    public CardioRecord() {
    }

    public CardioRecord(final Date date, final String machine, final float distance, final long duration, final long profileId, final DistanceUnit distanceUnit) {
        this.date = date;
        this.machine = machine;
        this.distance = distance;
        this.duration = duration;
        this.profileId = profileId;
        this.distanceUnit = distanceUnit;
    }

    public Date getDate() {
        return date;
    }

    public CardioRecord setDate(final Date date) {
        this.date = date;
        return this;
    }

    public String getMachine() {
        return machine;
    }

    public CardioRecord setMachine(final String machine) {
        this.machine = machine;
        return this;
    }

    public float getDistance() {
        return distance;
    }

    public CardioRecord setDistance(final float distance) {
        this.distance = distance;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public CardioRecord setDuration(final long duration) {
        this.duration = duration;
        return this;
    }

    public long getProfileId() {
        return profileId;
    }

    public CardioRecord setProfileId(final long profileId) {
        this.profileId = profileId;
        return this;
    }

    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }

    public CardioRecord setDistanceUnit(final DistanceUnit distanceUnit) {
        this.distanceUnit = distanceUnit;
        return this;
    }
}
