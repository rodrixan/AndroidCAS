package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.uam.eps.tfg.app.tfgapp.R;

public class ExpressionFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    public interface Callbacks {
        void setToolBar(Toolbar toolbar);
    }

    private Callbacks mCallbacks;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    public static Fragment newInstance() {
        return new ExpressionFragment();
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mCallbacks = (Callbacks) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_expression, container, false);


        wireComponents(v);
        setListeners(v);


        return v;
    }

    private void wireComponents(final View v) {
        final Toolbar toolbar = wireToolbar(v);

        mDrawer = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private Toolbar wireToolbar(final View v) {
        final Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mCallbacks.setToolBar(toolbar);
        return toolbar;
    }

    private void setListeners(final View v) {
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        final NavigationView navigationView = (NavigationView) v.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.expression, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                Toast.makeText(getActivity(), "History not available", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        closeDrawer();
        return true;
    }

    public boolean isDrawerOpened() {
        return mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer() {
        if (isDrawerOpened()) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }
}
