package es.uam.eps.tfg.cas.android.examples.criminalintent.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.database.CrimeDbSchema.CrimeTable;


public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(final Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        final String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        final String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        final long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        final int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        final Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);

        return crime;
    }
}
