package es.uam.eps.tfg.app.tfgapp.controller.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import es.uam.eps.tfg.app.tfgapp.R;

/**
 * Activity with navigation drawer basics
 */
public abstract class NavigationDrawerFragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout mDrawerLayout;
    protected Toolbar mToolbar;
    protected ActionBar mActionBar;
    protected NavigationView mNavigationView;
    protected ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        setToolbar();

        setNavigationDrawer();

        //First start (Main Fragment)
        setFragment(getDefaultFragmentId(), true);
        mNavigationView.getMenu().getItem(0).setChecked(true);

    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    /**
     * Sets a fragment in the container
     *
     * @param id    id of the fragment
     * @param first if it's first time the main activity is called
     * @return position of the fragment item  in the navigation drawer
     */
    protected abstract int setFragment(int id, boolean first);

    /**
     * @return the default fragment id for the navigation drawer to show
     */
    protected abstract int getDefaultFragmentId();

    /**
     * Sets up a navigation drawer
     */
    private void setNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        setActionToggle();

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Sets the toggle associated to the drawer
     */
    private void setActionToggle() {
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        mToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                onBackPressed();
            }
        });
    }

    /**
     * Sets the toolbar for the application
     */
    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

    }
}
