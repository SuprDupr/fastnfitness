package com.easyfitness.DAO.record;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.easyfitness.DAO.DAOBase;
import com.easyfitness.DAO.DAOMachine;
import com.easyfitness.DAO.Machine;
import com.easyfitness.DAO.Profile;
import com.easyfitness.R;
import com.easyfitness.enums.DistanceUnit;
import com.easyfitness.enums.ExerciseType;
import com.easyfitness.enums.ProgramRecordStatus;
import com.easyfitness.enums.RecordType;
import com.easyfitness.enums.WeightUnit;
import com.easyfitness.utils.DateConverter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAORecord extends DAOBase {

    // Contacts table name
    public static final String TABLE_NAME = "EFfontes";

    public static final String KEY = "_id";
    public static final String DATE = "date";
    public static final String LOCAL_DATE = "DATE(date || 'T' || time, 'localtime')";
    public static final String TIME = "time";
    public static final String DATE_TIME = "DATETIME(date || 'T' || time)";
    public static final String EXERCISE = "machine";
    public static final String PROFILE_KEY = "profil_id";
    public static final String EXERCISE_KEY = "machine_id";
    public static final String NOTES = "notes";
    public static final String EXERCISE_TYPE = "type";

    // Specific to BodyBuilding
    public static final String SETS = "serie";
    public static final String REPS = "repetition";
    public static final String WEIGHT = "poids";
    public static final String WEIGHT_UNIT = "unit"; // 0:kg 1:lbs

    // Specific to Cardio
    public static final String DISTANCE = "distance";
    public static final String DURATION = "duration";
    public static final String DISTANCE_UNIT = "distance_unit"; // 0:km 1:mi

    // Specific to STATIC
    public static final String SECONDS = "seconds";

    // For Workout Templates
    public static final String RECORD_TYPE = "RECORD_TYPE";
    public static final String PROGRAM_KEY = "TEMPLATE_KEY"; // ID of the Program. A program is made of several templated exercises : "templates"
    public static final String TEMPLATE_RECORD_KEY = "TEMPLATE_RECORD_KEY"; // ID of the exercise template
    public static final String PROGRAM_SESSION_KEY = "TEMPLATE_SESSION_KEY"; // ID of the running program session
    public static final String TEMPLATE_ORDER = "TEMPLATE_ORDER"; // order of the exercise in the program
    public static final String TEMPLATE_REST_TIME = "TEMPLATE_SECONDS"; // rest time after exercise is done. SQLite column to be renamed one day.

    public static final String TEMPLATE_RECORD_STATUS = "TEMPLATE_RECORD_STATUS"; // SUCCESS, FAILED or PENDING

    // Duplicating all record parameters to avoid wrong behavior on old records when a program is updated. Example: an exercise was said successful but after program update the exercise is considered failed because one value is different.
    // Specific to BodyBuilding
    public static final String TEMPLATE_SETS = "TEMPLATE_SETS";
    public static final String TEMPLATE_REPS = "TEMPLATE_REPS";
    public static final String TEMPLATE_WEIGHT = "TEMPLATE_WEIGHT";
    public static final String TEMPLATE_WEIGHT_UNIT = "TEMPLATE_WEIGHT_UNIT"; // 0:kg 1:lbs
    // Specific to Cardio
    public static final String TEMPLATE_DISTANCE = "TEMPLATE_DISTANCE";
    public static final String TEMPLATE_DURATION = "TEMPLATE_DURATION";
    public static final String TEMPLATE_DISTANCE_UNIT = "TEMPLATE_DISTANCE_UNIT"; // 0:km 1:mi
    // Specific to STATIC
    public static final String TEMPLATE_SECONDS = "TEMPLATE_STATIC_SECONDS";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PROFILE_KEY + " INTEGER, "
            + EXERCISE_KEY + " INTEGER,"
            + DATE + " DATE, "
            + TIME + " TEXT,"
            + EXERCISE + " TEXT, "
            + SETS + " INTEGER, "
            + REPS + " INTEGER, "
            + WEIGHT + " REAL, "
            + WEIGHT_UNIT + " INTEGER, "
            + NOTES + " TEXT, "
            + DISTANCE + " REAL, "
            + DURATION + " TEXT, "
            + EXERCISE_TYPE + " INTEGER, "
            + SECONDS + " INTEGER, "
            + DISTANCE_UNIT + " INTEGER,"
            + PROGRAM_KEY + " INTEGER,"
            + TEMPLATE_RECORD_KEY + " INTEGER,"
            + PROGRAM_SESSION_KEY + " INTEGER,"
            + TEMPLATE_ORDER + " INTEGER,"
            + TEMPLATE_REST_TIME + " INTEGER,"
            + TEMPLATE_RECORD_STATUS + " INTEGER,"
            + RECORD_TYPE + " INTEGER,"

            + TEMPLATE_SETS + " INTEGER, "
            + TEMPLATE_REPS + " INTEGER, "
            + TEMPLATE_WEIGHT + " REAL, "
            + TEMPLATE_WEIGHT_UNIT + " INTEGER, "
            + TEMPLATE_DISTANCE + " REAL, "
            + TEMPLATE_DISTANCE_UNIT + " INTEGER,"
            + TEMPLATE_DURATION + " TEXT, "
            + TEMPLATE_SECONDS + " INTEGER"
            + " );";

    protected Profile mProfile = null;
    protected Cursor mCursor = null;
    protected Context mContext;

    public DAORecord(final Context context) {
        super(context);
        mContext = context;
    }

    public void setProfile(final Profile pProfile) {
        mProfile = pProfile;
    }

    public int getCount() {
        final int count;
        final String countQuery = "SELECT " + KEY + " FROM " + TABLE_NAME;

        try (final SQLiteDatabase db = open()) {
            final Cursor cursor = db.rawQuery(countQuery, null);
            count = cursor.getCount();
            cursor.close();
        } finally {
            close();
        }

        return count;
    }

    public void addRecord(final Record record) {
        addRecord(record.getDate(),
                record.getExercise(), record.getExerciseType(),
                record.getSets(),
                record.getReps(),
                record.getWeightInKg(),
                record.getWeightUnit(),
                record.getNote(),
                record.getDistanceInKm(),
                record.getDistanceUnit(),
                record.getDuration(),
                record.getSeconds(),
                record.getProfileId(),
                record.getRecordType(),
                record.getTemplateRecordId(),
                record.getProgramId(),
                record.getTemplateSessionId(),
                record.getTemplateRestTime(),
                record.getProgramRecordStatus(),
                record.getTemplateSets(),
                record.getTemplateReps(),
                record.getTemplateWeight(),
                record.getTemplateWeightUnit(),
                record.getTemplateDistance(),
                record.getTemplateDistanceUnit(),
                record.getTemplateDuration(),
                record.getTemplateSeconds(),
                record.getTemplateOrder());
    }

    public long addTemplateToProgram(Date pDate, String pExercise, ExerciseType pExerciseType,
                                     int pSets, int pReps, float pWeight,
                                     WeightUnit pWeightUnit, int pSeconds, float pDistance, DistanceUnit pDistanceUnit,
                                     long pDuration, String pNote,
                                     long pProgramId, int restTime, int templateOrder) {
        return addRecord(pDate, pExercise, pExerciseType, pSets, pReps, pWeight, pWeightUnit, pNote, pDistance, pDistanceUnit, pDuration, pSeconds, -1,
                RecordType.PROGRAM_TEMPLATE, -1, pProgramId, -1, restTime, ProgramRecordStatus.NONE, pSets, pReps, pWeight,
                pWeightUnit, pDistance, pDistanceUnit, pDuration, pSeconds, templateOrder);
    }

    public long addRecordToFreeWorkout(Date pDate, String pExercise, ExerciseType pExerciseType,
                                       int pSets, int pReps, float pWeight,
                                       WeightUnit pUnit, int pSeconds, float pDistance, DistanceUnit pDistanceUnit,
                                       long pDuration, String pNote, long pProfileId) {
        return addRecord(pDate, pExercise, pExerciseType, pSets, pReps, pWeight, pUnit, pNote, pDistance, pDistanceUnit, pDuration, pSeconds, pProfileId,
                RecordType.FREE_RECORD, -1, -1, -1, 0, ProgramRecordStatus.NONE, 0, 0, 0,
                WeightUnit.KG, 0, DistanceUnit.KM, 0, 0, -1);
    }

    public long addRecord(Date pDate, String pExercise, ExerciseType pExerciseType, int pSets,
                          int pReps, float pWeight,
                          WeightUnit pWeightUnit, String pNote, float pDistance, DistanceUnit pDistanceUnit,
                          long pDuration, int pSeconds, long pProfileId,
                          RecordType pRecordType, long pTemplateRecordId, long pProgramId, long pProgramSessionId,
                          int pRestTime, ProgramRecordStatus pProgramRecordStatus, int templateSets, int templateReps,
                          float templateWeight,
                          WeightUnit templateWeightUnit, float templateDistance, DistanceUnit templateDistanceUnit,
                          long templateDuration, int templateSeconds, int templateOrder) {

        final ContentValues value = new ContentValues();
        final long machine_key;

        //Test if Machine exists. If not create it.
        final DAOMachine lDAOMachine = new DAOMachine(mContext);
        if (!lDAOMachine.machineExists(pExercise)) {
            machine_key = lDAOMachine.addMachine(pExercise, "", pExerciseType, "", false, "");
        } else {
            machine_key = lDAOMachine.getMachine(pExercise).getId();
        }

        value.put(DAORecord.DATE, DateConverter.dateTimeToDBDateStr(pDate));
        value.put(DAORecord.TIME, DateConverter.dateTimeToDBTimeStr(pDate));
        value.put(DAORecord.EXERCISE, pExercise);
        value.put(DAORecord.EXERCISE_KEY, machine_key);
        value.put(DAORecord.EXERCISE_TYPE, pExerciseType.ordinal());
        value.put(DAORecord.PROFILE_KEY, pProfileId);
        value.put(DAORecord.SETS, pSets);
        value.put(DAORecord.REPS, pReps);
        value.put(DAORecord.WEIGHT, pWeight);
        value.put(DAORecord.WEIGHT_UNIT, pWeightUnit.ordinal());
        value.put(DAORecord.DISTANCE, pDistance);
        value.put(DAORecord.DISTANCE_UNIT, pDistanceUnit.ordinal());
        value.put(DAORecord.DURATION, pDuration);
        value.put(DAORecord.SECONDS, pSeconds);
        value.put(DAORecord.NOTES, pNote);
        value.put(DAORecord.PROGRAM_KEY, pProgramId);
        value.put(DAORecord.TEMPLATE_RECORD_KEY, pTemplateRecordId);
        value.put(DAORecord.PROGRAM_SESSION_KEY, pProgramSessionId);
        value.put(DAORecord.TEMPLATE_REST_TIME, pRestTime);
        value.put(DAORecord.TEMPLATE_ORDER, templateOrder);
        value.put(DAORecord.RECORD_TYPE, pRecordType.ordinal());
        value.put(DAORecord.TEMPLATE_RECORD_STATUS, pProgramRecordStatus.ordinal());
        value.put(DAORecord.TEMPLATE_SETS, templateSets);
        value.put(DAORecord.TEMPLATE_REPS, templateReps);
        value.put(DAORecord.TEMPLATE_WEIGHT, templateWeight);
        value.put(DAORecord.TEMPLATE_WEIGHT_UNIT, templateWeightUnit.ordinal());
        value.put(DAORecord.TEMPLATE_DISTANCE, templateDistance);
        value.put(DAORecord.TEMPLATE_DISTANCE_UNIT, templateDistanceUnit.ordinal());
        value.put(DAORecord.TEMPLATE_DURATION, templateDuration);
        value.put(DAORecord.TEMPLATE_SECONDS, templateSeconds);

        final SQLiteDatabase db = open();
        final long new_id = db.insert(DAORecord.TABLE_NAME, null, value);
        close();

        return new_id;
    }

    public void addList(final List<Record> list) {
        for (final Record record : list) {
            addRecord(record);
        }
    }

    public int deleteRecord(final long id) {
        final SQLiteDatabase db = this.getWritableDatabase();
        final int ret = db.delete(TABLE_NAME, KEY + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return ret;
    }

    public Record getRecord(final SQLiteDatabase db, final long id) {
        final String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + KEY + "=" + id;

        mCursor = getRecordsListCursor(db, selectQuery);
        if (mCursor.moveToFirst()) {
            return fromCursor(mCursor);
        } else {
            return null;
        }
    }

    public Record getRecord(final long id) {
        return getRecord(this.getReadableDatabase(), id);
    }

    private Record fromCursor(final Cursor cursor) {
        final Date date = DateConverter.DBDateTimeStrToDate(
                cursor.getString(cursor.getColumnIndexOrThrow(DAOFonte.DATE)),
                cursor.getString(cursor.getColumnIndexOrThrow(DAOFonte.TIME))
        );

        final long machine_key;

        final DAOMachine lDAOMachine = new DAOMachine(mContext);
        if (cursor.getString(cursor.getColumnIndexOrThrow(DAOFonte.EXERCISE_KEY)) == null) {
            machine_key = lDAOMachine.addMachine(
                    cursor.getString(cursor.getColumnIndexOrThrow(DAOFonte.EXERCISE)),
                    "",
                    ExerciseType.STRENGTH,
                    "",
                    false,
                    "");
        } else {
            machine_key = cursor.getLong(cursor.getColumnIndexOrThrow(DAOFonte.EXERCISE_KEY));
        }

        final Record value = new Record(date,
                cursor.getString(cursor.getColumnIndexOrThrow(DAORecord.EXERCISE)),
                machine_key,
                cursor.getLong(cursor.getColumnIndexOrThrow(DAORecord.PROFILE_KEY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.SETS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.REPS)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(DAORecord.WEIGHT)),
                WeightUnit.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.WEIGHT_UNIT))),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.SECONDS)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(DAORecord.DISTANCE)),
                DistanceUnit.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.DISTANCE_UNIT))),
                cursor.getLong(cursor.getColumnIndexOrThrow(DAORecord.DURATION)),
                cursor.getString(cursor.getColumnIndexOrThrow(DAORecord.NOTES)),
                ExerciseType.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.EXERCISE_TYPE))),
                cursor.getLong(cursor.getColumnIndexOrThrow(DAORecord.PROGRAM_KEY)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_RECORD_KEY)),
                cursor.getLong(cursor.getColumnIndexOrThrow(DAORecord.PROGRAM_SESSION_KEY)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_REST_TIME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_ORDER)),
                ProgramRecordStatus.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_RECORD_STATUS))),
                RecordType.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.RECORD_TYPE))),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_SETS)),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_REPS)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_WEIGHT)),
                WeightUnit.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_WEIGHT_UNIT))),
                cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_SECONDS)),
                cursor.getFloat(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_DISTANCE)),
                DistanceUnit.fromInteger(cursor.getInt(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_DISTANCE_UNIT))),
                cursor.getLong(cursor.getColumnIndexOrThrow(DAORecord.TEMPLATE_DURATION)));

        value.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DAORecord.KEY)));
        return value;
    }

    public List<Record> fromCursorToList(final Cursor cursor) {
        final List<Record> valueList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final Record record = fromCursor(cursor);
                valueList.add(record);
            } while (cursor.moveToNext());
        }
        return valueList;
    }

    public List<Record> getAllRecords() {
        return getAllRecords(this.getReadableDatabase());
    }

    public List<Record> getAllRecords(final SQLiteDatabase sqLiteDatabase) {
        final String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " ORDER BY " + KEY + " DESC";

        return getRecordsList(selectQuery, sqLiteDatabase);
    }

    public Cursor getAllRecordsByProfile(final Profile pProfile) {
        return getAllRecordsByProfile(pProfile, -1);
    }

    public List<Record> getAllRecordsByProfileList(final Profile pProfile) {
        final Cursor cursor = getAllRecordsByProfile(pProfile, -1);
        return fromCursorToList(cursor);
    }

    /**
     * @param pProfile   record associated to one profile
     * @param pNbRecords max number of records requested
     * @return pNbRecords number of records for a specified pProfile
     */
    public Cursor getAllRecordsByProfile(final Profile pProfile, final int pNbRecords) {
        final String mTop;
        if (pNbRecords == -1) mTop = "";
        else mTop = " LIMIT " + pNbRecords;

        final String selectQuery = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + PROFILE_KEY + "=" + pProfile.getId() +
                " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC" + mTop;

        return getRecordsListCursor(selectQuery);
    }

    private Cursor getRecordsListCursor(final SQLiteDatabase db, final String pRequest) {
        return db.rawQuery(pRequest, null);
    }

    private Cursor getRecordsListCursor(final String pRequest) {
        return getRecordsListCursor(this.getReadableDatabase(), pRequest);
    }

    public List<String> getAllMachinesStrList() {
        return getAllMachinesStrList(null);
    }

    public List<String> getAllMachinesStrList(final Profile pProfile) {
        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;
        final String selectQuery;
        if (pProfile == null) {
            selectQuery = "SELECT DISTINCT " + EXERCISE + " FROM "
                    + TABLE_NAME + " ORDER BY " + EXERCISE + " ASC";
        } else {
            selectQuery = "SELECT DISTINCT " + EXERCISE + " FROM "
                    + TABLE_NAME + "  WHERE " + PROFILE_KEY + "=" + pProfile.getId() + " ORDER BY " + EXERCISE + " ASC";
        }
        mCursor = db.rawQuery(selectQuery, null);

        final int size = mCursor.getCount();

        final List<String> valueList = new ArrayList<>(size);

        if (mCursor.moveToFirst()) {
            do {
                valueList.add(mCursor.getString(0));
            } while (mCursor.moveToNext());
        }
        close();

        return valueList;
    }

    public List<String> getAllDatesList(final Profile pProfile, final Machine pMachine) {

        final SQLiteDatabase db = this.getReadableDatabase();

        mCursor = null;

        String selectQuery = "SELECT DISTINCT " + LOCAL_DATE + " FROM " + TABLE_NAME;
        if (pMachine != null) {
            selectQuery += " WHERE " + EXERCISE_KEY + "=" + pMachine.getId();
            if (pProfile != null)
                selectQuery += " AND " + PROFILE_KEY + "=" + pProfile.getId(); // pProfile should never be null but depending on how the activity is resuming it happen. to be fixed
        } else {
            if (pProfile != null)
                selectQuery += " WHERE " + PROFILE_KEY + "=" + pProfile.getId(); // pProfile should never be null but depending on how the activity is resuming it happen. to be fixed
        }
        selectQuery += " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                + " ORDER BY " + DATE_TIME + " DESC";

        mCursor = db.rawQuery(selectQuery, null);
        final int size = mCursor.getCount();

        final List<String> valueList = new ArrayList<>(size);

        if (mCursor.moveToFirst()) {
            do {
                final Date date = DateConverter.DBDateStrToDate(mCursor.getString(0));
                valueList.add(DateConverter.dateToLocalDateStr(date, mContext));
            } while (mCursor.moveToNext());
        }

        close();

        return valueList;
    }

    public Cursor getTop3DatesFreeWorkoutRecords(final Profile pProfile) {

        if (pProfile == null)
            return null;

        final String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + PROFILE_KEY + "=" + pProfile.getId()
                + " AND " + LOCAL_DATE + " IN (SELECT DISTINCT " + LOCAL_DATE + " FROM " + TABLE_NAME + " WHERE " + PROFILE_KEY + "=" + pProfile.getId() + " AND " + PROGRAM_KEY + "=-1" + " ORDER BY " + LOCAL_DATE + " DESC LIMIT 3)"
                + " AND " + PROGRAM_KEY + "=-1"
                + " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC";

        return getRecordsListCursor(selectQuery);
    }

    public Cursor getProgramTemplateRecords(final long mProgramId) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + PROGRAM_KEY + "=" + mProgramId
                + " AND " + RECORD_TYPE + "=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                + " ORDER BY " + TEMPLATE_ORDER + " ASC";

        return getRecordsListCursor(selectQuery);
    }

    public Cursor getProgramWorkoutRecords(final long mProgramSessionId) {
        String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + PROGRAM_SESSION_KEY + "=" + mProgramSessionId
                + " ORDER BY " + TEMPLATE_ORDER + " ASC";

        return getRecordsListCursor(selectQuery);
    }

    public Cursor getFilteredRecords(final Profile pProfile, final String pMachine,
                                     final String pDate) {

        boolean lfilterMachine = true;
        boolean lfilterDate = true;
        String selectQuery;

        if (pMachine == null || pMachine.isEmpty() || pMachine.equals(mContext.getResources().getText(R.string.all).toString())) {
            lfilterMachine = false;
        }

        if (pDate == null || pDate.isEmpty() || pDate.equals(mContext.getResources().getText(R.string.all).toString())) {
            lfilterDate = false;
        }

        if (lfilterMachine && lfilterDate) {
            selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine
                    + "\" AND " + LOCAL_DATE + "=\"" + pDate + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC";
        } else if (!lfilterMachine && lfilterDate) {
            selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + LOCAL_DATE + "=\"" + pDate + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC";
        } else if (lfilterMachine) {
            selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE + "=\"" + pMachine + "\""
                    + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_NAME
                    + " WHERE " + PROFILE_KEY + "=" + pProfile.getId()
                    + " AND " + RECORD_TYPE + "!=" + RecordType.PROGRAM_TEMPLATE.ordinal()
                    + " AND " + TEMPLATE_RECORD_STATUS + "!=" + ProgramRecordStatus.PENDING.ordinal()
                    + " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC";
        }

        return getRecordsListCursor(selectQuery);
    }

    /**
     * @return the last record for a profile p
     */
    public Record getLastRecord(final Profile pProfile) {

        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;
        Record lReturn = null;

        final String selectQuery = "SELECT MAX(" + KEY + ") FROM " + TABLE_NAME
                + " WHERE " + PROFILE_KEY + "=" + pProfile.getId();
        mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            try {
                final long value = mCursor.getLong(0);
                lReturn = getRecord(value);
            } catch (NumberFormatException e) {
                Log.e(this.getClass().getName(), "NumberFormatException", e);
            }
        }

        close();

        return lReturn;
    }


    /**
     * @return the last record for a profile p
     */
    public Record getLastExerciseRecord(final long machineID, final Profile p) {

        final SQLiteDatabase db = this.getReadableDatabase();
        mCursor = null;
        Record lReturn = null;

        String selectQuery;
        if (p == null) {
            selectQuery = "SELECT MAX(" + KEY + ") FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE_KEY + "=" + machineID;
        } else {
            selectQuery = "SELECT MAX(" + KEY + ") FROM " + TABLE_NAME
                    + " WHERE " + EXERCISE_KEY + "=" + machineID +
                    " AND " + PROFILE_KEY + "=" + p.getId();
        }
        mCursor = db.rawQuery(selectQuery, null);

        if (mCursor.moveToFirst()) {
            try {
                final long value = mCursor.getLong(0);
                lReturn = this.getRecord(value);
            } catch (NumberFormatException e) {
                Log.e(this.getClass().getName(), "NumberFormatException", e);
            }
        }

        close();

        return lReturn;
    }

    public List<Record> getAllRecordByMachineStrArray(final Profile pProfile,
                                                      final String pMachines) {
        return getAllRecordByMachineStrArray(pProfile, pMachines, -1);
    }

    public List<Record> getAllRecordByMachineStrArray(Profile pProfile, String pMachines,
                                                      int pNbRecords) {
        String mTop;
        if (pNbRecords == -1) mTop = "";
        else mTop = " LIMIT " + pNbRecords;

        String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + EXERCISE + "=\"" + pMachines + "\""
                + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                + " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC" + mTop;

        return getRecordsList(selectQuery);
    }

    public List<Record> getAllRecordByMachineIdArray(final Profile pProfile,
                                                     final long pMachineId, final int pNbRecords) {
        String mTop;
        if (pNbRecords == -1) mTop = "";
        else mTop = " LIMIT " + pNbRecords;

        final String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + EXERCISE_KEY + "=\"" + pMachineId + "\""
                + " AND " + PROFILE_KEY + "=" + pProfile.getId()
                + " ORDER BY " + DATE_TIME + " DESC," + KEY + " DESC" + mTop;

        return getRecordsList(selectQuery);
    }

    public List<Record> getAllRecordByMachineIdArray(final Profile pProfile,
                                                     final long pMachineId) {
        return getAllRecordByMachineIdArray(pProfile, pMachineId, -1);
    }


    public List<Record> getAllTemplateRecordByProgramArray(final long pTemplateId) {
        final String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + PROGRAM_KEY + "=" + pTemplateId
                + " AND " + RECORD_TYPE + "=" + RecordType.PROGRAM_TEMPLATE.ordinal();
        return getRecordsList(selectQuery);
    }

    private List<Record> getRecordsList(final String pRequest) {
        return getRecordsList(pRequest, this.getReadableDatabase());
    }

    private List<Record> getRecordsList(final String pRequest, final SQLiteDatabase db) {
        final List<Record> valueList = new ArrayList<>();

        mCursor = null;
        mCursor = db.rawQuery(pRequest, null);

        if (mCursor.moveToFirst() && mCursor.getCount() > 0) {
            do {
                final Record value = fromCursor(mCursor);
                value.setId(mCursor.getLong(mCursor.getColumnIndexOrThrow(DAORecord.KEY)));

                valueList.add(value);
            } while (mCursor.moveToNext());
        }
        return valueList;
    }

    public void updateRecord(final Record record) {
        updateRecord(this.getWritableDatabase(), record);
    }

    public void updateRecord(SQLiteDatabase db, Record record) {
        final ContentValues value = new ContentValues();

        value.put(DAORecord.KEY, record.getId());
        value.put(DAORecord.DATE, DateConverter.dateTimeToDBDateStr(record.getDate()));
        value.put(DAORecord.TIME, DateConverter.dateTimeToDBTimeStr(record.getDate()));
        value.put(DAORecord.EXERCISE, record.getExercise());
        value.put(DAORecord.EXERCISE_KEY, record.getExerciseId());
        value.put(DAORecord.EXERCISE_TYPE, record.getExerciseType().ordinal());
        value.put(DAORecord.PROFILE_KEY, record.getProfileId());
        value.put(DAORecord.SETS, record.getSets());
        value.put(DAORecord.REPS, record.getReps());
        value.put(DAORecord.WEIGHT, record.getWeightInKg());
        value.put(DAORecord.WEIGHT_UNIT, record.getWeightUnit().ordinal());
        value.put(DAORecord.DISTANCE, record.getDistanceInKm());
        value.put(DAORecord.DISTANCE_UNIT, record.getDistanceUnit().ordinal());
        value.put(DAORecord.DURATION, record.getDuration());
        value.put(DAORecord.SECONDS, record.getSeconds());
        value.put(DAORecord.NOTES, record.getNote());
        value.put(DAORecord.PROGRAM_KEY, record.getProgramId());
        value.put(DAORecord.TEMPLATE_RECORD_KEY, record.getTemplateRecordId());
        value.put(DAORecord.PROGRAM_SESSION_KEY, record.getTemplateSessionId());
        value.put(DAORecord.TEMPLATE_REST_TIME, record.getTemplateRestTime());
        value.put(DAORecord.TEMPLATE_ORDER, record.getTemplateOrder());
        value.put(DAORecord.TEMPLATE_RECORD_STATUS, record.getProgramRecordStatus().ordinal());
        value.put(DAORecord.RECORD_TYPE, record.getRecordType().ordinal());

        value.put(DAORecord.TEMPLATE_SETS, record.getTemplateSets());
        value.put(DAORecord.TEMPLATE_REPS, record.getTemplateReps());
        value.put(DAORecord.TEMPLATE_WEIGHT, record.getTemplateWeight());
        value.put(DAORecord.TEMPLATE_WEIGHT_UNIT, record.getTemplateWeightUnit().ordinal());
        value.put(DAORecord.TEMPLATE_DISTANCE, record.getTemplateDistance());
        value.put(DAORecord.TEMPLATE_DISTANCE_UNIT, record.getTemplateDistanceUnit().ordinal());
        value.put(DAORecord.TEMPLATE_DURATION, record.getTemplateDuration());
        value.put(DAORecord.TEMPLATE_SECONDS, record.getTemplateSeconds());

        db.update(TABLE_NAME, value, KEY + " = ?",
                new String[]{String.valueOf(record.getId())});
    }

    public void closeCursor() {
        if (mCursor != null) mCursor.close();
    }

    public void closeAll() {
        if (mCursor != null) mCursor.close();
        close();
    }
}
