package es.uam.eps.tfg.cas.android.examples.draganddraw.controller.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.uam.eps.tfg.cas.android.examples.draganddraw.R;


public class DragAndDrawFragment extends Fragment {

    public static Fragment newInstance() {
        return new DragAndDrawFragment();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_drag_and_draw, container, false);
        return v;
    }
}
