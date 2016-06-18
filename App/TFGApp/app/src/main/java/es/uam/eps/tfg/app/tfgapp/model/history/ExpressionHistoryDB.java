package es.uam.eps.tfg.app.tfgapp.model.history;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
    public void addRecord(final CASAdapter.Actions action, final String global, final String CASExpression, final String... selection) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selection.length; i++) {
            sb.append(selection[i]);
            if (i < selection.length - 1) {
                sb.append(",");
            }
        }
        addRecord(action, global, CASExpression, sb.toString());
    }

    @Override
    public void addRecord(final CASAdapter.Actions action, final String global, final String CASExpression, final String selection) {
        Log.d(Utils.LOG_TAG, "Adding history record");

        final ExpressionRecord record = new ExpressionRecord(action, global, selection, CASExpression);
        mRecords.add(0, record);//always added in first position

    }

    @Override
    public String returnToPreviousExpression() {

        final String newCurrent = mRecords.get(0).getCASExpression();
        //delete the last and return the new last
        mRecords.remove(0);

        return newCurrent;
    }

    @Override
    public String returnToExpression(final ExpressionRecord record) {
        final int indexOfRecord = mRecords.indexOf(record);
        for (int i = 0; i < indexOfRecord; i++) {
            mRecords.remove(0);
        }
        if (mRecords.size() == 1) {
            mRecords.clear();
        } else {
            mRecords.remove(0);
        }
        return record.getCASExpression();
    }

    @Override
    public int getRecordCount() {
        return mRecords.size();
    }
}
