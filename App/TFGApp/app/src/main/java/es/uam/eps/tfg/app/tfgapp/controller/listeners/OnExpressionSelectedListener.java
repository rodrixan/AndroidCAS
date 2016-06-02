package es.uam.eps.tfg.app.tfgapp.controller.listeners;

import es.uam.eps.expressions.types.interfaces.Expression;

/**
 * Listener for doing actions when a expression is selected
 */
public interface OnExpressionSelectedListener {
    /**
     * @param exp the expression which was selected
     */
    void onExpressionSelected(Expression exp);
}
