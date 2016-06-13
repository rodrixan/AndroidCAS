package es.uam.eps.tfg.app.tfgapp.model.history;

import java.util.List;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;

public interface ExpressionHistory {
    List<ExpressionRecord> getHistory();

    void addExpression(CASAdapter.Actions action, Expression global, Expression selection);

    Expression getLastExpression();
    Expression getFirstExpression();

    int getRecordCount();
}
