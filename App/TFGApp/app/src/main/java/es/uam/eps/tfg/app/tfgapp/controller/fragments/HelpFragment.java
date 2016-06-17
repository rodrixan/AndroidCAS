package es.uam.eps.tfg.app.tfgapp.controller.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import es.uam.eps.tfg.app.tfgapp.R;

/**
 * Show information about how the application working
 */
public class HelpFragment extends Fragment {
    public static final int HELP_FRAGMENT_ID = 1;
    private static final int FRAGMENT_TITLE = R.string.help_fragment_title;
    private Callbacks mCallbacks;

    /**
     * @return new instance of this fragment
     */
    public static Fragment newInstance() {
        return new HelpFragment();
    }

    /**
     * @return tag for the stackback
     */
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
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*it has a menu, but only for showing the items. It does not work*/
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_help_toolbar, menu);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_help, container, false);

        mCallbacks.setTitle(FRAGMENT_TITLE);

        return v;
    }

}
