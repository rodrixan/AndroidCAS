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

    @Override
    public Expression commuteProperty(final Expression mainExp, final int iniPos, final int finalPos) {
        return null;
    }

    @Override
    public Expression associativeProperty(final Expression mainExp, final int from, final int to) {
        return null;
    }

    @Override
    public Expression dissociativeProperty(final Expression mainExp, final int elemPos) {
        return null;
    }

    @Override
    public Expression operate(final Expression mainExp, final int elemPos) {
        return null;
    }

    @Override
    public Expression operate(final Expression mainExp, final int elemPos1, final int elemPos2) {
        return null;
    }

    @Override
    public Expression operate(final Expression mainExp, final int... elemPos) {
        return null;
    }

    @Override
    public Expression dropElement(final Expression mainExp, final int elemPos) {
        return null;
    }

    @Override
    public Expression moveMember(final Expression equation, final int elemPos) {
        return null;
    }
}
