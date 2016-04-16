package es.uam.eps.tfg.cas.android.examples.testapp.controller.fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.uam.eps.tfg.cas.android.examples.testapp.R;


public class DragFragment extends Fragment {

    private static final String TAG = "APP_TEST";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;
    private Callbacks mCallbacks;

    public static Fragment newInstance() {
        return new DragFragment();
    }

    public interface Callbacks {
        void setToolBar(Toolbar toolbar);

        void setDrawerToggle();
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
        //configureHandler();
        Log.i(TAG, "DragFragment created");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_drag, container, false);


        wireComponents(v);
        setListeners();


        return v;
    }

    private void wireComponents(final View v) {
        mDrawerLayout = (DrawerLayout) v.findViewById(R.id.fragment_drag_drawer_layout);
        mNavigationView = (NavigationView) v.findViewById(R.id.fragment_drag_nav_view);
        mToolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mCallbacks.setToolBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        );
    }

    private void setListeners() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_draw_settings_color:
                        Toast.makeText(getActivity(), "Changed color", Toast.LENGTH_SHORT).show();
                        item.setChecked(false);
                        closeDrawer();
                        break;
                    default:
                        break;
                }

                return true;
            }
        });


        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mCallbacks.setDrawerToggle();

        mDrawerToggle.syncState();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_drag, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_item_history:
                Toast.makeText(getActivity(), "History not available", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isDrawerOpened() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer() {
        if (isDrawerOpened()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}