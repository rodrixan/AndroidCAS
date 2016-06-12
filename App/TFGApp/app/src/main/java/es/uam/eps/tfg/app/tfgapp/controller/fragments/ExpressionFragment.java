package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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
public class ExpressionFragment extends Fragment implements OnExpressionActionListener, View.OnClickListener {
    public static final int EXPRESSION_FRAGMENT_ID = 0;
    private static final int FRAGMENT_TITLE = R.string.expression_fragment_title;

    private Callbacks mCallbacks;
    private ExpressionView mExpressionView;
    private ActionButtons mButtons;
    private CASAdapter CAS;

    /**
     * @return new instance of this fragment
     */
    public static Fragment newInstance() {
        return new ExpressionFragment();
    }

    public static int getTagID() {
        return FRAGMENT_TITLE;
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
        setupCAS();
    }

    private void setupCAS() {
        CAS = CASImplementation.getInstance();
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
        mCallbacks.setTitle(FRAGMENT_TITLE);

        mExpressionView = (ExpressionView) v.findViewById(R.id.current_exp_view);
        updateExpressionView();

        mButtons = new ActionButtons(v, getContext());
    }

    private void updateExpressionView() {
        mExpressionView.onExpressionUpdated(CAS.getCurrentExpression());
    }

    /**
     * Assigns the proper listeners of the views
     *
     * @param v root view
     */
    private void setListeners(final View v) {

        mExpressionView.setOnExpressionActionListener(this);
        mButtons.setListeners(this);
    }

    @Override
    public void onSingleExpressionSelected(final Expression global, final Expression selected) {
        final int index = ((ExpressionList) global).indexOf(selected);
        final String selectedExpSymbolicExpression = selected.symbolicExpression();
    }

    @Override
    public void onMultipleExpressionSelected(final Expression global, final List<Expression> selection) {
        final int[] indexes = new int[selection.size()];
        for (int i = 0; i < selection.size(); i++) {
            indexes[i] = ((ExpressionList) global).indexOf(selection.get(i));
        }
    }

    @Override
    public void onClick(final View view) {
        final CASAdapter.Actions action = mButtons.getAction(view.getId());
        if (action != null) {
            Log.d(Utils.LOG_TAG, "Button " + action.toString() + " pressed");
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_expression_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if (onOptionsItemSelected(item.getItemId())) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected boolean onOptionsItemSelected(final int id) {
        switch (id) {
            case R.id.menu_item_action_help:
                showHelp();
                return true;
            default:
                return false;
        }
    }

    private void showHelp() {
        mCallbacks.navigateToFragment(HelpFragment.HELP_FRAGMENT_ID);
    }

}
