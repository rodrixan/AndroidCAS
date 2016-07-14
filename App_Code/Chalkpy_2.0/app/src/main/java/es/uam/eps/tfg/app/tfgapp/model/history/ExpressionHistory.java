package es.uam.eps.tfg.app.tfgapp.model.history;

import java.util.List;

import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;

/**
 * Repository for the expression history
 */
public interface ExpressionHistory {
    /**
     * @return All the previous expressions
     */
    List<ExpressionRecord> getHistory();

    /**
     * Add a new record to the history (single selection)
     *
     * @param action        performed action
     * @param global        expression BEFORE doing the action
     * @param CASExpression expression for the CAS to be init in case of restart
     * @param selection     element the action was performed on
     */
    void addRecord(CASAdapter.Actions action, String global, String CASExpression, String selection);

    /**
     * Add a new record to the history (multiple selection)
     *
     * @param action        performed action
     * @param global        expression BEFORE doing the action
     * @param CASExpression expression for the CAS to be init in case of restart
     * @param selection     elements the action was performed on
     */
    void addRecord(CASAdapter.Actions action, String global, String CASExpression, String... selection);

    /**
     * Goes back to the last previous expression on the register. IT DOES NOT INIT THE CAS
     *
     * @return the previous expression
     */
    String returnToPreviousExpression();

    /**
     * Goes back to a previous expression. IT DOES NOT INIT THE CAS
     *
     * @param record previous record
     * @return the previous expression
     */
    String returnToExpression(ExpressionRecord record);

    /**
     * @return the number os entries the history has
     */
    int getRecordCount();
}
