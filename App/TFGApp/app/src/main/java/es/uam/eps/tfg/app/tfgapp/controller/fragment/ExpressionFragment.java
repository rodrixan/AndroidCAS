package es.uam.eps.tfg.app.tfgapp.controller.fragment;

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

import java.util.Arrays;
import java.util.List;

import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.controller.ActionButtons;
import es.uam.eps.tfg.app.tfgapp.controller.listener.OnExpressionActionListener;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASImplementation;
import es.uam.eps.tfg.app.tfgapp.model.history.ExpressionHistory;
import es.uam.eps.tfg.app.tfgapp.model.history.ExpressionHistoryDB;
import es.uam.eps.tfg.app.tfgapp.util.CASUtils;
import es.uam.eps.tfg.app.tfgapp.util.PreferenceUtils;
import es.uam.eps.tfg.app.tfgapp.util.Utils;
import es.uam.eps.tfg.app.tfgapp.view.ExpressionView;

/**
 * Board that shows the expressions and the allowed actions
 */
public class ExpressionFragment extends BaseFragment implements OnExpressionActionListener, View.OnClickListener {
    public static final int EXPRESSION_FRAGMENT_ID = 0;
    private static final int FRAGMENT_TITLE = R.string.expression_fragment_title;

    private ExpressionView mExpressionView;
    private CardView mBoardCardView;
    private ActionButtons mButtons;
    private CASAdapter mCAS;
    private ExpressionHistory mHistory;
    private Operation mSingleSelectedExpression;
    private List<Operation> mMultipleSelectionExpressions;

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
    public void onSingleExpressionSelected(final Operation selected) {
        mSingleSelectedExpression = selected;
        mMultipleSelectionExpressions = null;
        enableSingleSelectionButtons();
    }

    /**
     * Enables the available actions for a single selection
     */
    private void enableSingleSelectionButtons() {
        mButtons.disable(R.id.button_exp_associate);
        if (!CASUtils.isOnMainLevelOfEquation(mSingleSelectedExpression)) {
            mButtons.disable(R.id.button_exp_change_side);
        }
    }

    @Override
    public void onMultipleExpressionSelected(final List<Operation> selection) {
        mMultipleSelectionExpressions = selection;
        mSingleSelectedExpression = null;
        enableMultipleSelectionButtons();
    }

    /**
     * Enables the available actions for a multiple selection
     */
    private void enableMultipleSelectionButtons() {
        mButtons.disableAll();
        mButtons.enable(R.id.button_exp_associate);
        mButtons.enable(R.id.button_exp_operate);
    }

    @Override
    public void onClick(final View view) {
        final CASAdapter.Actions action = mButtons.getAction(view.getId());
        if (action != null) {
            Log.d(Utils.LOG_TAG, "Button " + action.toString() + " pressed");
        }
        if (doAction(action)) {
            mButtons.enableAll();
            mMultipleSelectionExpressions = null;
            mSingleSelectedExpression = null;
        }

    }

    /**
     * Performs a given action, according to the current selection
     *
     * @param action action to do
     * @return true if success, false otherwise
     */
    private boolean doAction(final CASAdapter.Actions action) {
        if (mSingleSelectedExpression != null) {
            return doSingleSelectionAction(action);
        } else if (mMultipleSelectionExpressions != null) {
            return doMultipleSelectionAction(action);
        } else {
            Toast.makeText(getActivity(), R.string.no_exp_selected, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Do an action for a single selection
     *
     * @param action action to perform
     * @return true or false, depending on success
     */
    private boolean doSingleSelectionAction(final CASAdapter.Actions action) {
        final String oldCASExp = mCAS.getCurrentExpression().toString();
        final Operation cloneSelection = (Operation) mSingleSelectedExpression.clone();
        try {
            switch (action) {
                case CHANGE_SIDE:
                    mCAS.changeSide(mSingleSelectedExpression);
                    break;
                case MOVE_RIGHT:
                case MOVE_LEFT:
                    mCAS.commutativeProperty(mSingleSelectedExpression, action);
                    break;

                case DISASSOCIATE:
                    if (!CASUtils.isMathematicalOperation(mSingleSelectedExpression)) {
                        Toast.makeText(getActivity(), R.string.operation_failure_dissociative, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    mCAS.dissociativeProperty(mSingleSelectedExpression);
                    break;

                case OPERATE:
                    if (!CASUtils.isMathematicalOperation(mSingleSelectedExpression)) {
                        Toast.makeText(getActivity(), R.string.operation_failure_operate, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    mCAS.operate(mSingleSelectedExpression);
                    break;

                default:
                    return false;
            }
        } catch (final Exception e) {
            Log.e(Utils.LOG_TAG, "Error on action: " + action.toString() + ". Cause: " + e.getMessage());
            Toast.makeText(getActivity(), R.string.operation_failure, Toast.LENGTH_SHORT).show();
            onCancelledSelectedExpression();
            mCAS.initCAS(oldCASExp);
            updateExpressionView();
            return false;
        }
        addRecordToHistory(oldCASExp, Arrays.asList(new Operation[]{cloneSelection}), action);
        updateExpressionView();
        return true;
    }

    /**
     * Add to the history a new record
     *
     * @param oldExpression the CAS string expression for the old one (before taken the action)
     * @param operations    selected expressions. At least one element is needed
     * @param action        the action that was performed
     */
    private void addRecordToHistory(final String oldExpression, final List<Operation> operations, final CASAdapter.Actions action) {
        final String infixExpressionFromOldExp = CASUtils.getInfixExpressionOf(oldExpression);
        final String selection1 = CASUtils.getInfixExpressionOf(operations.get(0));


        if (operations.size() == 1) {
            mHistory.addRecord(action, infixExpressionFromOldExp, oldExpression, selection1);
        } else {
            final String[] stringOpers = new String[operations.size()];
            for (int i = 0; i < operations.size(); i++) {
                stringOpers[i] = CASUtils.getInfixExpressionOf(operations.get(i));
            }
            mHistory.addRecord(action, infixExpressionFromOldExp, oldExpression, stringOpers);
        }
    }

    @Override
    public void onCancelledSelectedExpression() {
        mSingleSelectedExpression = null;
        mMultipleSelectionExpressions = null;
        mButtons.enableAll();
    }

    /**
     * Do a given action for the current multiple selection
     *
     * @param action action to perform
     * @return tru or false according to the action success
     */
    private boolean doMultipleSelectionAction(final CASAdapter.Actions action) {

        if (mMultipleSelectionExpressions.size() < 2) {
            Toast.makeText(getActivity(), R.string.operation_failure_multiple_selection, Toast.LENGTH_SHORT).show();
            return false;
        }
        final String oldCASExp = mCAS.getCurrentExpression().toString();

        try {
            switch (action) {
                case ASSOCIATE:
                    if (mMultipleSelectionExpressions.size() != 2) {
                        Toast.makeText(getActivity(), R.string.operation_failure_associative, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    mCAS.associativeProperty(mMultipleSelectionExpressions.get(0), mMultipleSelectionExpressions.get(1));
                    break;
                case OPERATE:

                    final int firstIndex = isDistributiveForm();
                    if (firstIndex != -1) {
                        final int secondIndex = (firstIndex == 0) ? 1 : 0;
                        mCAS.distribute(mMultipleSelectionExpressions.get(firstIndex), mMultipleSelectionExpressions.get(secondIndex));
                    } else {

                        mCAS.commonFactor(mMultipleSelectionExpressions);
                    }

                    break;
                default:
                    return false;
            }
        } catch (final Exception e) {
            Log.e(Utils.LOG_TAG, "Error on action: " + action.toString() + ". Cause: " + e.getMessage(), e);
            Toast.makeText(getActivity(), R.string.operation_failure, Toast.LENGTH_SHORT).show();
            onCancelledSelectedExpression();
            mCAS.initCAS(oldCASExp);
            updateExpressionView();
            return false;
        }
        addRecordToHistory(oldCASExp, mMultipleSelectionExpressions, action);
        updateExpressionView();
        return true;
    }

    private CASAdapter.Actions distributeOrCommonFactor() {
        if (mMultipleSelectionExpressions == null || mMultipleSelectionExpressions.size() < 2) {
            return null;
        }
        if (mMultipleSelectionExpressions.size() == 2) {
            if (isDistributiveForm() != -1) {
                return CASAdapter.Actions.DISTRIBUTE;
            } else {
                return CASAdapter.Actions.COMMON_FACTOR;
            }
        } else {
            return CASAdapter.Actions.COMMON_FACTOR;
        }
    }

    private int isDistributiveForm() {
        final Operation op1 = mMultipleSelectionExpressions.get(0);
        final Operation op2 = mMultipleSelectionExpressions.get(1);

        if (!mCAS.isOnDistributiveForm(op1, op2)) {
            if (!mCAS.isOnDistributiveForm(op2, op1)) {
                return -1;
            } else {
                return 1;
            }

        }
        return 0;
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
        final String current = mHistory.returnToPreviousExpression();
        mCAS.initCAS(current);
        updateExpressionView();
    }

}
