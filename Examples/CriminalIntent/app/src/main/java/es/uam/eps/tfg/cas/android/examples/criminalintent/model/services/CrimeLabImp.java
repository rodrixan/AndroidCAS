package es.uam.eps.tfg.cas.android.examples.criminalintent.model.services;

import android.content.Context;

import java.util.List;
import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;


public class CrimeLabImp implements CrimeLab {

    private static CrimeLabImp sCrimeLab;

    private List<Crime> mCrimeList;
    private final Context mContext;

    public static CrimeLab getCrimeLab(final Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLabImp(context);
        }
        return sCrimeLab;
    }

    private CrimeLabImp(final Context context) {
        mContext = context;

        createSampleCrimes(50);
    }

    private void createSampleCrimes(final int n) {
        for (int i = 0; i < n; i++) {
            final Crime c = new Crime();
            c.setTitle("Crime #" + i);
            c.setSolved((i % 2 == 0));
            mCrimeList.add(c);
        }
    }

    @Override
    public List<Crime> getCrimes() {
        return mCrimeList;
    }

    @Override
    public Crime getCrime(final UUID crimeId) {
        for (final Crime c : mCrimeList) {
            if (c.getId().equals(crimeId)) {
                return c;
            }
        }
        return null;
    }
}
