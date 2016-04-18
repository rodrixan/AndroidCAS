package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;
import es.uam.eps.tfg.cas.android.examples.criminalintent.utils.Utils;


public class ImageViewerFragment extends DialogFragment {

    private static final String ARG_IMG = "iamge";
    private static final String EXTRA_IMG = Utils.APP_PATH + ".image";
    private ImageView mImageView;

    public static ImageViewerFragment newInstance(final File photoFile) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_IMG, photoFile);

        final ImageViewerFragment fragment = new ImageViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image, null);

        final File file = (File) getArguments().getSerializable(ARG_IMG);

        setImage(v, file);

        return createImageAlertDialog(v);
    }

    private void setImage(final View v, final File file) {
        final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), null);
        mImageView = (ImageView) v.findViewById(R.id.dialog_image_image_viewer);
        mImageView.setImageBitmap(bitmap);
    }

    private Dialog createImageAlertDialog(final View v) {
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }

    private void sendResult(final int resultCode) {
        if (getTargetFragment() == null) {
            return;
        }

        final Intent i = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
    }
}
