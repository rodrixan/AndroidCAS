package es.uam.eps.tfg.app.tfgapp.util;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASImplementation;

/**
 * CAS Utilities for using them without using the CAS directly
 */
public final class CASUtils {

    private CASUtils() {
    }

    /**
     * Given an expression, returns the string operator symbol
     *
     * @param exp expression to know its operator symbol
     * @return string representation of the expression operator, null if the expression is not an operation
     */
    public static String getStringOperatorSymbol(final Expression exp) {
        final CASAdapter CAS = CASImplementation.getInstance();

        return CAS.getStringOperatorSymbol(exp);
    }

    /**
     * Given an expression, returns the string parent operator symbol
     *
     * @param exp expression to know its parent operator symbol
     * @return string representation of the expression's parent operator, null if the expression is not an operation
     */
    public static String getParentStringOperatorSymbol(final Expression exp) {
        final CASAdapter CAS = CASImplementation.getInstance();
        return CAS.getParentStringOperatorSymbol(exp);
    }

}
