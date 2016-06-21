package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Fragment with basic functionality
 */
public class BaseFragment extends Fragment {
    protected Callbacks mCallbacks;

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

    protected static Fragment newInstance() {
        return new Fragment();
    }
}
