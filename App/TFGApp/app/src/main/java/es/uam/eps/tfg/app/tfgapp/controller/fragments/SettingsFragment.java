package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import es.uam.eps.tfg.app.tfgapp.R;


public class SettingsFragment extends PreferenceFragmentCompat {
    public static final int SETTINGS_FRAGMENT_ID = 4;
    private static final int FRAGMENT_TITLE = R.string.settings_fragment_title;
    private Callbacks mCallbacks;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCallbacks.setTitle(FRAGMENT_TITLE);

    }


    @Override
    public void onCreatePreferencesFix(final Bundle savedInstanceState, final String rootKey) {
        addPreferencesFromResource(R.xml.settings);
    }


    /**
     * @return new instance of this fragment
     */
    public static Fragment newInstance() {
        return new SettingsFragment();
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


}
