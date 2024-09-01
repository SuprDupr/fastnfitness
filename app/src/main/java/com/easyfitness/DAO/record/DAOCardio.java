package com.easyfitness.DAO.record;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.easyfitness.DAO.Profile;
import com.easyfitness.DAO.record.domain.CardioRecord;
import com.easyfitness.DAO.record.domain.CardioTemplate;
import com.easyfitness.enums.ExerciseType;
import com.easyfitness.enums.ProgramRecordStatus;
import com.easyfitness.enums.RecordType;
import com.easyfitness.enums.WeightUnit;
import com.easyfitness.graph.GraphData;
import com.easyfitness.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOCardio extends DAORecord {

    public static final int DISTANCE_FCT = 0;
    public static final int DURATION_FCT = 1;
    public static final int SPEED_FCT = 2;
    public static final int MAX_DISTANCE_FCT = 4;

    public DAOCardio(Context context) {
        super(context);
        mContext = context;
    }

    public void addCardioRecordToFreeWorkout(final CardioRecord cardioRecord) {
        addRecordToFreeWorkout(
                cardioRecord.getDate(),
                cardioRecord.getMachine(),
                ExerciseType.CARDIO,
                0,
                0,
                0,
                WeightUnit.LBS,
                0,
                cardioRecord.getDistance(),
                cardioRecord.getDistanceUnit(),
                cardioRecord.getDuration(),
                "",
                cardioRecord.getProfileId());
    }

    public void addCardioTemplateToProgram(final CardioTemplate cardioTemplate) {
        addTemplateToProgram(
                cardioTemplate.getDate(),
                cardioTemplate.getExerciseName(),
                ExerciseType.CARDIO,
                0,
                0,
                0,
                WeightUnit.LBS,
                0,
                cardioTemplate.getDistance(),
                cardioTemplate.getDistanceUnit(),
                cardioTemplate.getDuration(),
                "",
                cardioTemplate.getProgramId(),
                cardioTemplate.getRestTime(),
                cardioTemplate.getTemplateOrder());
    }

    public List<GraphData> getFunctionRecords(
            final Profile pProfile,
            final String pMachine,
            final int pFunction) {

        String selectQuery = null;

        if (pFunction == DAOCardio.DISTANCE_FCT) {
            selectQuery = "SELECT SUM(" + DISTANCE + "), " + LOCAL_DATE + " FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOCardio.DURATION_FCT) {
            selectQuery = "SELECT SUM(" + DURATION + ") , " + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOCardio.SPEED_FCT) {
            selectQuery = "SELECT SUM(" + DISTANCE + ") / SUM(" + DURATION + ")," + LOCAL_DATE + " FROM "
                    + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " GROUP BY " + LOCAL_DATE
                    + " ORDER BY " + DATE_TIME + " ASC";
        } else if (pFunction == DAOCardio.MAX_DISTANCE_FCT) {
            selectQuery = "SELECT MAX(" + DISTANCE + ") , " + LOCAL_DATE + " FROM "
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

        // looping through all rows and adding to list
        if (mCursor.moveToFirst()) {

            do {
                final Date date = DateConverter.DBDateStrToDate(mCursor.getString(1));
                final GraphData value = new GraphData(DateConverter.nbDays(date),
                        mCursor.getDouble(0));
                valueList.add(value);

            } while (mCursor.moveToNext());
        }

        return valueList;
    }
}
