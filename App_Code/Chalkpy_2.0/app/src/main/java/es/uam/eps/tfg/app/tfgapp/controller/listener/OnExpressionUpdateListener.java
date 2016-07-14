package es.uam.eps.tfg.app.tfgapp.controller.listener;

import es.uam.eps.tfg.algebraicEngine.Operation;

/**
 * Used for telling the view that the expression has changed
 */
public interface OnExpressionUpdateListener {
    /**
     * @param exp the updated expression
     */
    void onExpressionUpdated(Operation exp);
}
