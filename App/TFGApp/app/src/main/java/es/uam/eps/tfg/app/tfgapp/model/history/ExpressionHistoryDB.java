package es.uam.eps.tfg.app.tfgapp.model.history;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;
import es.uam.eps.tfg.app.tfgapp.util.Utils;

/**
 * Implementation of the history.Singleton.
 */
public class ExpressionHistoryDB implements ExpressionHistory {

    private static ExpressionHistory mInstance = null;
    private final List<ExpressionRecord> mRecords;

    private ExpressionHistoryDB() {
        mRecords = new ArrayList<>();
    }

    public static ExpressionHistory getInstance() {
        if (mInstance == null) {
            mInstance = new ExpressionHistoryDB();
        }
        return mInstance;
    }

    @Override
    public List<ExpressionRecord> getHistory() {
        Log.d(Utils.LOG_TAG, "Recovering history");
        return mRecords;
    }

    @Override
    public void addExpression(final CASAdapter.Actions action, final Expression global, final Expression selection) {
        Log.d(Utils.LOG_TAG, "Adding history record");
        final ExpressionRecord record = new ExpressionRecord(action, global, selection);
        mRecords.add(0, record);//always added in first position

    }

    @Override
    public Expression returnToPreviousExpression() {

        final Expression newCurrent = mRecords.get(0).getGlobalExp();
        //delete the last and return the new last
        mRecords.remove(0);

        return newCurrent;
    }

    @Override
    public int getRecordCount() {
        return mRecords.size();
    }
}
