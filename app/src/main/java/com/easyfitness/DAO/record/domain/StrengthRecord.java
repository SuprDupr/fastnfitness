package com.easyfitness.DAO.record.domain;

import com.easyfitness.enums.WeightUnit;

import java.util.Date;


public class StrengthRecord {
    private Date date;
    private String exercise;
    private int sets;
    private int reps;
    private float weight;
    private WeightUnit weightUnit;
    private String notes;
    private long profileId;

    public StrengthRecord() {
    }

    public StrengthRecord(
            final Date date,
            final String exercise,
            final int sets,
            final int reps,
            final float weight,
            final WeightUnit weightUnit,
            final String notes,
            final long profileId) {
        this.date = date;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.notes = notes;
        this.profileId = profileId;
    }

    public Date getDate() {
        return date;
    }

    public StrengthRecord setDate(Date date) {
        this.date = date;
        return this;
    }

    public String getExercise() {
        return exercise;
    }

    public StrengthRecord setExercise(String exercise) {
        this.exercise = exercise;
        return this;
    }

    public int getSets() {
        return sets;
    }

    public StrengthRecord setSets(int sets) {
        this.sets = sets;
        return this;
    }

    public int getReps() {
        return reps;
    }

    public StrengthRecord setReps(int reps) {
        this.reps = reps;
        return this;
    }

    public float getWeight() {
        return weight;
    }

    public StrengthRecord setWeight(float weight) {
        this.weight = weight;
        return this;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public StrengthRecord setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public StrengthRecord setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public long getProfileId() {
        return profileId;
    }

    public StrengthRecord setProfileId(long profileId) {
        this.profileId = profileId;
        return this;
    }
}
