package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;
import es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments.CrimeFragment;
import es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments.CrimeListFragment;


public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.CallBacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(final UUID crimeId) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            final Intent i = CrimePagerActivity.newIntent(this, crimeId);
            startActivity(i);
        } else {
            final Fragment detail = CrimeFragment.newInstance(crimeId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, detail)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(final UUID crimeId) {
        final CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
