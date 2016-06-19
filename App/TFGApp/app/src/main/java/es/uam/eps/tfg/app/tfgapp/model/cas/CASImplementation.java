package es.uam.eps.tfg.app.tfgapp.model.cas;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import es.uam.eps.tfg.algebraicEngine.AlgebraicEngine;
import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.util.CASUtils;
import es.uam.eps.tfg.app.tfgapp.util.Utils;
import es.uam.eps.tfg.exception.EquationCreationException;
import es.uam.eps.tfg.exception.NotApplicableReductionException;

/**
 * Implementation of the CASAdapter. Singleton.
 */
public class CASImplementation implements CASAdapter {
    private static final String DEF_SYMBOL = "o-";
    private static final List<String> mShowcaseExpressionList = new ArrayList<>();
    private static CASAdapter mCASInstance = null;

    static {
        mShowcaseExpressionList.add(CASUtils.createShortSampleExpression());
        mShowcaseExpressionList.add(CASUtils.createMediumSampleExpression());
        mShowcaseExpressionList.add(CASUtils.createLongSampleExpression());
        mShowcaseExpressionList.add(CASUtils.createUltraLongSampleExpression());
    }

    private final AlgebraicEngine mCAS;

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
    public String getGrandParentStringOperatorSymbol(final Operation exp) {
        final Operation parent = mCAS.getOperById(exp.getParentID());
        if (parent != null) {
            return getParentStringOperatorSymbol(parent);
        }
        return null;
    }

    @Override
    public String getParentStringOperatorSymbol(final Operation exp) {
        final Operation parent = mCAS.getOperById(exp.getParentID());
        if (parent != null) {
            return getStringOperatorSymbol(parent);
        }
        return null;
    }

    @Override
    public String getStringOperatorSymbol(final Operation exp) {

        final String symbol = exp.getRepresentationOperID();

        //when custom operation creation
        if (symbol == null) {
            return DEF_SYMBOL;
        }

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
            case "-":
                return symbol;
            case "@INV":
                return "1/";
            default:
                return null;
        }
    }

    @Override
    public Operation commuteProperty(final Operation elementToCommute, final Actions leftOrRight) throws NotApplicableReductionException {
        final UUID parentId = elementToCommute.getParentID();
        final Operation parent = mCAS.getOperById(parentId);

        if (parent == null) {
            throw new NotApplicableReductionException("No parent");
        }
        final int indexOfElement = parent.getIndexOfArg(elementToCommute);
        final int finalPosition = getFinalPosition(leftOrRight, indexOfElement);
        if (finalPosition >= parent.getArgs().size() || finalPosition < 0) {
            throw new NotApplicableReductionException("Can't apply the commutative: exceeded index");
        }
        final Operation commutedOperation = mCAS.commute(parent, indexOfElement, finalPosition);

        final Operation grandParent = mCAS.getOperById(parent.getParentID());

        if (grandParent == null) {
            mCAS.setOperEq(commutedOperation);
            return mCAS.getOperEq();//we are on the main expression
        }
        final int indexOfParent = grandParent.getIndexOfArg(parent);
        grandParent.setArg(indexOfParent, commutedOperation);

        return mCAS.getOperEq();
    }

    private int getFinalPosition(final Actions leftOrRight, final int indexOfElement) {
        int finalPosition = -1;
        if (leftOrRight.equals(Actions.MOVE_LEFT)) {
            finalPosition = indexOfElement - 1;
        } else if (leftOrRight.equals(Actions.MOVE_RIGHT)) {
            finalPosition = indexOfElement + 1;
        }
        return finalPosition;
    }

    @Override
    public String getSymbolStringExpression(final Operation exp) {
        final String symbol = getStringOperatorSymbol(exp);
        if (symbol == null) {
            return exp.getArg(0).toString();
        } else {
            return symbol;
        }
    }

    @Override
    public Operation associativeProperty(final Operation startElement, final Operation endElement) throws NotApplicableReductionException {
        final Operation parent = mCAS.getOperById(startElement.getParentID());

        if (parent == null) {
            throw new NotApplicableReductionException("No parent");
        }
        if (!parent.equals(mCAS.getOperById(endElement.getParentID()))) {
            throw new NotApplicableReductionException("Parent on associative property are not the same");
        }
        final int startIndex = parent.getIndexOfArg(startElement);
        final int endIndex = parent.getIndexOfArg(endElement);

        final Operation associatedElement = mCAS.associate(parent, Math.min(startIndex, endIndex), Math.max(startIndex, endIndex));

        final Operation grandParent = mCAS.getOperById(parent.getParentID());
        if (grandParent == null) {
            mCAS.setOperEq(associatedElement);
            return mCAS.getOperEq();//we are on the main expression
        }
        final int indexOfParent = grandParent.getIndexOfArg(parent);
        grandParent.setArg(indexOfParent, associatedElement);

        return mCAS.getOperEq();
    }

    /*TODO Implement*/
    @Override
    public Operation dissociativeProperty(final Operation elementToDissociate) throws NotApplicableReductionException {
        final Operation parent = mCAS.getOperById(elementToDissociate.getParentID());

        if (parent == null) {
            throw new NotApplicableReductionException("No parent");
        }
        if (!parent.getOperId().equals(elementToDissociate.getOperId())) {
            throw new NotApplicableReductionException("Parent on dissociative property does not have the same symbol");
        }

        final Operation dissociatedElement = mCAS.disociate(parent, parent.getIndexOfArg(elementToDissociate));

        final Operation grandParent = mCAS.getOperById(parent.getParentID());

        if (grandParent == null) {
            mCAS.setOperEq(dissociatedElement);
            return mCAS.getOperEq();//we are on the main expression
        }

        final int indexOfParent = grandParent.getIndexOfArg(parent);
        grandParent.setArg(indexOfParent, dissociatedElement);

        return mCAS.getOperEq();
    }

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
    /*TODO: implement*/
    @Override
    public List<String> getSampleExpressions() {
        return mShowcaseExpressionList;
    }

    @Override
    public Operation createOperationFromString(final String CASExpression) {
        try {
            return mCAS.createOper(CASExpression);
        } catch (final Exception e) {
            Log.e(Utils.LOG_TAG, "Error on operation creation", e);
            return null;
        }
    }
}
