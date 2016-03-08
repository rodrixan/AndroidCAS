package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;
import es.uam.eps.tfg.cas.android.examples.criminalintent.controller.activities.CrimePagerActivity;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.services.CrimeLab;
import es.uam.eps.tfg.cas.android.examples.criminalintent.utils.Utils;


public class CrimeListFragment extends Fragment {
    private static final int REQUEST_CRIME = 1;
    private static final String SAVED_SUBTITLE_SHOWN = "subtitle";


    private RecyclerView mCrimeRecyclerView;
    private TextView mEmptyView;
    private CrimeAdapter mAdapter;
    private boolean mShowSubtitle = false;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        Log.d(Utils.APP_LOG_TAG, "lista fragmentos creada");
        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
        mEmptyView = (TextView) v.findViewById(R.id.empty_list_text_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mCrimeRecyclerView.setLayoutManager(layoutManager);


        restoreData(savedInstanceState);
        updateUI();
        return v;
    }

    private void restoreData(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mShowSubtitle = savedInstanceState.getBoolean(SAVED_SUBTITLE_SHOWN);
        }
    }

    private void updateUI() {
        Log.d(Utils.APP_LOG_TAG, "actualizando UI");
        updateAdapter();
        updateSubtitle();
        updateEmptyView();
    }

    private void updateAdapter() {
        Log.d(Utils.APP_LOG_TAG, "actualizando adapter");
        final CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        final List<Crime> crimes = crimeLab.getCrimes();
        if (mAdapter == null) {
            Log.d(Utils.APP_LOG_TAG, "actualizando adapter desde 0");
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            Log.d(Utils.APP_LOG_TAG, "actualizando adpter: setCrimes");
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateSubtitle() {

        final String subtitle = createSubtitle();
        setSubtitleText(subtitle);
    }

    private String createSubtitle() {
        final long nCrimes = CrimeLab.getCrimeLab(getActivity()).getNumberOfCrimes();
        final int nCrimesInt = Integer.valueOf(nCrimes + "");
        return (!mShowSubtitle) ? null : getResources().getQuantityString(R.plurals.subtitle_plurals, nCrimesInt, nCrimesInt);
    }

    private void setSubtitleText(final String subtitle) {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateEmptyView() {
        if (CrimeLab.getCrimeLab(getActivity()).getNumberOfCrimes() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mCrimeRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.GONE);
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_SHOWN, mShowSubtitle);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        setSubtitleTitle(menu);
    }

    private void setSubtitleTitle(final Menu menu) {
        final MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mShowSubtitle) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_new_crime: {
                Log.d(Utils.APP_LOG_TAG, "Nuevo crimen seleccionado");
                final UUID crimeId = createNewCrime();
                Log.d(Utils.APP_LOG_TAG, "Crimen creado");
                startCrimeActivity(crimeId);
                break;
            }
            case R.id.menu_item_show_subtitle: {
                mShowSubtitle = !mShowSubtitle;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private UUID createNewCrime() {

        final Crime crime = new Crime();
        CrimeLab.getCrimeLab(getActivity()).addCrime(crime);
        Log.d(Utils.APP_LOG_TAG, "creando nuevo crimen con ID" + crime.getId().toString());
        return crime.getId();
    }

    private void startCrimeActivity(final UUID crimeId) {
        Log.d(Utils.APP_LOG_TAG, "lanzando actividad CrimeActivity");
        final Intent i = new Intent(CrimePagerActivity.newIntent(getActivity(), crimeId));
        startActivity(i);
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CRIME) {
            //HANDLE
        }
    }

    //ViewHolder for Crime class
    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            wireComponents(itemView);
        }

        private void wireComponents(final View itemView) {
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_tv);
            mDateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_tv);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_cb);
        }


        public void bindCrime(final Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(crime.getTitle());

            final String formattedDate = Utils.formatDateToString(mCrime.getDate());
            mDateTextView.setText(formattedDate);

            mSolvedCheckBox.setChecked(mCrime.isSolved());
            mSolvedCheckBox.setEnabled(false);

        }

        @Override
        public void onClick(final View v) {
            final Intent i = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(i, REQUEST_CRIME);
        }


        @Override
        public boolean onLongClick(final View v) {
            CrimeLab.getCrimeLab(getActivity()).removeCrime(mCrime.getId());
            updateUI();
            return true;
        }
    }//END_CrimeHolder


    //Adapter for a Crime list
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimeList;

        public CrimeAdapter(final List<Crime> crimes) {
            mCrimeList = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

            final View view = createViewFromParent(parent);

            return new CrimeHolder(view);
        }

        private View createViewFromParent(final ViewGroup parent) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return layoutInflater.inflate(R.layout.list_item_crime, parent, false);
        }

        @Override
        public void onBindViewHolder(final CrimeHolder holder, final int position) {
            final Crime c = mCrimeList.get(position);
            holder.bindCrime(c);
        }

        @Override
        public int getItemCount() {
            return mCrimeList.size();
        }

        public void setCrimes(final List<Crime> crimes) {
            mCrimeList = crimes;
        }
    }//END_CrimeAdapter
}
