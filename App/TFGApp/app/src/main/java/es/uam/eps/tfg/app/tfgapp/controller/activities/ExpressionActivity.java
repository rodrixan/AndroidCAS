package es.uam.eps.tfg.app.tfgapp.controller.activities;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.ExpressionFragment;

/**
 * Activity that holds the fragment of the app
 */
public class ExpressionActivity extends SingleFragmentActivity
        implements ExpressionFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return ExpressionFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        final ExpressionFragment fragment = (ExpressionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

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
