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
    public Operation getOperationById(final UUID id) {
        return mCAS.getOperById(id);
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

    private Operation operateRec(final Operation oper) throws NotApplicableReductionException {

        Operation res;
        final List<Integer> variableIndexes = new ArrayList<>();

        for (final Operation arg : oper.getArgs()) {
            if (CASUtils.isVariable(arg)) {
                //throw new NotApplicableReductionException("There are variables");
                variableIndexes.add(oper.getIndexOfArg(arg));
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
        } else {
            //associate variables and numbers
            Operation commutedOper = oper;
            for (final Integer varIndex : variableIndexes) {
                commutedOper = mCAS.commute(commutedOper, varIndex, 0);
            }
            //TODO fix this
        }

        return null;
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

    private Operation calculateRes(final Operation oper) throws NotApplicableReductionException {
        final String operId = oper.getOperId();
        final Operation result;
        final String operArgId0 = (oper.getArg(0) != null) ? oper.getArg(0).getOperId() : null;
        final String operArgId1 = (oper.getArg(1) != null) ? oper.getArg(1).getOperId() : null;

        switch (operId) {
            case "SUM":
                result = operateSum(oper, operArgId0, operArgId1);
                break;
            case "PROD":
                result = operateProd(oper, operArgId0, operArgId1);
                break;
            case "INV":
                result = operateInverse(oper, operArgId0);
                break;
            case "MINUS":
                result = operateMinus(oper, operArgId0);
                break;
            default:
                return null;
        }
        return result;
    }

    private Operation operateSum(final Operation oper, final String operArgId0, final String operArgId1) throws NotApplicableReductionException {
        final Operation result;

        /**
         * Not in separated functions as each 'if' depends on the other ones
         */

        //ZERO
        if (operArgId0.equals("ZERO")) {
            final Operation commuted = mCAS.commute(oper, 0, 1);
            result = mCAS.reduction3(commuted);
        } else if (operArgId1.equals("ZERO")) {
            result = mCAS.reduction3(oper);
        }
        //ONE
        else if (operArgId0.equals("ONE")) {
            if (operArgId1.equals("ONE")) {
                result = mCAS.reduction2(oper);
            } else {
                final Operation commuted = mCAS.commute(oper, 0, 1);
                result = mCAS.reduction1(commuted);
            }
        } else if (operArgId1.equals("ONE")) {
            result = mCAS.reduction1(oper);
        }
        //MINUS
        else if (operArgId0.equals("MONE")) {

            if (operArgId1.equals("MONE")) {
                result = mCAS.reduction34(oper);
            } else {
                final Operation commuted = mCAS.commute(oper, 0, 1);
                result = mCAS.reduction33(commuted);
            }
        } else if (operArgId1.equals("MONE")) {
            result = mCAS.reduction33(oper);
        } else {
            result = mCAS.reduction0(oper);
        }
        return result;
    }

    private Operation operateProd(final Operation oper, final String operArgId0, final String operArgId1) throws NotApplicableReductionException {
        final Operation result;//ZERO
        if (operArgId0.equals("ZERO")) {
            final Operation commuted = mCAS.commute(oper, 0, 1);
            result = mCAS.reduction6(commuted);
        } else if (operArgId1.equals("ZERO")) {
            result = mCAS.reduction6(oper);
        }
        //ONE
        else if (operArgId0.equals("ONE")) {
            final Operation commuted = mCAS.commute(oper, 0, 1);
            result = mCAS.reduction7(commuted);
        } else if (operArgId1.equals("ONE")) {
            result = mCAS.reduction7(oper);
        }
        //MINUS ONE
        else if (operArgId0.equals("MONE")) {
            final Operation commuted = mCAS.commute(oper, 0, 1);
            result = mCAS.reduction35(commuted);
        } else if (operArgId1.equals("ONE")) {
            result = mCAS.reduction35(oper);
        } else {
            result = mCAS.reduction5(oper);
        }
        return result;
    }

    private Operation operateInverse(final Operation oper, final String operArgId0) throws NotApplicableReductionException {
        final Operation result;//ONE
        if (operArgId0.equals("ONE")) {
            result = mCAS.reduction24(oper);
        }
        //MINUS ONE
        else if (operArgId0.equals("MONE")) {
            result = mCAS.reduction25(oper);
        }
        //ZERO
        else if (operArgId0.equals("ZERO")) {
            throw new NotApplicableReductionException("INFINITY!! Trying to destroy the world?? ¬¬");
        } else {
            result = mCAS.reduction23(oper);
        }
        return result;
    }

    private Operation operateMinus(final Operation oper, final String operArgId0) {
        final Operation result;

        if (operArgId0.equals("ONE")) {
            result = mCAS.reduction12(oper);
        } else if (operArgId0.equals("MONE")) {
            result = mCAS.reduction13(oper);
        } else if (operArgId0.equals("ZERO")) {
            result = mCAS.reduction11(oper);
        } else {
            result = mCAS.reduction10(oper);
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

    @Override
    public Operation commonFactor(final List<Operation> commonElements) throws NotApplicableReductionException {

        final Operation sumOperation = canApplyCommonFactor(commonElements);
        if (sumOperation == null) {
            throw new NotApplicableReductionException("Can't apply common factor");
        }

        return applyCommonFactor(commonElements);
    }

    private Operation canApplyCommonFactor(final List<Operation> commonElements) {
        if (!allElementsAreTheSame(commonElements)) {
            return null;
        }
        Operation sumOperation = null;
        final List<Operation> grandpaOrphanList = new ArrayList<>();

        for (final Operation op : commonElements) {
            final Operation parent = mCAS.getOperById(op.getParentID());
            final String parentOperId = parent.getOperId();
            if (parentOperId.equals("PROD")) {
                final Operation currentGrandpa = mCAS.getOperById(parent.getParentID());

                if (!currentGrandpa.getOperId().equals("SUM")) {
                    return null;
                }

                if (sumOperation == null) {
                    sumOperation = (Operation) currentGrandpa.clone();
                }

            } else if (parentOperId.equals("SUM")) {

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

        final boolean orphansLeft = !grandpaOrphanList.isEmpty();
        if (sumOperation == null) {
            if (orphansLeft) {
                sumOperation = (Operation) mCAS.getOperById(commonElements.get(0).getParentID()).clone();
            } else {
                return null; //no grandpa found
            }
        }

        if (orphansLeft) {

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

    private Operation applyCommonFactor(final List<Operation> commonElements) throws NotApplicableReductionException {

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

    @Override
    public Operation changeSide(final Operation selection) throws NotApplicableReductionException {
        if (!canChangeSide(selection)) {
            throw new NotApplicableReductionException("Can't change side of the equation");
        }
        return changeSideOfEquation(selection);
    }

    private boolean canChangeSide(final Operation op) {
        if (!CASUtils.isOnMainLevelOfEquation(op)) {
            return false;
        }
        return true;
    }

    private Operation changeSideOfEquation(final Operation elementToChange) throws NotApplicableReductionException {
        final Operation parent = mCAS.getOperById(elementToChange.getParentID());
        final int indexOfElementToChangeInParent = parent.getIndexOfArg(elementToChange);
        final String parentOperdId = parent.getOperId();

        if (tryToChangeZero(elementToChange, parentOperdId)) {
            throw new NotApplicableReductionException("Trying to divide by zero");
        }

        if (CASUtils.isMainTermOfEquation(elementToChange)) {

            final boolean isTermNegative = CASUtils.isMinusOperation(elementToChange);
            final boolean isTermInverse = CASUtils.isInverseOperation(elementToChange);

            //move to the end
            final int finalPosition = parent.getNumberArgs() - 1;
            final Operation commutedOperation = mCAS.commute(parent, indexOfElementToChangeInParent, finalPosition);
            final Operation grandParent = mCAS.getOperById(parent.getParentID());//must be the equal
            final int indexOfParent = grandParent.getIndexOfArg(parent);
            grandParent.setArg(indexOfParent, commutedOperation);

            //associate
            Operation associatedElement = commutedOperation;
            if (commutedOperation.getNumberArgs() > 2) {
                associatedElement = mCAS.associate(commutedOperation, 0, finalPosition - 1);
            }

            grandParent.setArg(indexOfParent, associatedElement);

            final int sideOfEquation = CASUtils.getSideOfEquation(elementToChange);

            if (parentOperdId.equals(AlgebraicEngine.Opers.SUM.toString())) {

                if (sideOfEquation == 0) {
                    if (isTermNegative) {
                        final Operation finalExp = mCAS.reduction38(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    } else {
                        final Operation finalExp = mCAS.reduction30(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    }
                } else if (sideOfEquation == 1) {
                    if (isTermNegative) {
                        final Operation finalExp = mCAS.reduction43(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    } else {
                        final Operation finalExp = mCAS.reduction41(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    }
                } else {
                    throw new NotApplicableReductionException("Bad index: not in a side of equation");
                }

            } else if (parentOperdId.equals(AlgebraicEngine.Opers.PROD.toString())) {

                if (sideOfEquation == 0) {
                    if (isTermInverse) {
                        final Operation finalExp = mCAS.reduction44(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    } else {
                        final Operation finalExp = mCAS.reduction32(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    }
                } else if (sideOfEquation == 1) {
                    if (isTermInverse) {
                        final Operation finalExp = mCAS.reduction45(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    } else {
                        final Operation finalExp = mCAS.reduction42(grandParent);
                        mCAS.setOperEq(finalExp);
                        return finalExp;
                    }
                } else {
                    throw new NotApplicableReductionException("Bad index: not in a side of equation");
                }
            } else {
                throw new NotApplicableReductionException("Can't change side, parent not a sum or product");
            }

        } else if (CASUtils.isSideOfEquation(elementToChange)) {

            Operation finalExp = null;
            if (indexOfElementToChangeInParent == 0) {

                final Operation changedExpression = mCAS.reduction39(parent);
                mCAS.setOperEq(changedExpression);
                finalExp = mCAS.getOperEq();

            } else if (indexOfElementToChangeInParent == 1) {

                final Operation changedExpression = mCAS.reduction40(parent);
                mCAS.setOperEq(changedExpression);
                finalExp = mCAS.getOperEq();
            }

            return finalExp;
        }

        return null;
    }

    private boolean tryToChangeZero(final Operation elemToChange, final String parentOperId) {
        if (parentOperId.equals(AlgebraicEngine.Opers.PROD.toString())) {
            if (elemToChange.getOperId().equals(AlgebraicEngine.Opers.ZERO.toString())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Operation distribute(final Operation elemToDistribute, final Operation sumOperation) throws NotApplicableReductionException {
        if (!isOnDistributiveForm(elemToDistribute, sumOperation)) {
            throw new NotApplicableReductionException("Bad distributive form");
        }

        final Operation parent = mCAS.getOperById(elemToDistribute.getParentID());

        final Operation grandParent = mCAS.getOperById(parent.getParentID());

        final int indexOfParentInGrandpa = grandParent.getIndexOfArg(parent);
        final int indexOfSingleElem = parent.getIndexOfArg(elemToDistribute);
        final int indexOfSum = parent.getIndexOfArg(sumOperation);

        //commute
        final Operation commutedExpAux = mCAS.commute(parent, indexOfSingleElem, 0);

        grandParent.setArg(indexOfParentInGrandpa, commutedExpAux);

        //final Operation commutedExp = mCAS.commute(commutedExpAux, indexOfSum, 1);

        //r20
        final Operation distributedExp = mCAS.reduction20(commutedExpAux);

        grandParent.setArg(indexOfParentInGrandpa, distributedExp);

        return mCAS.getOperEq();
    }

    @Override
    public boolean isOnDistributiveForm(final Operation singleElem, final Operation sumOperation) {
        final UUID parentId = singleElem.getParentID();

        //same parent
        if (!parentId.equals(sumOperation.getParentID())) {
            return false;
        }
        final Operation parent = mCAS.getOperById(parentId);

        //parent is a product
        if (!AlgebraicEngine.Opers.PROD.toString().equals(parent.getOperId())) {
            return false;
        }

        //second one must be a sum
        if (!AlgebraicEngine.Opers.SUM.toString().equals(sumOperation.getOperId())) {
            return false;
        }

        //single element must be a number or a minus with a single number
        if (!AlgebraicEngine.Opers.NUMBER.toString().equals(singleElem.getOperId())) {
            if (AlgebraicEngine.Opers.MINUS.toString().equals(singleElem.getOperId())) {
                if (CASUtils.minusOperationHasSubexpressions(singleElem)) {
                    return false;
                }
                return true;
            }
            return false;
        }
        return true;
    }

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

}
