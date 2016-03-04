package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.services.CrimeLabImp;
import es.uam.eps.tfg.cas.android.examples.criminalintent.utils.Utils;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleText;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;

    private static final String ARG_CRIME_ID = "crime_id";

    public static CrimeFragment newInstance(final UUID crimeId) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        final CrimeFragment f = new CrimeFragment();
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);

        mCrime = CrimeLabImp.getCrimeLab(getActivity()).getCrime(crimeId);
        Log.d("TEST", "OnCreate finalized");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);
        Log.d("TEST", "OnCreateView called");
        wireWidgets(view);
        setData();
        setListeners();

        return view;
    }

    private void wireWidgets(final View v) {
        mTitleText = (EditText) v.findViewById(R.id.crime_title);
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
    }

    private void setData() {
        setTitle();
        setDate();
        setSolved();
    }

    private void setTitle() {
        mTitleText.setText(mCrime.getTitle());
    }

    private void setDate() {

        final String formattedDate = Utils.formatDateToString(mCrime.getDate());

        mDateButton.setText(formattedDate);
        mDateButton.setEnabled(false);
    }

    private void setSolved() {
        mSolvedCheckbox.setChecked(mCrime.isSolved());
    }


    private void setListeners() {
        mTitleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(final Editable s) {

            }
        });

        mSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
    }
    
    public void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }
}
