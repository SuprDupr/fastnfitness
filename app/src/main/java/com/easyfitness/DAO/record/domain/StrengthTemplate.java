package com.easyfitness.DAO.record.domain;

import com.easyfitness.enums.WeightUnit;

import java.util.Date;

public class StrengthTemplate {

    private long programId;
    private Date date;
    private String exercise;
    private int sets;
    private int reps;
    private float weight;
    private WeightUnit weightUnit;
    private int restTime;
    private int templateOrder;

    public StrengthTemplate() {
    }

    public StrengthTemplate(
            final long programId,
            final Date date,
            final String exercise,
            final int sets,
            final int reps,
            final float weight,
            final WeightUnit weightUnit,
            final int restTime,
            final int templateOrder) {
        this.programId = programId;
        this.date = date;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.restTime = restTime;
        this.templateOrder = templateOrder;
    }

    public long getProgramId() {
        return programId;
    }

    public StrengthTemplate setProgramId(final long programId) {
        this.programId = programId;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public StrengthTemplate setDate(final Date date) {
        this.date = date;
        return this;
    }

    public String getExercise() {
        return exercise;
    }

    public StrengthTemplate setExercise(final String exercise) {
        this.exercise = exercise;
        return this;
    }

    public int getSets() {
        return sets;
    }

    public StrengthTemplate setSets(final int sets) {
        this.sets = sets;
        return this;
    }

    public int getReps() {
        return reps;
    }

    public StrengthTemplate setReps(final int reps) {
        this.reps = reps;
        return this;
    }

    public float getWeight() {
        return weight;
    }

    public StrengthTemplate setWeight(final float weight) {
        this.weight = weight;
        return this;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public StrengthTemplate setWeightUnit(final WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
        return this;
    }

    public int getRestTime() {
        return restTime;
    }

    public StrengthTemplate setRestTime(final int restTime) {
        this.restTime = restTime;
        return this;
    }

    public int getTemplateOrder() {
        return templateOrder;
    }

    public StrengthTemplate setTemplateOrder(final int templateOrder) {
        this.templateOrder = templateOrder;
        return this;
    }
}
