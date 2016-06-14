package es.uam.eps.tfg.app.tfgapp.controller.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;

import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.Utils.Utils;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.Callbacks;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.ExpressionFragment;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.HelpFragment;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.HistoryFragment;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.SettingsFragment;
import es.uam.eps.tfg.app.tfgapp.controller.fragments.ShowcaseFragment;

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
        int fragmentId = -1;

        switch (item.getItemId()) {
            case R.id.nav_board:
                fragmentId = ExpressionFragment.EXPRESSION_FRAGMENT_ID;
                break;
            case R.id.nav_showcase:
                fragmentId = ShowcaseFragment.SHOWCASE_FRAGMENT_ID;
                break;
            case R.id.nav_history:
                fragmentId = HistoryFragment.HISTORY_FRAGMENT_ID;
                break;
            case R.id.nav_settings:
                fragmentId = SettingsFragment.SETTINGS_FRAGMENT_ID;
                break;
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

        final int navItemPos = setFragment(fragmentId, false);
        mNavigationView.getMenu().getItem(navItemPos).setChecked(true);
    }

    @Override
    public int setFragment(final int id, final boolean first) {
        Log.d(Utils.LOG_TAG, "Called setFragment with id: " + id);

        Fragment fragment = null;
        String tag = null;
        boolean main = false;
        int navItemPos = 0;

        switch (id) {
            case ExpressionFragment.EXPRESSION_FRAGMENT_ID:
                fragment = ExpressionFragment.newInstance();
                navItemPos = 0;
                main = true;
                break;
            case HelpFragment.HELP_FRAGMENT_ID:
                fragment = HelpFragment.newInstance();
                setDrawerEnable(false);
                tag = HelpFragment.getTagID() + "";
                navItemPos = 0;
                break;
            case ShowcaseFragment.SHOWCASE_FRAGMENT_ID:
                fragment = ShowcaseFragment.newInstance();
                tag = ShowcaseFragment.getTagID() + "";
                navItemPos = 1;
                break;
            case HistoryFragment.HISTORY_FRAGMENT_ID:
                fragment = HistoryFragment.newInstance();
                tag = HistoryFragment.getTagID() + "";
                navItemPos = 2;
                break;
            case SettingsFragment.SETTINGS_FRAGMENT_ID:
                fragment = SettingsFragment.newInstance();
                tag = SettingsFragment.getTagID() + "";
                break;
            default:
                return 0;
        }

        doFragmentTransaction(first, fragment, tag, main);
        return navItemPos;

    }


    private void doFragmentTransaction(final boolean first, final Fragment fragment, final String tag, final boolean main) {

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        getSupportFragmentManager().popBackStack();//always go back to the first fragment
        fragmentTransaction.replace(R.id.fragment_container, fragment);

        if (!first && !main) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();


    }

    private void setDrawerEnable(final boolean enable) {
        final int drawerSwipeStateFlag = (enable) ? DrawerLayout.LOCK_MODE_UNDEFINED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED;

        mToggle.setDrawerIndicatorEnabled(enable);
        mDrawerLayout.setDrawerLockMode(drawerSwipeStateFlag);
    }

    @Override
    public void setSubtitle(final int subtitle) {
        getSupportActionBar().setSubtitle(subtitle);
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
        getSupportActionBar().setSubtitle(null);
        //always going to te main screen
        mNavigationView.getMenu().getItem(0).setChecked(true);
    }

    private boolean isDrawerOpened() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

}
