package es.uam.eps.tfg.app.tfgapp.util;

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

    /**
     * Decides if the operation is an invrse operation (1/exp)
     *
     * @param op operation to decide
     * @return true if the operation is an inverse one, false in other case
     */
    public static boolean isInverseOperation(final Operation op) {
        if (getStringOperatorSymbol(op).equals("1/")) {
            return true;
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
     * @param op operation to know its symbol representation
     * @return string represntation of an operation symbol
     */
    public static String getSymbolStringExpression(final Operation op) {
        final CASAdapter CAS = CASImplementation.getInstance();
        return CAS.getSymbolStringExpression(op);
    }

    /**
     * @param op operation to know if it's on the main level of the equation
     * @return tru if its parent or grandparent is the main equation, false if not
     */
    public static boolean isOnMainLevelOfEquation(final Operation op) {
        final String parentSymbol = CASUtils.getParentStringOperatorSymbol(op);
        final String grandParentSymbol = CASImplementation.getInstance().getGrandParentStringOperatorSymbol(op);
        if (parentSymbol != null) {
            if (parentSymbol.equals("=")) {
                return true;
            }
            if (grandParentSymbol != null) {
                if (grandParentSymbol.equals("=")) {
                    return true;
                }
            }
            return false;
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

        return "=[*[$[x],+[#[3],@INV[#[2],#[4]]]],@INV[-[$[x],#[5]]]]";
    }

    public static String createShortSampleExpression() {
        return "+[#[3],#[5]]";
    }

    public static String createMediumSampleExpression() {

        return "=[*[$[x],+[#[3],#[2],#[4]]],-[$[x],#[5]]]";
    }

    public static String createUltraLongSampleExpression() {

        return "=[*[$[a],$[x],+[#[9],#[3],@INV[+[#[2],#[4],#[34]]]]],@INV[-[$[x],#[5]]]]";
    }

    /**
     * @param op
     * @return
     */
    public static boolean isOnlyOperatorOrParenthesis(final Operation op) {
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
        int i = 0;
        if (isOnlyOperatorOrParenthesis(op)) {
            return op.getOperId();
        }
        for (final Operation arg : op.getArgs()) {
            if (arg.isNumber()) {
                sb.append(arg.getArgNumber() + "");
            } else if (arg.isString()) {
                sb.append(arg.getArgStr());
            } else if (isMathematicalOperation(arg)) {
                if (isInverseOperation(arg)) {
                    sb.append("( 1 / ");
                    sb.append(getInfixExpressionOf(arg) + "");
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
}
