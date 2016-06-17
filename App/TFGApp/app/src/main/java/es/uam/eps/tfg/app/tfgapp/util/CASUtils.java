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
     * Given an expression, returns the string parent operator symbol
     *
     * @param exp expression to know its parent operator symbol
     * @return string representation of the expression's parent operator, null if the expression is not an operation
     */
    public static String getParentStringOperatorSymbol(final Operation exp) {
        final CASAdapter CAS = CASImplementation.getInstance();
        return CAS.getParentStringOperatorSymbol(exp);
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

    public static String getSymbolicExpressionOf(final Operation op) {
        final CASAdapter CAS = CASImplementation.getInstance();
        return CAS.getSymbolicExpression(op);
    }
}
