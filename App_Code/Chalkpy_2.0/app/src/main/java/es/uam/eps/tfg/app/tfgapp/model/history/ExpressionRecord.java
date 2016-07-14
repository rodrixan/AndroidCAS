package es.uam.eps.tfg.app.tfgapp.model.history;

import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;

/**
 * Each item of the expression history
 */
public class ExpressionRecord {
    private final CASAdapter.Actions mAction;
    private final String mGlobalExp;
    private final String mSelectedExp;
    private final String mCASExpression;

    public ExpressionRecord(final CASAdapter.Actions action, final String global, final String selection, final String CASExpression) {
        mAction = action;
        mGlobalExp = global;
        mSelectedExp = selection;
        mCASExpression = CASExpression;
    }

    public CASAdapter.Actions getAction() {
        return mAction;
    }

    public String getGlobalExp() {
        return mGlobalExp;
    }

    public String getSelectedExp() {
        return mSelectedExp;
    }

    public String getCASExpression() {
        return mCASExpression;
    }

    @Override
    public String toString() {
        return mAction.toString() + "[" + mGlobalExp + "," + mSelectedExp + "]";
    }
}
