package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import es.uam.eps.expressions.types.ExpressionList;
import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.controller.ActionButtons;
import es.uam.eps.tfg.app.tfgapp.controller.listeners.OnExpressionActionListener;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASImplementation;
import es.uam.eps.tfg.app.tfgapp.model.history.ExpressionHistory;
import es.uam.eps.tfg.app.tfgapp.model.history.ExpressionHistoryDB;
import es.uam.eps.tfg.app.tfgapp.util.PreferenceUtils;
import es.uam.eps.tfg.app.tfgapp.util.Utils;
import es.uam.eps.tfg.app.tfgapp.view.ExpressionView;

/**
 * Board that shows the expressions and the allowed actions
 */
public class ExpressionFragment extends Fragment implements OnExpressionActionListener, View.OnClickListener {
    public static final int EXPRESSION_FRAGMENT_ID = 0;
    private static final int FRAGMENT_TITLE = R.string.expression_fragment_title;

    private Callbacks mCallbacks;
    private ExpressionView mExpressionView;
    private CardView mBoardCardView;
    private ActionButtons mButtons;
    private CASAdapter mCAS;
    private ExpressionHistory mHistory;

    /**
     * @return new instance of this fragment
     */
    public static Fragment newInstance() {
        return new ExpressionFragment();
    }

    /**
     * @return the tag for the backstack
     */
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
        setupHistory();
    }

    /**
     * Gets a CAS instance
     */
    private void setupCAS() {
        mCAS = CASImplementation.getInstance();
    }

    /**
     * Gets a history instance
     */
    private void setupHistory() {
        mHistory = ExpressionHistoryDB.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_expression, container, false);
        wireComponents(v);
        setListeners(v);

        mCallbacks.setSubtitle(null);

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

        mBoardCardView = (CardView) v.findViewById(R.id.fragment_expression_board);
        setBoardColor();

        mButtons = new ActionButtons(v, getContext());
    }

    /**
     * Notifies the view to be updated
     */
    private void updateExpressionView() {
        mExpressionView.onExpressionUpdated(mCAS.getCurrentExpression());
    }

    /**
     * Changes the background color of the board
     */
    private void setBoardColor() {
        final int boardColor = PreferenceUtils.getBoardColor(getActivity());
        mBoardCardView.setCardBackgroundColor(boardColor);
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
    public void onSingleExpressionSelected(final Expression selected) {
        final int index = ((ExpressionList) mCAS.getCurrentExpression()).indexOf(selected);
        Log.d(Utils.LOG_TAG, "Index of single selection: " + index);
        mHistory.addExpression(CASAdapter.Actions.SELECT_SINGLE, mCAS.getCurrentExpression(), selected);


    }

    @Override
    public void onMultipleExpressionSelected(final List<Expression> selection) {
        final int[] indexes = new int[selection.size()];
        for (int i = 0; i < selection.size(); i++) {
            indexes[i] = ((ExpressionList) mCAS.getCurrentExpression()).indexOf(selection.get(i));
            Log.d(Utils.LOG_TAG, "Index of multiple selection: " + indexes[i]);
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

    /**
     * Performs an action from the toolbar menu
     *
     * @param id id of the menu item clicked
     * @return true if the event was consumed, false if not
     */
    private boolean onOptionsItemSelected(final int id) {
        switch (id) {
            case R.id.menu_item_action_help:
                showHelp();
                return true;
            case R.id.menu_item_action_undo:
                undo();
                return true;
            default:
                return false;
        }
    }

    /**
     * Shows the help screen
     */
    private void showHelp() {
        mCallbacks.navigateToFragment(HelpFragment.HELP_FRAGMENT_ID);
    }

    /**
     * Undo the last action (if it's possible)
     */
    private void undo() {
        if (mHistory.getRecordCount() < 1) {
            Toast.makeText(getActivity(), R.string.popup_unable_to_undo, Toast.LENGTH_SHORT).show();
            return;
        }
        final Expression current = mHistory.returnToPreviousExpression();
        mCAS.initCAS(current);
        updateExpressionView();
    }

}
