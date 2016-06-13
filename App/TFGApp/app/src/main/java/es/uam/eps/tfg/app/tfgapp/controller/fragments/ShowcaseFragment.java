package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.app.Activity;
import android.content.Context;
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

import es.uam.eps.expressions.types.interfaces.Expression;
import es.uam.eps.tfg.app.tfgapp.R;
import es.uam.eps.tfg.app.tfgapp.Utils.Utils;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASAdapter;
import es.uam.eps.tfg.app.tfgapp.model.cas.CASImplementation;

public class ShowcaseFragment extends Fragment {
    public static final int SHOWCASE_FRAGMENT_ID = 2;
    private static final int FRAGMENT_TITLE = R.string.showcase_fragment_title;
    private static final int FRAGMENT_SUBTITLE = R.string.showcase_fragment_subtitle;

    private Callbacks mCallbacks;
    private RecyclerView mExpRecyclerView;
    private ExpressionAdapter mAdapter;

    /**
     * @return new instance of this fragment
     */
    public static Fragment newInstance() {
        return new ShowcaseFragment();
    }

    public static int getTagID() {
        return FRAGMENT_TITLE;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mCallbacks = (Callbacks) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_showcase, container, false);

        mCallbacks.setTitle(FRAGMENT_TITLE);
        mCallbacks.setSubtitle(FRAGMENT_SUBTITLE);

        mExpRecyclerView = (RecyclerView) v.findViewById(R.id.showcase_recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mExpRecyclerView.setLayoutManager(layoutManager);

        updateAdapter();
        return v;

    }

    private void updateAdapter() {
        final CASAdapter CAS = CASImplementation.getInstance();
        final List<Expression> expList = CAS.getSampleExpressions();
        if (mAdapter == null) {

            mAdapter = new ExpressionAdapter(expList);
            mExpRecyclerView.setAdapter(mAdapter);
        } else {

            mAdapter.setData(expList);
            mAdapter.notifyDataSetChanged();
        }
    }

    //ExpressionHolder for Expression class
    private class ExpressionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mExpressionTextView;
        private Expression mExpression;

        public ExpressionHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            wireComponents(itemView);
        }

        private void wireComponents(final View itemView) {
            mExpressionTextView = (TextView) itemView.findViewById(R.id.showcase_item_exp_text);
            final Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), Utils.FONT_PATH);
            mExpressionTextView.setTypeface(tf);
        }

        public void bindExpression(final Expression expression) {
            mExpression = expression;
            mExpressionTextView.setText(expression.symbolicExpression());
        }

        @Override
        public void onClick(final View v) {
            //just update the CAS and go back
            CASImplementation.getInstance().initCAS(mExpression);
            mCallbacks.navigateToFragment(ExpressionFragment.EXPRESSION_FRAGMENT_ID);
        }
    }//END_ExpressionHolder

    //Adapter for an Expression list
    private class ExpressionAdapter extends RecyclerView.Adapter<ExpressionHolder> {

        private List<Expression> mExpressionList;

        public ExpressionAdapter(final List<Expression> expressionList) {
            mExpressionList = expressionList;
        }

        @Override
        public ExpressionHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

            final View view = createViewFromParent(parent);

            return new ExpressionHolder(view);
        }

        private View createViewFromParent(final ViewGroup parent) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return layoutInflater.inflate(R.layout.list_item_showcase, parent, false);
        }

        @Override
        public void onBindViewHolder(final ExpressionHolder holder, final int position) {
            final Expression exp = mExpressionList.get(position);
            holder.bindExpression(exp);
        }

        @Override
        public int getItemCount() {
            return mExpressionList.size();
        }

        public void setData(final List<Expression> expList) {
            mExpressionList = expList;
        }
    }//END_ExpressionAdapter

}
