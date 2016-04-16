package es.uam.eps.tfg.cas.android.examples.testapp.controller.activities;

import android.support.v4.app.Fragment;

import es.uam.eps.tfg.cas.android.examples.testapp.controller.fragments.DragFragment;

public class DragActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return DragFragment.newInstance();
    }
}
