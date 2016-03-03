package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleText;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);

        wireWidgets(view);
        setDateButtonProperties();
        setListeners();

        return view;
    }

    private void wireWidgets(final View v) {
        mTitleText = (EditText) v.findViewById(R.id.crime_title);
        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
    }

    private void setDateButtonProperties() {

        final String formattedDate = formatDateToString(mCrime.getDate());

        mDateButton.setText(formattedDate);
        mDateButton.setEnabled(false);
    }

    private String formatDateToString(final Date date) {
        final DateFormat df = new DateFormat();
        return df.format("EEEE, MMM dd, yyyy", date).toString();
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
}
