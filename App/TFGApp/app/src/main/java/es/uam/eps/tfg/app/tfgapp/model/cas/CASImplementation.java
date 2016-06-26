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
 * Implementation of the CASAdapter. Singleton pattern.
 */
public class CASImplementation implements CASAdapter {
    private static final String DEFAULT_SYMBOL = "o-";
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

        final AlgebraicEngine.Opers symbol = exp.getRepresentationOperID();

        //when custom operation creation
        if (exp.isNumber() || exp.isString()) {
            return null;
        }
        if (symbol == null) {
            return DEFAULT_SYMBOL;
        }

        switch (symbol) {
            case NUMBER:
            case VAR:
                return null;
            case ZERO:
                return CASUtils.ZERO;
            case ONE:
                return CASUtils.ONE;
            case MONE:
                return CASUtils.M_ONE;
            case SUM:
            case PROD:
            case EQU:
            case MINUS:
                return symbol.getSymbol();
            case INV:
                return CASUtils.INV_OP;
            default:
                return null;
        }
    }

    @Override
    public Operation commutativeProperty(final Operation elementToCommute, final Actions leftOrRight) throws NotApplicableReductionException {
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
            if (exp.isNumber()) {
                return exp.getArgNumber() + "";
            }
            if (exp.isString()) {
                return exp.getArgStr();
            }
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

    @Override
    public Operation operate(final Operation selection) throws NotApplicableReductionException {

        final UUID parentId = selection.getParentID();
        final Operation parent = mCAS.getOperById(parentId);
        if (parent == null) {
            throw new NotApplicableReductionException("No parent");
        }

        final int indexOfElement = parent.getIndexOfArg(selection);

        Operation res = operateRec(selection);
        if (res == null) {
            throw new NotApplicableReductionException("Null on operation");
        }
        final float numberRes = res.getArg(0).getArgNumber();
        if (numberRes == 0) {
            res = new Operation(AlgebraicEngine.Opers.ZERO.toString());
        } else if (numberRes == 1) {
            res = new Operation(AlgebraicEngine.Opers.ONE.toString());
        } else if (numberRes == -1) {
            res = new Operation(AlgebraicEngine.Opers.MONE.toString());
        } else if (numberRes < 0) {
            res = createNegativeNumber(numberRes);
        }
        res.setParentIdsRec(parentId);
        parent.setArg(indexOfElement, res);

        return mCAS.getOperEq();
    }

    @Override
    public Operation operate(final List<Operation> commonElems) throws NotApplicableReductionException {

        applyCommonFactor(commonElems);

        return null;
    }

    private Operation applyCommonFactor(final List<Operation> commonElements) throws NotApplicableReductionException {
        final Operation sumOperation = canApplyCommonFactor(commonElements);
        if (sumOperation == null) {
            throw new NotApplicableReductionException("Can't apply common factor");
        }

        Operation pivot = mCAS.getOperById(commonElements.get(0).getParentID());
        Operation grandParent = mCAS.getOperById(pivot.getParentID());
        final int indexParent = grandParent.getIndexOfArg(pivot);
        final Operation greatGranParent = mCAS.getOperById(grandParent.getParentID());
        final int indexGrandParent = greatGranParent.getIndexOfArg(grandParent);
        final int indexOfCommonElement0 = pivot.getIndexOfArg(commonElements.get(0));
        if (indexOfCommonElement0 != pivot.getNumberArgs() - 1) {
            //move to the end
            pivot = mCAS.commute(pivot, indexOfCommonElement0, pivot.getNumberArgs() - 1);
            grandParent.setArg(indexParent, pivot);
        }
        if (pivot.getNumberArgs() > 2) {
            //associate
            pivot = mCAS.associate(pivot, 0, pivot.getNumberArgs() - 2);
            grandParent.setArg(indexParent, pivot);
        }

        //insert the new element in grandpa
        grandParent = mCAS.commute(grandParent, grandParent.getIndexOfArg(pivot), 0);
        greatGranParent.setArg(indexGrandParent, grandParent);

        for (int i = 1; i < commonElements.size(); i++) {

            Operation nextElement = mCAS.getOperById(commonElements.get(i).getParentID());
            final int indexOfNextCommonElement = nextElement.getIndexOfArg(commonElements.get(i));
            if (indexOfNextCommonElement != nextElement.getNumberArgs() - 1) {
                final int indexOfNextCommonOper = grandParent.getIndexOfArg(nextElement);
                //move to the end
                nextElement = mCAS.commute(nextElement, indexOfNextCommonElement, nextElement.getNumberArgs() - 1);
                grandParent.setArg(indexOfNextCommonOper, nextElement);
            }
            if (nextElement.getNumberArgs() > 2) {
                final int indexOfNextCommonOper = grandParent.getIndexOfArg(nextElement);
                //associate
                nextElement = mCAS.associate(nextElement, 0, nextElement.getNumberArgs() - 2);
                grandParent.setArg(indexOfNextCommonOper, nextElement);
            }

            //insert the new element in grandpa and associate
            grandParent = mCAS.commute(grandParent, grandParent.getIndexOfArg(nextElement), 1);
            greatGranParent.setArg(indexGrandParent, grandParent);

            grandParent = mCAS.associate(grandParent, 0, 1);
            greatGranParent.setArg(indexGrandParent, grandParent);
            //reduction19 => commonfactor
            pivot = mCAS.reduction19(grandParent.getArg(0));
            grandParent.setArg(0, pivot);
        }

        grandParent.setArg(0, pivot);
        greatGranParent.setArg(indexGrandParent, grandParent);

        return mCAS.getOperEq();
    }

    private Operation canApplyCommonFactor(final List<Operation> commonElements) {
        if (!allElementsAreTheSame(commonElements)) {
            return null;
        }
        Operation sumOperation = null;
        final List<Operation> grandpaOrphanList = new ArrayList<>();

        for (final Operation op : commonElements) {

            final Operation parent = mCAS.getOperById(op.getParentID());

            if (parent.getOperId().equals("PROD")) {
                final Operation currentGrandpa = mCAS.getOperById(parent.getParentID());

                if (!currentGrandpa.getOperId().equals("SUM")) {
                    return null;
                }

                if (sumOperation == null) {
                    sumOperation = (Operation) currentGrandpa.clone();
                }

            } else if (parent.getOperId().equals("SUM")) {

                if (sumOperation == null) {
                    grandpaOrphanList.add(op);

                } else if (parent.getId().equals(sumOperation.getId())) {

                    //reduction8 => a= 1*a
                    final Operation multipliedByOneOp = mCAS.reduction8(op);
                    commonElements.set(commonElements.indexOf(op), multipliedByOneOp.getArg(1));
                    //replace the element in the cloned expression
                    sumOperation.getArgs().set(sumOperation.getIndexOfArg(op), multipliedByOneOp);

                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        //all fine, single elements as (1*elem)
        if (sumOperation == null) {
            return null; //no grandpa found
        }

        if (!grandpaOrphanList.isEmpty()) {

            for (final Operation op : grandpaOrphanList) {
                //reduction8 => a= 1*a
                final Operation multipliedByOneOp = mCAS.reduction8(op);
                commonElements.set(commonElements.indexOf(op), multipliedByOneOp.getArg(1));
                //replace the element in the cloned expression
                sumOperation.getArgs().set(sumOperation.getIndexOfArg(op), multipliedByOneOp);
            }
        }

        //at this point, we can apply common factor to the sum operation
        return sumOperation;
    }

    private boolean allElementsAreTheSame(final List<Operation> operList) {
        final Operation first = operList.get(0);
        for (int i = 1; i < operList.size() - 1; i++) {
            if (!first.genericEquals(operList.get(i))) {
                return false;
            }
        }
        return true;
    }

    private Operation operateRec(final Operation oper) throws NotApplicableReductionException {

        Operation res;

        for (final Operation arg : oper.getArgs()) {
            if (CASUtils.isVariable(arg)) {
                throw new NotApplicableReductionException("Hay variables");
            }
            if (CASUtils.isMathematicalOperation(arg)) {

                res = operateRec(arg);

                if (res == null) {
                    return null;
                }

                oper.setArg(oper.getIndexOfArg(arg), res);
            }
        }

        if (allArgsAreNumbers(oper)) {
            return calculate(oper);
        }

        return null;
    }

    private Operation calculateVarRes(final Operation oper) {

        final String operId = oper.getOperId();

        switch (operId) {
            case "SUM":
                return null;
            case "PROD":
                return null;
            case "INV":
                return null;
            case "MINUS":
                return null;
            default:
                return null;
        }

    }

    private Operation calculateRes(final Operation oper) throws NotApplicableReductionException {
        final String operId = oper.getOperId();
        final Operation result;
        switch (operId) {
            case "SUM":
                //CERO
                if (oper.getArg(0).getOperId().equals("ZERO")) {
                    final Operation commuted = mCAS.commute(oper, 0, 1);
                    result = mCAS.reduction3(commuted);
                } else if (oper.getArg(1).getOperId().equals("ZERO")) {
                    result = mCAS.reduction3(oper);
                }
                //UNO
                else if (oper.getArg(0).getOperId().equals("ONE")) {
                    if (oper.getArg(1).getOperId().equals("ONE")) {
                        result = mCAS.reduction2(oper);
                    } else {
                        final Operation commuted = mCAS.commute(oper, 0, 1);
                        result = mCAS.reduction1(commuted);
                    }
                } else if (oper.getArg(1).getOperId().equals("ONE")) {
                    result = mCAS.reduction1(oper);
                }
                //MENOS UNO
                else if (oper.getArg(0).getOperId().equals("MONE")) {
                    if (oper.getArg(1).getOperId().equals("MONE")) {
                        result = mCAS.reduction34(oper);
                    } else {
                        final Operation commuted = mCAS.commute(oper, 0, 1);
                        result = mCAS.reduction33(commuted);
                    }
                } else if (oper.getArg(1).getOperId().equals("MONE")) {
                    result = mCAS.reduction33(oper);
                } else {
                    result = mCAS.reduction0(oper);
                }
                break;
            case "PROD":
                //CERO
                if (oper.getArg(0).getOperId().equals("ZERO")) {
                    final Operation commuted = mCAS.commute(oper, 0, 1);
                    result = mCAS.reduction6(commuted);
                } else if (oper.getArg(1).getOperId().equals("ZERO")) {
                    result = mCAS.reduction6(oper);
                }
                //UNO
                else if (oper.getArg(0).getOperId().equals("ONE")) {
                    final Operation commuted = mCAS.commute(oper, 0, 1);
                    result = mCAS.reduction7(commuted);
                } else if (oper.getArg(1).getOperId().equals("ONE")) {
                    result = mCAS.reduction7(oper);
                }
                //MENOS UNO
                else if (oper.getArg(0).getOperId().equals("MONE")) {
                    final Operation commuted = mCAS.commute(oper, 0, 1);
                    result = mCAS.reduction35(commuted);
                } else if (oper.getArg(1).getOperId().equals("ONE")) {
                    result = mCAS.reduction35(oper);
                } else {
                    result = mCAS.reduction5(oper);
                }
                break;
            case "INV":
                //UNO
                if (oper.getArg(0).getOperId().equals("ONE")) {
                    result = mCAS.reduction24(oper);
                }
                //MENOS UNO
                else if (oper.getArg(0).getOperId().equals("MONE")) {
                    result = mCAS.reduction25(oper);
                }
                //CERO
                else if (oper.getArg(0).getOperId().equals("ZERO")) {
                    throw new NotApplicableReductionException("INFINITY!!Trying to destroy the world??¬¬");
                } else {
                    result = mCAS.reduction23(oper);
                }
                break;
            case "MINUS":
                if (oper.getArg(0).getOperId().equals("ONE")) {
                    result = mCAS.reduction12(oper);
                } else if (oper.getArg(0).getOperId().equals("MONE")) {
                    result = mCAS.reduction13(oper);
                } else if (oper.getArg(0).getOperId().equals("ZERO")) {
                    result = mCAS.reduction11(oper);
                } else {
                    result = mCAS.reduction10(oper);
                }
                break;
            default:
                return null;
        }

        return result;

    }

    private Operation createNegativeNumber(final float number) {
        final Operation negNumber = new Operation(AlgebraicEngine.Opers.MINUS.toString());
        final Operation numberOp = new Operation(AlgebraicEngine.Opers.NUMBER.toString());
        numberOp.addArg(new Operation(Math.abs(number)));
        negNumber.addArg(numberOp);
        return negNumber;
    }

    private Operation calculate(Operation oper) throws NotApplicableReductionException {

        final List<Operation> args = oper.getArgs();

        if (CASUtils.isInverseOperation(oper) || CASUtils.isInverseOperation(oper) || args.size() == 2) {
            return calculateRes(oper);
        }

        Operation pivot;

        while (oper.getNumberArgs() > 2) {
            oper = mCAS.associate(oper, 0, 1);
            pivot = calculateRes(oper.getArg(0));
            oper.setArg(0, pivot);
        }

        return calculateRes(oper);
    }

    private boolean allArgsAreNumbers(final Operation selection) {
        if (!CASUtils.isMathematicalOperation(selection)) {
            return false;
        }

        for (final Operation arg : selection.getArgs()) {
            if (!CASUtils.isNumber(arg)) {
                return false;
            }
        }
        return true;
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
