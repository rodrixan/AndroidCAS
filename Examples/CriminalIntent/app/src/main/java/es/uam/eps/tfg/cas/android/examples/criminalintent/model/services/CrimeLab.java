package es.uam.eps.tfg.cas.android.examples.criminalintent.model.services;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.database.CrimeCursorWrapper;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.database.CrimeDBHelper;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.database.CrimeDbSchema.CrimeTable;
import es.uam.eps.tfg.cas.android.examples.criminalintent.utils.Utils;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    public static CrimeLab getCrimeLab(final Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(final Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeDBHelper(mContext).getWritableDatabase();
    }

    private static ContentValues getContentValues(final Crime crime) {
        final ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, (crime.isSolved()) ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }


    public List<Crime> getCrimes() {
        Log.d(Utils.APP_LOG_TAG, "Cogiendo crimenes");
        final CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            return getCrimeListFromCursor(cursor);
        } finally {
            cursor.close();
        }


    }

    private CrimeCursorWrapper queryCrimes(final String whereClause, final String[] whereArgs) {
        final Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,       //select all columns
                whereClause,//where
                whereArgs,  // values
                null,       //groupBy
                null,       //having
                null,       //orderBy
                null        //limit
        );
        return new CrimeCursorWrapper(cursor);
    }

    private List<Crime> getCrimeListFromCursor(final CrimeCursorWrapper cursor) {
        final List<Crime> list = new ArrayList<>();
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {

            Log.d(Utils.APP_LOG_TAG, "Cogiendo crimen unico: " + cursor.getCrime().getId().toString());
            list.add(cursor.getCrime());
            cursor.moveToNext();
            i++;
        }
        return list;
    }

    public Crime getCrime(final UUID crimeId) {
        final String whereClause = CrimeTable.Cols.UUID + " = ?";
        final CrimeCursorWrapper cursor = queryCrimes(whereClause, new String[]{crimeId.toString()});
        Log.d(Utils.APP_LOG_TAG, "crime lab: cogiendo crimen con id " + crimeId.toString());
        try {
            return getCrimeFromCursor(cursor);
        } finally {
            cursor.close();
        }

    }


    private Crime getCrimeFromCursor(final CrimeCursorWrapper cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        cursor.moveToFirst();
        return cursor.getCrime();
    }

    public long getNumberOfCrimes() {
        return DatabaseUtils.queryNumEntries(mDatabase, CrimeTable.NAME);
    }


    public boolean addCrime(final Crime c) {
        final ContentValues values = getContentValues(c);

        final long code = mDatabase.insert(CrimeTable.NAME, null, values);
        Log.d(Utils.APP_LOG_TAG, "crime lab: añadido crimen con id " + c.getId().toString());
        return code != -1;
    }

    public boolean removeCrime(final UUID crimeId) {
        final String whereClause = CrimeTable.Cols.UUID + " = ?";
        Log.d(Utils.APP_LOG_TAG, "crime lab: eliminado crimen con id " + crimeId.toString());
        final long code = mDatabase.delete(CrimeTable.NAME, whereClause, new String[]{crimeId.toString()});

        return code == 1;
    }

    public boolean updateCrime(final Crime c) {
        final String whereClause = CrimeTable.Cols.UUID + " = ?";
        final String uuidString = c.getId().toString();
        final ContentValues values = getContentValues(c);
        Log.d(Utils.APP_LOG_TAG, "crime lab: actualizando crimen con id " + c.getId().toString());
        final int code = mDatabase.update(CrimeTable.NAME, values, whereClause, new String[]{uuidString});

        return code == 1;
    }

    public File getPhotoFile(final Crime c) {
        final File externalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir != null) {
            return new File(externalFilesDir, getCrimePhotoFileName(c));
        }
        return null;
    }

    public String getCrimePhotoFileName(final Crime c) {
        return "IMG_" + c.getId().toString() + ".jpg";
    }

}
