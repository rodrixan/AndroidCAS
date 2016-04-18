package es.uam.eps.tfg.cas.android.examples.photogallery.controller.activities;

import android.support.v4.app.Fragment;

import es.uam.eps.tfg.cas.android.examples.photogallery.controller.fragments.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
