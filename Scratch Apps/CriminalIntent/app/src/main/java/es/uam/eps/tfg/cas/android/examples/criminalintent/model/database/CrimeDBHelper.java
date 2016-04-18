package es.uam.eps.tfg.cas.android.examples.criminalintent.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import es.uam.eps.tfg.cas.android.examples.criminalintent.model.database.CrimeDbSchema.CrimeTable;


public class CrimeDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "crimeBase.db";
    private static final int DB_VERSION = 1;

    public CrimeDBHelper(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + "(" +
                        CrimeTable.Cols.UUID + ", " +
                        CrimeTable.Cols.TITLE + ", " +
                        CrimeTable.Cols.DATE + ", " +
                        CrimeTable.Cols.SOLVED + ", " +
                        CrimeTable.Cols.SUSPECT +
                        ")"
        );
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

    }
}
