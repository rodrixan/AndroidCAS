package es.uam.eps.tfg.cas.android.examples.criminalintent.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;
import es.uam.eps.tfg.cas.android.examples.criminalintent.model.Crime;

public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleText;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_crime, container, false);

        wireWidgets(view);
        setListeners();

        return view;
    }


    private void wireWidgets(final View v) {
        mTitleText = (EditText) v.findViewById(R.id.crime_title);
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
    }
}
