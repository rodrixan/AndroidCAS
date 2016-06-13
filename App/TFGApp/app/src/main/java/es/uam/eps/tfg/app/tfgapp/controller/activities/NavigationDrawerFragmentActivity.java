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

    public abstract int setFragment(int id, boolean first);

    protected abstract int getDefaultFragmentId();

    private void setNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        setActionToggle();

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
        }

        mNavigationView.setNavigationItemSelectedListener(this);
    }

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

//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.fragment_expression_toolbar, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(final MenuItem item) {
//        if (mToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//        if (onOptionsItemSelected(item.getItemId())) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    protected abstract boolean onOptionsItemSelected(int id);

    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

    }
}
