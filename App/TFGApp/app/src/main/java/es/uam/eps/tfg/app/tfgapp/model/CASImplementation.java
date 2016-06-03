package es.uam.eps.tfg.app.tfgapp.model;

import es.uam.eps.expressions.types.interfaces.Expression;


public class CASImplementation implements CASAdapter {
    private static CASAdapter mCASInstance = null;
    private Expression mExpression;

    private CASImplementation() {
    }


    public static CASAdapter getInstance() {
        if (mCASInstance == null) {
            mCASInstance = new CASImplementation();
        }
        return mCASInstance;
    }


    @Override
    public void initCAS(final Expression exp) {
        mExpression = exp;
    }

    @Override
    public Expression getCurrentExpression() {
        return mExpression;
    }
}
