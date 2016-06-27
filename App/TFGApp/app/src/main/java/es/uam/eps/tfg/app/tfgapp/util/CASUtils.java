package es.uam.eps.tfg.app.tfgapp.util;

import es.uam.eps.tfg.algebraicEngine.AlgebraicEngine;
import es.uam.eps.tfg.algebraicEngine.Operation;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASImplementation;

/**
 * CAS Utilities for using them without using the CAS directly
 */
public final class CASUtils {
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String M_ONE = "-1";
    public static final String INV_OP = "1/";

    private CASUtils() {
    }

    /**
     * Given an operation, decides if it's a mathematical operation or just a single element
     *
     * @param op operation to know if it's a mathematical operation
     * @return true if it is, false if not
     */
    public static boolean isMathematicalOperation(final Operation op) {
        final String opString = getStringOperatorSymbol(op);
        if (opString != null) {
            if (opString.equals(ZERO) || opString.equals(ONE) || opString.equals(M_ONE)) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNumber(final Operation op) {

        final String opString = getStringOperatorSymbol(op);
        if (opString != null) {
            if (opString.equals(ZERO) || opString.equals(ONE) || opString.equals(M_ONE)) {
                return true;
            }
            return false;

        }
        if (op.isNumber()) {
            return true;
        }
        if (op.isString()) {
            return false;
        }
        if (op.isOper()) {
            if (op.getRepresentationOperID().equals(AlgebraicEngine.Opers.NUMBER)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Given an expression, returns the string operator symbol
     *
     * @param exp expression to know its operator symbol
     * @return string representation of the expression operator, null if the expression is not an operation
     */
    public static String getStringOperatorSymbol(final Operation exp) {
        final CASAdapter CAS = CASImplementation.getInstance();

        return CAS.getStringOperatorSymbol(exp);
    }

    /**
     * Decides if the operation is an inverse operation (1/exp)
     *
     * @param op operation to decide
     * @return true if the operation is an inverse one, false in other case
     */
    public static boolean isInverseOperation(final Operation op) {
        if (op == null) {
            return false;
        }
        if (INV_OP.equals(getStringOperatorSymbol(op))) {
            return true;
        }
        return false;
    }

    /**
     * Decides if the operation is a minus operation (-exp)
     *
     * @param op operation to decide
     * @return true if the operation is an inverse one, false in other case
     */
    public static boolean isMinusOperation(final Operation op) {
        if (AlgebraicEngine.Opers.MINUS.getSymbol().equals(getStringOperatorSymbol(op))) {
            return true;
        }
        return false;
    }

    public static boolean isVariable(final Operation op) {

        if (AlgebraicEngine.Opers.VAR.equals(op.getRepresentationOperID()) || op.isString()) {
            return true;
        }
        return false;
    }

    /**
     * @param op operation to know its symbol representation
     * @return string representation of an operation symbol
     */
    public static String getSymbolStringExpression(final Operation op) {
        final CASAdapter CAS = CASImplementation.getInstance();
        return CAS.getSymbolStringExpression(op);
    }

    /**
     * @param op operation to know if it's on the main level of the equation
     * @return true if its parent or grandparent is the main equation, false if not
     */
    public static boolean isOnMainLevelOfEquation(final Operation op) {
        if (isSideOfEquation(op)) {
            return true;
        }
        return isMainTermOfEquation(op);

    }

    /**
     * @param op operation to know if its grandparent is the equation
     * @return true if the operation it's grandchildren of the equation
     */
    public static boolean isMainTermOfEquation(final Operation op) {
        final String grandParentSymbol = CASImplementation.getInstance().getGrandParentStringOperatorSymbol(op);

        if (grandParentSymbol != null) {
            if (grandParentSymbol.equals(AlgebraicEngine.Opers.EQU.getSymbol())) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param op operation to know if it's one of the two sides of an equation
     * @return true if the operation it's a whole side of the equation
     */
    public static boolean isSideOfEquation(final Operation op) {
        final String parentSymbol = CASUtils.getParentStringOperatorSymbol(op);
        if (parentSymbol != null) {
            if (parentSymbol.equals(AlgebraicEngine.Opers.EQU.getSymbol())) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean minusOperationHasSubexpressions(final Operation op) {
        //assuming that op is minus operation
        final Operation arg0 = op.getArg(0);
        if (isMathematicalOperation(arg0)) {
            if (!isMinusOperation(arg0)) {
                return true;
            } else {
                return minusOperationHasSubexpressions(arg0);
            }
        }
        return false;
    }

    /**
     * Given an expression, returns the string parent operator symbol
     *
     * @param exp expression to know its parent operator symbol
     * @return string representation of the expression's parent operator, null if the expression is not an operation
     */
    public static String getParentStringOperatorSymbol(final Operation exp) {
        final CASAdapter CAS = CASImplementation.getInstance();
        return CAS.getParentStringOperatorSymbol(exp);
    }

    public static String createLongSampleExpression() {

        return "=[*[$[x],+[-[#[8]],+[#[2],#[4]]],@INV[#[3]]],@INV[+[$[x],-[#[5]]]]]";
    }

    public static String createShortSampleExpression() {
        return "#[3.6663]";
    }

    public static String createMediumSampleExpression() {
        return "=[+[*[#[15],$[x]],&ONE[]],+[#[31],*[#[5],$[x]]]]";
    }

    public static String createUltraLongSampleExpression() {

        //return "=[*[$[a],$[x],+[#[9],#[3],@INV[+[#[2],#[4],#[34]]]]],@INV[+[$[x],-[#[5]]]]]";
        return "=[+[#[3],#[3],*[#[3],#[7],#[8]],#[4],*[#[3],#[2],#[4]]],$[x]]";
        //return "=[+[#[3],&MONE[],*[#[3],#[2]]],$[x]]";
    }

    /**
     * @param op
     * @return
     */
    public static boolean isOnlyOperatorOrParenthesis(final Operation op) {
        if (op.getOperId() == null) {
            return false;
        }
        if (op.getArgs().size() == 0) {
            if (isMathematicalOperation(op) || op.getOperId().equals("(") || op.getOperId().equals(")")) {
                return true;
            }
        }
        return false;

    }

    /**
     * Returns the infix notation for an operation
     *
     * @param op operation to transform
     * @return string representation in infix form
     */
    public static String getInfixExpressionOf(final Operation op) {
        final StringBuilder sb = new StringBuilder();
        final boolean inverse = false;
        int i = 0;
        if (isOnlyOperatorOrParenthesis(op)) {
            return op.getOperId();
        }
        if (isConstant(op)) {
            return getStringRepresentationOfConstant(op);
        }
        for (final Operation arg : op.getArgs()) {

            if (isConstant(arg)) {
                sb.append(getStringRepresentationOfConstant(arg));
            } else if (arg.isString()) {
                sb.append(arg.getArgStr());
            } else if (isMathematicalOperation(arg)) {
                if (isInverseOperation(arg)) {
                    sb.append("( 1 / ");
                    sb.append(getInfixExpressionOf(arg) + ")");
                } else if (isMinusOperation(arg)) {
                    sb.append("(-");
                    sb.append(getInfixExpressionOf(arg) + ")");
                } else {
                    sb.append("(" + getInfixExpressionOf(arg) + ")");
                }
            } else {
                sb.append(arg.getArg(0).toString());
            }
            if (i != op.getArgs().size() - 1) {
                sb.append(" " + getStringOperatorSymbol(op) + " ");
            }
            i++;
        }

        return sb.toString();
    }

    /**
     * Returns the infix notation for an string represtantion in the CAS form
     *
     * @param CASExpression string representation of the operation to transform
     * @return string representation in infix form
     */
    public static String getInfixExpressionOf(final String CASExpression) {
        final Operation op = CASImplementation.getInstance().createOperationFromString(CASExpression);
        return getInfixExpressionOf(op);
    }

    /**
     * Returns the expression of a constant
     *
     * @param op operation to know its constant string
     * @return constant as a string
     */
    private static String getStringRepresentationOfConstant(final Operation op) {
        final String opString = getStringOperatorSymbol(op);
        if (opString != null) {
            if (opString.equals(ZERO)) {
                return "0";
            } else if (opString.equals(ONE)) {
                return "1";
            } else if (opString.equals(M_ONE)) {
                return "(-1)";
            }
            return null;
        }
        if (op.isNumber()) {
            return op.getArgNumber() + "";
        }
        return null;
    }


    private static boolean isConstant(final Operation op) {
        if (op.isNumber()) {
            return true;
        }
        final String opString = getStringOperatorSymbol(op);
        if (opString != null) {
            if (opString.equals(ZERO) || opString.equals(ONE) || opString.equals(M_ONE)) {
                return true;
            }
            return false;

        }

        return false;
    }


    public static boolean isMinusOne(final Operation op) {
        if (op.getOperId() == null) {
            return false;
        }
        if (op.getOperId().equals("MONE")) {
            return true;
        }
        return false;
    }

    public static int getSideOfEquation(final Operation oper) {
        if (isMainTermOfEquation(oper)) {
            final Operation parent = CASImplementation.getInstance().getOperationById(oper.getParentID());
            final Operation grandparent = CASImplementation.getInstance().getOperationById(parent.getParentID());
            final int indexOfElementToChangeInParent = grandparent.getIndexOfArg(parent);
            return indexOfElementToChangeInParent;

        } else if (isSideOfEquation(oper)) {
            final Operation parent = CASImplementation.getInstance().getOperationById(oper.getParentID());
            final int indexOfElementToChangeInParent = parent.getIndexOfArg(oper);
            return indexOfElementToChangeInParent;
        }
        return -1;
    }
}
