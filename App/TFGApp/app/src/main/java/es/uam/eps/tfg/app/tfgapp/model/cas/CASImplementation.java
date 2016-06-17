package es.uam.eps.tfg.app.tfgapp.model.cas;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.uam.eps.tfg.algebraicEngine.AlgebraicEngine;
import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.util.CASUtils;
import es.uam.eps.tfg.app.tfgapp.util.Utils;
import es.uam.eps.tfg.exception.EquationCreationException;

/**
 * Implementation of the CASAdapter. Singleton.
 */
public class CASImplementation implements CASAdapter {
    private static final List<String> mShowcaseExpressionList = new ArrayList<>();
    private static CASAdapter mCASInstance = null;
    private final AlgebraicEngine mCAS;

    static {
        mShowcaseExpressionList.add(Utils.createShortSampleExpression());
        mShowcaseExpressionList.add(Utils.createMediumSampleExpression());
        mShowcaseExpressionList.add(Utils.createLongSampleExpression());
        mShowcaseExpressionList.add(Utils.createUltraLongSampleExpression());
    }


    private CASImplementation() {
        mCAS = new AlgebraicEngine();
    }

    /**
     * @return the instance of the CAS
     */
    public static CASAdapter getInstance() {
        if (mCASInstance == null) {
            mCASInstance = new CASImplementation();
        }
        return mCASInstance;
    }

    @Override
    public void initCAS(final String exp) {
        try {
            Log.d(Utils.LOG_TAG, "Initializing CAS with expression: " + exp);
            mCAS.insertEquation(exp);
        } catch (final EquationCreationException e) {
            Log.e(Utils.LOG_TAG, "Error while initializing CAS", e);
        }
    }

    @Override
    public Operation getCurrentExpression() {
        final Operation current = mCAS.getOperEq();
        if (current == null) {
            final Random rand = new Random();
            final String newCurrent = mShowcaseExpressionList.get(rand.nextInt((mShowcaseExpressionList.size() - 1) + 1));
            initCAS(newCurrent);
            return mCAS.getOperEq();
        }
        return current;
    }


    /*TODO Implement*/
    @Override
    public String getStringOperatorSymbol(final Operation exp) {

        final String symbol = exp.getRepresentationOperID();

        switch (symbol) {
            case "#":
            case "$":
                return null;
            case "&ZERO":
                return CASUtils.ZERO;
            case "&ONE":
                return CASUtils.ONE;
            case "&MONE":
                return CASUtils.M_ONE;
            case "+":
            case "*":
            case "=":
                return symbol;
            case "-R":
                return "-";
            default:
                return null;
        }
    }

    @Override
    public String getParentStringOperatorSymbol(final Operation exp) {
        return null;
    }

    @Override
    public String getSymbolicExpression(final Operation exp) {
        final String symbol = getStringOperatorSymbol(exp);
        if (symbol == null) {
            return exp.getArg(0).toString();
        } else {
            return symbol;
        }
    }

    @Override
    public Operation commuteProperty(final Operation elementToCommute, final Actions leftOrRight) {
        return null;
    }

    @Override
    public Operation associativeProperty(final Operation startElem, final Operation endElement) {
        return null;
    }

    @Override
    public Operation dissociativeProperty(final Operation elementToDissociate) {
        return null;
    }
    /*TODO: implement*/


//    @Override
//    public Expression operate(final Expression mainExp, final int elemPos) {
//        return null;
//    }
//
//    @Override
//    public Expression operate(final Expression mainExp, final int elemPos1, final int elemPos2) {
//        return null;
//    }
//
//    @Override
//    public Expression operate(final Expression mainExp, final int... elemPos) {
//        return null;
//    }
//
//    @Override
//    public Expression dropElement(final Expression mainExp, final int elemPos) {
//        return null;
//    }
//
//    @Override
//    public Expression moveMember(final Expression equation, final int elemPos) {
//        return null;
//    }

    @Override
    public List<String> getSampleExpressions() {
        return mShowcaseExpressionList;
    }
}
