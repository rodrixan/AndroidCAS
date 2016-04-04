package es.uam.eps.tfg.cas.android.examples.beatbox.controller.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import es.uam.eps.tfg.cas.android.examples.beatbox.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    protected abstract Fragment createFragment();

    private FragmentManager mFragmentManager;

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        mFragmentManager = getSupportFragmentManager();

        addFragmentToManager(R.id.fragment_container);

    }


    private void addFragmentToManager(final int fragmentId) {
        //Fragment transitions are used to add, remove, (de)attach, or replace fragments in the frag. list
        Fragment fragment = mFragmentManager.findFragmentById(fragmentId);

        if (fragment == null) {
            fragment = createFragment();
            mFragmentManager.beginTransaction().add(fragmentId, fragment).commit();
        }
    }
}
