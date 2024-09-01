package com.easyfitness.DAO.record;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.easyfitness.DAO.DAOMachine;
import com.easyfitness.DAO.Machine;
import com.easyfitness.DAO.Profile;
import com.easyfitness.DAO.Weight;
import com.easyfitness.DAO.record.domain.StrengthRecord;
import com.easyfitness.DAO.record.domain.StrengthTemplate;
import com.easyfitness.enums.DistanceUnit;
import com.easyfitness.enums.ExerciseType;
import com.easyfitness.enums.ProgramRecordStatus;
import com.easyfitness.enums.RecordType;
import com.easyfitness.enums.WeightUnit;
import com.easyfitness.graph.GraphData;
import com.easyfitness.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOFonte extends DAORecord {

    public static final int SUM_FCT = 0;
    public static final int MAX1_FCT = 1;
    public static final int MAX5_FCT = 2;
    public static final int NBSERIE_FCT = 3;
    public static final int TOTAL_REP_FCT = 4;
    public static final int MAX_REP_FCT = 5;
    public static final int ONEREPMAX_FCT = 6;

    public DAOFonte(final Context context) {
        super(context);
    }

    public void addStrengthRecordToFreeWorkout(final StrengthRecord strengthRecord) {
        addRecordToFreeWorkout(
                strengthRecord.getDate(),
                strengthRecord.getExercise(),
                ExerciseType.STRENGTH,
                strengthRecord.getSets(),
                strengthRecord.getReps(),
                strengthRecord.getWeight(),
                strengthRecord.getWeightUnit(),
                0,
                0,
                DistanceUnit.KM,
                0,
                strengthRecord.getNotes(),
                strengthRecord.getProfileId()
        );
    }

    public void addStrengthTemplateToProgram(final StrengthTemplate strengthTemplate) {
        addTemplateToProgram(
                strengthTemplate.getDate(),
                strengthTemplate.getExercise(),
                ExerciseType.STRENGTH,
                strengthTemplate.getSets(),
                strengthTemplate.getReps(),
                strengthTemplate.getWeight(),
                strengthTemplate.getWeightUnit(),
                0,
                0,
                DistanceUnit.KM,
                0,
                "",
                strengthTemplate.getProgramId(),
                strengthTemplate.getRestTime(),
                strengthTemplate.getTemplateOrder()
        );
    }

    public List<GraphData> getBodyBuildingFunctionRecords(
            final Profile pProfile,
            final String pMachine,
            final int pFunction) {

        String selectQuery = null;

        // TODO Weight unit are not considered here yet.
        if (pFunction == DAOFonte.SUM_FCT) {
            selectQuery = "SELECT SUM(" + SETS + "*" + REPS + "*"
                    + WEIGHT + "), " + LOCAL_DATE + " FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOFonte.MAX5_FCT) {
            selectQuery = "SELECT MAX(" + WEIGHT + ") , " + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + REPS + ">=5"
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOFonte.MAX1_FCT) {
            selectQuery = "SELECT MAX(" + WEIGHT + ") , " + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + REPS + ">=1"
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOFonte.NBSERIE_FCT) {
            selectQuery = "SELECT count(" + KEY + ") , " + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOFonte.ONEREPMAX_FCT) {
            //https://en.wikipedia.org/wiki/One-repetition_maximum#Brzycki
            selectQuery = "SELECT MAX(" + WEIGHT + " * (36.0 / (37.0 - " + REPS + "))) , " + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + REPS + "<=10"
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOFonte.TOTAL_REP_FCT) {
            selectQuery = "SELECT SUM(" + SETS + "*" + REPS + "), " + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOFonte.MAX_REP_FCT) {
            selectQuery = "SELECT MAX(" + REPS + ") , " + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        }

        final List<GraphData> valueList = new ArrayList<>();
        final SQLiteDatabase db = this.getReadableDatabase();

        mCursor = null;
        mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            do {
                final Date date = DateConverter.DBDateStrToDate(mCursor.getString(1));
                final GraphData value = new GraphData(DateConverter.nbDays(date), mCursor.getDouble(0));
                valueList.add(value);

            } while (mCursor.moveToNext());
        }

        return valueList;
    }

    /**
     * @return the number of sets for this machine for this day
     */
    public int getSets(final Date pDate, final String pMachine, final Profile pProfile) {

        if (pProfile == null) return 0;
        int lReturn;

        final DAOMachine lDAOMachine = new DAOMachine(mContext);
        final long machine_key = lDAOMachine.getMachine(pMachine).getId();

        final String lDate = DateConverter.dateToDBDateStr(pDate);

        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;

        final String selectQuery = "SELECT SUM(" + SETS + ") FROM " + TABLE_NAME
                + " WHERE " + LOCAL_DATE + "=\"" + lDate + "\" AND " + EXERCISE_KEY + "=" + machine_key
                + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal();
        mCursor = db.rawQuery(selectQuery, null);

        mCursor.moveToFirst();

        try {
            lReturn = mCursor.getInt(0);
        } catch (final NumberFormatException e) {
            lReturn = 0; // Return une valeur
        }

        close();

        return lReturn;
    }

    /**
     * @return the total weight for this machine for this day
     */
    public float getTotalWeightMachine(
            final Date pDate,
            final String pMachine,
            final Profile pProfile) {

        if (pProfile == null) return 0;
        float lReturn = 0;

        final DAOMachine lDAOMachine = new DAOMachine(mContext);
        final long machine_key = lDAOMachine.getMachine(pMachine).getId();

        final String lDate = DateConverter.dateToDBDateStr(pDate);

        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;

        final String selectQuery = "SELECT " + SETS + ", " + WEIGHT + ", " + REPS + " FROM " + TABLE_NAME
                + " WHERE " + LOCAL_DATE + "=\"" + lDate + "\" AND " + EXERCISE_KEY + "=" + machine_key
                + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal();
        mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            do {
                final float value = mCursor.getInt(0) * mCursor.getFloat(1) * mCursor.getInt(2);
                lReturn += value;
            } while (mCursor.moveToNext());
        }
        close();

        return lReturn;
    }


    /**
     * @return the total weight for this day
     */
    public float getTotalWeightSession(final Date pDate, final Profile pProfile) {

        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;
        float lReturn = 0;

        final String lDate = DateConverter.dateToDBDateStr(pDate);

        final String selectQuery = "SELECT " + SETS + ", " + WEIGHT + ", " + REPS + " FROM " + TABLE_NAME
                + " WHERE " + LOCAL_DATE + "=\"" + lDate + "\""
                + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                + " AND " + TEMPLATE_RECORD_STATUS + "<" + ProgramRecordStatus.PENDING.ordinal()
                + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal();
        mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            do {
                final float value = mCursor.getInt(0) * mCursor.getFloat(1) * mCursor.getInt(2);
                lReturn += value;
            } while (mCursor.moveToNext());
        }
        close();

        return lReturn;
    }

    /**
     * @return Max weight for a profile p and a machine m
     */
    public Weight getMax(final Profile p, final Machine m) {

        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;
        Weight weight = null;

        final String selectQuery = "SELECT MAX(" + WEIGHT + "), " + WEIGHT_UNIT + " FROM " + TABLE_NAME
                + " WHERE " + PROFILE_KEY + "=" + p.getId() + " AND " + EXERCISE_KEY + "=" + m.getId()
                + " AND ( " + TEMPLATE_RECORD_STATUS + "<" + ProgramRecordStatus.SUCCESS.ordinal()
                + " OR " + TEMPLATE_RECORD_STATUS + "=" + ProgramRecordStatus.NONE.ordinal() + ")"
                + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal();
        mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            weight = new Weight(mCursor.getFloat(0), WeightUnit.fromInteger(mCursor.getInt(1)));
        }
        close();

        return weight;
    }

    /**
     * @return Min weight for a profile p and a machine m
     */
    public Weight getMin(final Profile p, final Machine m) {

        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;
        Weight weight = null;

        final String selectQuery = "SELECT MIN(" + WEIGHT + "), " + WEIGHT_UNIT + " FROM " + TABLE_NAME
                + " WHERE " + PROFILE_KEY + "=" + p.getId() + " AND " + EXERCISE_KEY + "=" + m.getId()
                + " AND ( " + TEMPLATE_RECORD_STATUS + "<" + ProgramRecordStatus.SUCCESS.ordinal()
                + " OR " + TEMPLATE_RECORD_STATUS + "=" + ProgramRecordStatus.NONE.ordinal() + ")"
                + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal();
        mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            weight = new Weight(mCursor.getFloat(0), WeightUnit.fromInteger(mCursor.getInt(1)));
        }
        close();

        return weight;
    }
}
