package es.uam.eps.tfg.app.tfgapp.controller.listeners;

import java.util.List;

import es.uam.eps.expressions.types.interfaces.Expression;

/**
 * Listener for doing actions when a expression is selected
 */
public interface OnExpressionActionListener {

    void onSingleExpressionSelected(Expression global,Expression selected);

    void onMultipleExpressionSelected(Expression global, List<Expression> selection);

}
