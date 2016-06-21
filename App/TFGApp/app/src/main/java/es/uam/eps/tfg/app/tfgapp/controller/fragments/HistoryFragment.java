package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASImplementation;
import es.uam.eps.tfg.app.tfgapp.model.history.ExpressionHistory;
import es.uam.eps.tfg.app.tfgapp.model.history.ExpressionHistoryDB;
import es.uam.eps.tfg.app.tfgapp.model.history.ExpressionRecord;
import es.uam.eps.tfg.app.tfgapp.util.Utils;

/**
 * History screen for expressions
 */
public class HistoryFragment extends BaseFragment {
    public static final int HISTORY_FRAGMENT_ID = 3;
    private static final int FRAGMENT_TITLE = R.string.history_fragment_title;
    private static final int FRAGMENT_SUBTITLE = R.string.history_fragment_subtitle;


    private RecyclerView mRecordRecyclerView;
    private TextView mEmptyView;
    private ExpressionAdapter mAdapter;

    /**
     * @return new instance of this fragment
     */
    public static Fragment newInstance() {
        return new HistoryFragment();
    }

    /**
     * @return tag for backstack
     */
    public static int getTagID() {
        return FRAGMENT_TITLE;
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_history, container, false);

        mCallbacks.setTitle(FRAGMENT_TITLE);
        mCallbacks.setSubtitle(FRAGMENT_SUBTITLE);

        wireComponents(v);

        updateAdapter();
        updateEmptyView();
        return v;

    }

    private void wireComponents(final View v) {
        mRecordRecyclerView = (RecyclerView) v.findViewById(R.id.history_recycler_view);
        mEmptyView = (TextView) v.findViewById(R.id.history_empty_list_text_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecordRecyclerView.setLayoutManager(layoutManager);

    }

    private void updateAdapter() {
        final ExpressionHistory history = ExpressionHistoryDB.getInstance();
        final List<ExpressionRecord> records = history.getHistory();
        if (mAdapter == null) {

            mAdapter = new ExpressionAdapter(records);
            mRecordRecyclerView.setAdapter(mAdapter);
        } else {

            mAdapter.setData(records);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateEmptyView() {
        if (ExpressionHistoryDB.getInstance().getRecordCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecordRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mRecordRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    //Holder for Record class
    private class ExpressionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mGlobalExpTextView;
        private TextView mActionTextView;
        private TextView mSelectedExpTextView;

        private ExpressionRecord mRecord;

        public ExpressionHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            wireComponents(itemView);
        }

        private void wireComponents(final View itemView) {
            mGlobalExpTextView = (TextView) itemView.findViewById(R.id.history_item_global_exp_text);
            mSelectedExpTextView = (TextView) itemView.findViewById(R.id.history_item_selected_exp_text);

            mActionTextView = (TextView) itemView.findViewById(R.id.history_item_action_text);

            final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Utils.FONT_PATH);
            mGlobalExpTextView.setTypeface(tf, Typeface.BOLD);
            mSelectedExpTextView.setTypeface(tf);

        }

        public void bindExpression(final ExpressionRecord record) {
            mRecord = record;
            mGlobalExpTextView.setText(record.getGlobalExp());
            mSelectedExpTextView.setText(record.getSelectedExp());
            mActionTextView.setText(record.getAction().getStringCode());
        }

        @Override
        public void onClick(final View v) {
            //just update the CAS and go back
            final String oldExp = ExpressionHistoryDB.getInstance().returnToExpression(mRecord);
            CASImplementation.getInstance().initCAS(oldExp);
            mCallbacks.navigateToFragment(ExpressionFragment.EXPRESSION_FRAGMENT_ID);
        }
    }//END_RecordHolder

    //Adapter for a Record list
    private class ExpressionAdapter extends RecyclerView.Adapter<ExpressionHolder> {

        private List<ExpressionRecord> mRecordList;

        public ExpressionAdapter(final List<ExpressionRecord> records) {
            mRecordList = records;
        }

        @Override
        public ExpressionHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

            final View view = createViewFromParent(parent);

            return new ExpressionHolder(view);
        }

        private View createViewFromParent(final ViewGroup parent) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return layoutInflater.inflate(R.layout.list_item_history, parent, false);
        }

        @Override
        public void onBindViewHolder(final ExpressionHolder holder, final int position) {
            final ExpressionRecord record = mRecordList.get(position);
            holder.bindExpression(record);
        }

        @Override
        public int getItemCount() {
            return mRecordList.size();
        }

        public void setData(final List<ExpressionRecord> record) {
            mRecordList = record;
        }
    }//END_RecordAdapter

}
