package es.uam.eps.tfg.app.tfgapp.model.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.Utils.Utils;

public class CASImplementation implements CASAdapter {
    private static final List<Expression> mShowcaseExpressionList = new ArrayList<>();
    private static CASAdapter mCASInstance = null;

    static {
        mShowcaseExpressionList.add(Utils.createShortSampleExpression());
        mShowcaseExpressionList.add(Utils.createMediumSampleExpression());
        mShowcaseExpressionList.add(Utils.createLongSampleExpression());
        mShowcaseExpressionList.add(Utils.createUltraLongSampleExpression());
    }

    private Expression mExpression = null;

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
        if (mExpression == null) {
            final Random rand = new Random();
            mExpression = mShowcaseExpressionList.get(rand.nextInt((mShowcaseExpressionList.size()-1 - 0) + 1) + 0);
        }
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

    @Override
    public List<Expression> getSampleExpressions() {
        return mShowcaseExpressionList;
    }
}
