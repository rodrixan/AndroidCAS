package es.uam.eps.tfg.cas.android.examples.testapp.controller.activities;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import es.uam.eps.tfg.cas.android.examples.testapp.R;
import es.uam.eps.tfg.cas.android.examples.testapp.controller.fragments.DragFragment;

public class DragActivity extends SingleFragmentActivity implements DragFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return DragFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        final DragFragment fragment = (DragFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment.isDrawerOpened()) {
            fragment.closeDrawer();
            return;
        }

        super.onBackPressed();
    }


    @Override
    public void setToolBar(final Toolbar toolbar) {
        setSupportActionBar(toolbar);
    }
}
