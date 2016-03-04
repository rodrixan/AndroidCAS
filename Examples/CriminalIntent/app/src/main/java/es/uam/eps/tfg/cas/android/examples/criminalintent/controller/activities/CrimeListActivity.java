package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.activities;

import android.support.v4.app.Fragment;

import es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments.CrimeListFragment;


public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
