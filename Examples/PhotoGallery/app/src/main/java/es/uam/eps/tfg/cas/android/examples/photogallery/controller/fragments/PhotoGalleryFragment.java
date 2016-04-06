package es.uam.eps.tfg.cas.android.examples.photogallery.controller.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.cas.android.examples.photogallery.R;
import es.uam.eps.tfg.cas.android.examples.photogallery.controller.handlers.FlickrFetcher;
import es.uam.eps.tfg.cas.android.examples.photogallery.listeners.OnLoadMoreListener;
import es.uam.eps.tfg.cas.android.examples.photogallery.model.GalleryItem;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        setupAdapter();
        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mPhotoAdapter = new PhotoAdapter(mItems);
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);

        }
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private final TextView mTitleTextView;

        public PhotoHolder(final View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView;
        }

        public void bindGalleryItem(final GalleryItem item) {
            mTitleTextView.setText(item.toString());
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private final List<GalleryItem> mGalleryItems;
        private OnLoadMoreListener mOnLoadMoreListener;

        public PhotoAdapter(final List<GalleryItem> items) {
            mGalleryItems = items;
        }

        @Override
        public PhotoHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            final TextView textView = new TextView(getActivity());
            return new PhotoHolder(textView);
        }

        @Override
        public void onBindViewHolder(final PhotoHolder holder, final int position) {
            final GalleryItem galleryItem = mGalleryItems.get(position);
            holder.bindGalleryItem(galleryItem);
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        public void setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
            mOnLoadMoreListener = onLoadMoreListener;
        }
    }

    private class FetchItemsTask extends AsyncTask<List<GalleryItem>, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(final List<GalleryItem>... params) {
            return new FlickrFetcher().fetchXanFlickrGalleryPhotos();

        }

        @Override
        protected void onPostExecute(final List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setupAdapter();
        }
    }


}
