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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.Utils.Utils;
import es.uam.eps.tfg.app.tfgapp.controller.ActionButtons;
import es.uam.eps.tfg.app.tfgapp.controller.listeners.OnExpressionActionListener;
import es.uam.eps.tfg.app.tfgapp.model.CASAdapter;
import es.uam.eps.tfg.app.tfgapp.model.CASImplementation;
import es.uam.eps.tfg.app.tfgapp.view.ExpressionView;

/**
 * Main controller for the app
 */
public class ExpressionFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, OnExpressionActionListener, View.OnClickListener {

    private Callbacks mCallbacks;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private ExpressionView mExpressionView;
    private ActionButtons mButtons;
    private CASAdapter CAS;

    /**
     * @return new instance of this fragment
     */
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
        setupCAS();
        setHasOptionsMenu(true);
    }

    private void setupCAS() {
        CAS = CASImplementation.getInstance();
        CAS.initCAS(Utils.createUltraLongSampleExpression());
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_expression, container, false);
        wireComponents(v);
        setListeners(v);

        return v;
    }

    /**
     * Links the view components with the local fields of the fragment
     *
     * @param v root view where the views are attached to
     */
    private void wireComponents(final View v) {
        final Toolbar toolbar = wireToolbar(v);

        mDrawer = (DrawerLayout) v.findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                getActivity(), mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mExpressionView = (ExpressionView) v.findViewById(R.id.current_exp_view);
        updateExpressionView();

        mButtons = new ActionButtons(v, getContext());
    }

    private void updateExpressionView() {
        mExpressionView.onExpressionUpdated(CAS.getCurrentExpression());
    }

    /**
     * Links the toolbar to the parent activity
     *
     * @param v root view
     * @return the toolbar linked
     */
    private Toolbar wireToolbar(final View v) {
        final Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mCallbacks.setToolBar(toolbar);
        return toolbar;
    }

    /**
     * Assigns the proper listeners of the views
     *
     * @param v root view
     */
    private void setListeners(final View v) {
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        final NavigationView navigationView = (NavigationView) v.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mExpressionView.setOnExpressionActionListener(this);

        mButtons.setListeners(this);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_expression_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.menu_item_action_help:
                //showHelp();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        //http://www.appsrox.com/android/tutorials/showcase/4/#11
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

    public void closeDrawer() {
        if (isDrawerOpened()) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    public boolean isDrawerOpened() {
        return mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void onExpressionSelected(final Expression exp) {
        if (exp != null) {
            final int index = ((ExpressionList) CAS.getCurrentExpression()).indexOf(exp);
            final String selectedExpSymbolicExpression = exp.symbolicExpression();
            Log.d(Utils.LOG_TAG, "Selected Exp: " + selectedExpSymbolicExpression);
        } else {
            Log.d(Utils.LOG_TAG, "Selected Exp: none");
        }
    }

    @Override
    public void onClick(final View view) {
        final CASAdapter.Actions action = mButtons.getAction(view.getId());
        if (action != null) {

        }
    }

    /**
     * Used for the parent activity to do certain functionality
     */
    public interface Callbacks {
        /**
         * Sets a custom toolbar in the activity
         *
         * @param toolbar
         */
        void setToolBar(Toolbar toolbar);
    }
}
