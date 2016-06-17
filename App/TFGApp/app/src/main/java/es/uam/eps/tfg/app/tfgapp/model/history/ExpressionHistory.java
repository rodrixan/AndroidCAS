package es.uam.eps.tfg.app.tfgapp.model.history;

import java.util.List;

import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;

/**
 * Repository for the expression history
 */
public interface ExpressionHistory {
    List<ExpressionRecord> getHistory();

    void addExpression(CASAdapter.Actions action, String global, String selection);

    String returnToPreviousExpression();


    int getRecordCount();
}
