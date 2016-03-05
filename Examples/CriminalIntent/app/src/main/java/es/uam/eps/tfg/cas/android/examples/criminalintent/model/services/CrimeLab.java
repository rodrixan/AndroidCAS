package es.uam.eps.tfg.cas.android.examples.criminalintent.model.services;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private final List<Crime> mCrimeList;
    private final Context mContext;

    public static CrimeLab getCrimeLab(final Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }


    private void createSampleCrimes(final int n) {
        for (int i = 0; i < n; i++) {
            final Crime c = new Crime();
            c.setTitle("Crime #" + i);
            c.setSolved((i % 2 == 0));
            mCrimeList.add(c);
        }
    }

    private CrimeLab(final Context context) {
        mContext = context;
        mCrimeList = new ArrayList<>();
        //createSampleCrimes(100);
    }


    public List<Crime> getCrimes() {
        return mCrimeList;
    }

    public Crime getCrime(final UUID crimeId) {
        for (final Crime c : mCrimeList) {
            if (c.getId().equals(crimeId)) {
                return c;
            }
        }
        return null;
    }

    public int getNumberOfCrimes() {
        return mCrimeList.size();
    }


    public boolean addCrime(final Crime c) {
        return mCrimeList.add(c);
    }
}
