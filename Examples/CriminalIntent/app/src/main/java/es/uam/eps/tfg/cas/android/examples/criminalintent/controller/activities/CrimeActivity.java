package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.activities;

import android.support.v4.app.Fragment;

import es.uam.eps.tfg.cas.android.examples.criminalintent.controller.SingleFragmentActivity;
import es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments.CrimeFragment;

public class CrimeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
