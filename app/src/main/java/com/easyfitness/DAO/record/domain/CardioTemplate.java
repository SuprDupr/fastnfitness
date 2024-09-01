package com.easyfitness.DAO.record.domain;

import com.easyfitness.enums.DistanceUnit;
import com.easyfitness.enums.WeightUnit;

import java.util.Date;

public class CardioTemplate {

    private Date date;
    private String exerciseName;
    private String exerciseType;
    private int sets;
    private int reps;
    private int weight;
    private WeightUnit weightUnit;
    private int seconds;
    private float distance;
    private DistanceUnit distanceUnit;
    private long duration;
    private String notes;
    private long programId;
    private int restTime;
    private int templateOrder;

    public CardioTemplate() {
    }

    public CardioTemplate(
            final Date date,
            final String exerciseName,
            final String exerciseType,
            final int sets,
            final int reps,
            final int weight,
            final WeightUnit weightUnit,
            final int seconds,
            final float distance,
            final DistanceUnit distanceUnit,
            final long duration,
            final String notes,
            final long programId,
            final int restTime,
            final int templateOrder) {
        this.date = date;
        this.exerciseName = exerciseName;
        this.exerciseType = exerciseType;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.seconds = seconds;
        this.distance = distance;
        this.distanceUnit = distanceUnit;
        this.duration = duration;
        this.notes = notes;
        this.programId = programId;
        this.restTime = restTime;
        this.templateOrder = templateOrder;
    }

    public Date getDate() {
        return date;
    }

    public CardioTemplate setDate(final Date date) {
        this.date = date;
        return this;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public CardioTemplate setExerciseName(final String exerciseName) {
        this.exerciseName = exerciseName;
        return this;
    }

    public String getExerciseType() {
        return exerciseType;
    }

    public CardioTemplate setExerciseType(final String exerciseType) {
        this.exerciseType = exerciseType;
        return this;
    }

    public int getSets() {
        return sets;
    }

    public CardioTemplate setSets(final int sets) {
        this.sets = sets;
        return this;
    }

    public int getReps() {
        return reps;
    }

    public CardioTemplate setReps(final int reps) {
        this.reps = reps;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public CardioTemplate setWeight(final int weight) {
        this.weight = weight;
        return this;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public CardioTemplate setWeightUnit(final WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
        return this;
    }

    public int getSeconds() {
        return seconds;
    }

    public CardioTemplate setSeconds(final int seconds) {
        this.seconds = seconds;
        return this;
    }

    public float getDistance() {
        return distance;
    }

    public CardioTemplate setDistance(final float distance) {
        this.distance = distance;
        return this;
    }

    public DistanceUnit getDistanceUnit() {
        return distanceUnit;
    }

    public CardioTemplate setDistanceUnit(final DistanceUnit distanceUnit) {
        this.distanceUnit = distanceUnit;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public CardioTemplate setDuration(final long duration) {
        this.duration = duration;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public CardioTemplate setNotes(final String notes) {
        this.notes = notes;
        return this;
    }

    public long getProgramId() {
        return programId;
    }

    public CardioTemplate setProgramId(final long programId) {
        this.programId = programId;
        return this;
    }

    public int getRestTime() {
        return restTime;
    }

    public CardioTemplate setRestTime(final int restTime) {
        this.restTime = restTime;
        return this;
    }

    public int getTemplateOrder() {
        return templateOrder;
    }

    public CardioTemplate setTemplateOrder(final int templateOrder) {
        this.templateOrder = templateOrder;
        return this;
    }
}
