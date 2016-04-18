package es.uam.eps.tfg.cas.android.examples.draganddraw.controller.activities;

import android.support.v4.app.Fragment;

import es.uam.eps.tfg.cas.android.examples.draganddraw.controller.fragments.DragAndDrawFragment;

public class DragAndDrawActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }
}
