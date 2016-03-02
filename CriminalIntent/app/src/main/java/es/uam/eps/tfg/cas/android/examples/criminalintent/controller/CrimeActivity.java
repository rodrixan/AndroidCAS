package es.uam.eps.tfg.cas.android.examples.criminalintent.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;

public class CrimeActivity extends FragmentActivity {

    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);
        setToolBar();

        mFragmentManager = getSupportFragmentManager();

        addFragmentToManager(R.id.fragment_container);

    }

    private void setToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    private void addFragmentToManager(final int fragmentId) {
        //Fragment transitions are used to add, remove, (de)attach, or replace fragments in the frag. list
        Fragment fragment = mFragmentManager.findFragmentById(fragmentId);

        if (fragment == null) {
            fragment = new CrimeFragment();
            mFragmentManager.beginTransaction().add(fragmentId, fragment).commit();
        }
    }


}
