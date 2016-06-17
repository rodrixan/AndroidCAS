package es.uam.eps.tfg.app.tfgapp.controller.listeners;

import java.util.List;

import es.uam.eps.expressions.types.interfaces.Expression;

/**
 * Listener for doing actions when a expression is selected
 */
public interface OnExpressionActionListener {

    /**
     * Single selection of an item of an expression
     *
     * @param selected the selected item
     */
    void onSingleExpressionSelected(Expression selected);

    /**
     * Multiple selection of several items of an expression
     *
     * @param selection list with the multiple items
     */
    void onMultipleExpressionSelected(List<Expression> selection);

}
