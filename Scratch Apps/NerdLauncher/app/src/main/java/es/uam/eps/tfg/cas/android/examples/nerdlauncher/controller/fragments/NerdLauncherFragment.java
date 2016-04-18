package es.uam.eps.tfg.cas.android.examples.nerdlauncher.controller.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.uam.eps.tfg.cas.android.examples.nerdlauncher.R;


public class NerdLauncherFragment extends Fragment {
    private static final String TAG = "NerdLauncherFragment";
    private RecyclerView mRecyclerView;

    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        final Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager pm = getActivity().getPackageManager();
        final List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            @Override
            public int compare(final ResolveInfo lhs, final ResolveInfo rhs) {
                final PackageManager pm = getActivity().getPackageManager();
                return String.CASE_INSENSITIVE_ORDER.compare(lhs.loadLabel(pm).toString(), rhs.loadLabel(pm).toString());
            }
        });
        Log.i(TAG, "Found " + activities.size() + " activities");
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ResolveInfo mResolveInfo;
        private final TextView mNameTextView;
        private final ImageView mIconImageView;

        public ActivityHolder(final View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.list_item_app_name);
            mIconImageView = (ImageView) itemView.findViewById(R.id.list_item_app_icon);
            itemView.setOnClickListener(this);
        }

        public void bindActivity(final ResolveInfo resolveInfo) {
            mResolveInfo = resolveInfo;
            final PackageManager pm = getActivity().getPackageManager();
            final String appName = mResolveInfo.loadLabel(pm).toString();
            mNameTextView.setText(appName);
            mIconImageView.setImageDrawable(mResolveInfo.loadIcon(pm));
        }

        @Override
        public void onClick(final View v) {
            final ActivityInfo actInfo = mResolveInfo.activityInfo;

            final Intent i = new Intent(Intent.ACTION_MAIN).setClassName(actInfo.applicationInfo.packageName, actInfo.name).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivityList;

        public ActivityAdapter(final List<ResolveInfo> activities) {
            mActivityList = activities;
        }

        @Override
        public ActivityHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            final View view = layoutInflater.inflate(R.layout.list_item_app, parent, false);
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(final ActivityHolder holder, final int position) {
            final ResolveInfo resolveInfo = mActivityList.get(position);
            holder.bindActivity(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mActivityList.size();
        }
    }
}
