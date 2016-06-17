package es.uam.eps.tfg.app.tfgapp.model.history;

import java.util.List;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;

/**
 * Repository for the expression history
 */
public interface ExpressionHistory {
    List<ExpressionRecord> getHistory();

    /*TODO cambiar por string*/
    void addExpression(CASAdapter.Actions action, Expression global, Expression selection);

    Expression returnToPreviousExpression();


    int getRecordCount();
}
