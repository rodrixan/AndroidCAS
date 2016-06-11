package es.uam.eps.tfg.app.tfgapp.controller.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.controller.Callbacks;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.ExpressionFragment;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.HelpFragment;

/**
 * Activity that holds the fragments of the app
 */
public class MainActivity extends NavigationDrawerFragmentActivity implements Callbacks {

    @Override
    protected int getDefaultFragmentId() {
        return ExpressionFragment.EXPRESSION_FRAGMENT_ID;
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        //http://www.appsrox.com/android/tutorials/showcase/4/#11
        item.setChecked(true);
        int fragmentId = -1;

        switch (item.getItemId()) {
            case R.id.nav_board:
                fragmentId = ExpressionFragment.EXPRESSION_FRAGMENT_ID;
            default:
                fragmentId = ExpressionFragment.EXPRESSION_FRAGMENT_ID;
                break;
        }
        navigateToFragment(fragmentId);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void navigateToFragment(final int fragmentId) {
        setFragment(fragmentId, false);
    }

    @Override
    public void setFragment(final int id, final boolean first) {

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        switch (id) {
            case ExpressionFragment.EXPRESSION_FRAGMENT_ID:
                fragment = ExpressionFragment.newInstance();
                break;
            case HelpFragment.HELP_FRAGMENT_ID:
                fragment = HelpFragment.newInstance();
                setDrawerEnable(false);
                break;
            default:
                return;
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (!first) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

    }

    private void setDrawerEnable(final boolean enable) {
        final int drawerSwipeStateFlag = (enable) ? DrawerLayout.LOCK_MODE_UNDEFINED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

        mToggle.setDrawerIndicatorEnabled(enable);
        mDrawerLayout.setDrawerLockMode(drawerSwipeStateFlag);
    }

    @Override
    public void setTitle(final int title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpened()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
        setDrawerEnable(true);
    }

    private boolean isDrawerOpened() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

}
