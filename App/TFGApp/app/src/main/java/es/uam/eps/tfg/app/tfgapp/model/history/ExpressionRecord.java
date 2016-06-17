package es.uam.eps.tfg.app.tfgapp.model.history;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;

/**
 * Each item of the expression history
 */
public class ExpressionRecord {
    private final CASAdapter.Actions mAction;
    private final Expression mGlobalExp;
    private final Expression mSelectedExp;

    public ExpressionRecord(final CASAdapter.Actions action, final Expression global, final Expression selection) {
        mAction = action;
        mGlobalExp = global;
        mSelectedExp = selection;
    }

    public CASAdapter.Actions getAction() {
        return mAction;
    }

    public Expression getGlobalExp() {
        return mGlobalExp;
    }

    public Expression getSelectedExp() {
        return mSelectedExp;
    }

    @Override
    public String toString() {
        return mAction.toString() + "[" + mGlobalExp.symbolicExpression() + "," + mSelectedExp.symbolicExpression() + "]";
    }
}
