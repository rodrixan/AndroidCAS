package es.uam.eps.tfg.cas.android.examples.nerdlauncher.controller.activities;

import android.support.v4.app.Fragment;

import es.uam.eps.tfg.cas.android.examples.nerdlauncher.controller.fragments.NerdLauncherFragment;

public class NerdLauncherActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
