package es.uam.eps.tfg.cas.android.examples.photogallery.controller.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.cas.android.examples.photogallery.R;
import es.uam.eps.tfg.cas.android.examples.photogallery.controller.handlers.FlickrFetcher;
import es.uam.eps.tfg.cas.android.examples.photogallery.model.GalleryItem;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "APP_PRUEBA";
    private static final String SAVED_CURRENT_PAGE = "current page";
    private static final String SAVED_DATA = "adapter data";

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();
    private int mCurrentPage = 1;

    private interface OnLoadMoreListener {
        void onLoadMore();
    }

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    private void getDataFromFlickr() {
        Log.i(TAG, "Getting data from flickr: page " + mCurrentPage);
        new FetchItemsTask().execute(mCurrentPage);
        mCurrentPage++;
    }

    private void restoreData(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mCurrentPage = savedInstanceState.getInt(SAVED_CURRENT_PAGE);
            mItems = mPhotoAdapter.JSONStringToData(savedInstanceState.getString(SAVED_DATA));
            Log.i(TAG, "data restored " + mItems.toString());
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CURRENT_PAGE, mCurrentPage);
        outState.putString(SAVED_DATA, mPhotoAdapter.dataToJSONString());
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getDataFromFlickr();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        restoreData(savedInstanceState);
        setupAdapter(mItems);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAdapter(mItems);
    }

    private void setupAdapter(final List<GalleryItem> items) {
        if (isAdded()) {
            if (mPhotoAdapter == null) {
                mPhotoAdapter = new PhotoAdapter(items);

            } else {
                mPhotoAdapter.setGalleryItems(items);
                mPhotoAdapter.notifyDataSetChanged();
            }
            mPhotoRecyclerView.setAdapter(mPhotoAdapter);
            mPhotoAdapter.setOnLoadMoreListener(createOnLoadMoreListener());
            mPhotoRecyclerView.addOnScrollListener(createOnScrollListener());
        }
    }

    private OnLoadMoreListener createOnLoadMoreListener() {
        return new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i(TAG, "on Load More called");
                mItems.add(null);
                mPhotoAdapter.notifyDataSetChanged();

                mPhotoAdapter.setLoading(false);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mItems.remove(mItems.size() - 1);
                        mPhotoAdapter.notifyDataSetChanged();

                        //load data
                        getDataFromFlickr();
                    }
                }, 2000);


                Log.i(TAG, "Loading = false");
            }
        };
    }

    private RecyclerView.OnScrollListener createOnScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState
                );
                final RecyclerView.LayoutManager layoutManager = mPhotoRecyclerView.getLayoutManager();
                if (layoutManager instanceof GridLayoutManager) {
                    final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    final int totalItemCount = gridLayoutManager.getItemCount();
                    final int lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition();

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!mPhotoAdapter.isLoading() && totalItemCount <= (lastVisibleItemPosition + 5)) {

                            mPhotoAdapter.onLoadMore();
                            Log.i(TAG, "Loading = true");
                            mPhotoAdapter.setLoading(true);

                        }
                    }
                }

            }
        };
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

    private class LoadingHolder extends RecyclerView.ViewHolder {
        private final ProgressBar mProgressBar;

        public LoadingHolder(final View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.fragment_photo_gallery_progress_bar);
        }

        public void setIndeterminate(final boolean isIndeterminate) {
            mProgressBar.setIndeterminate(isIndeterminate);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final static int VIEW_ITEM = 1;
        private final static int VIEW_PROG = 0;

        private List<GalleryItem> mGalleryItems;
        private OnLoadMoreListener mOnLoadMoreListener;
        private boolean mIsLoading = false;


        public PhotoAdapter(final List<GalleryItem> items) {
            mGalleryItems = items;
        }

        public void setLoading(final boolean isLoading) {
            mIsLoading = isLoading;
        }

        public boolean isLoading() {
            return mIsLoading;
        }

        @Override
        public int getItemViewType(final int position) {
            return mGalleryItems.get(position) != null ? VIEW_ITEM : VIEW_PROG;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            if (viewType == VIEW_ITEM) {
                final TextView textView = new TextView(getActivity());
                return new PhotoHolder(textView);
            } else {
                final View v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.fragment_photo_gallery_item_loading, parent, false);

                return new LoadingHolder(v);

            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final GalleryItem galleryItem = mGalleryItems.get(position);
            if (holder instanceof PhotoHolder) {
                ((PhotoHolder) holder).bindGalleryItem(galleryItem);
            } else {
                ((LoadingHolder) holder).setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }

        public void setOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
            mOnLoadMoreListener = onLoadMoreListener;
        }

        public void setGalleryItems(final List<GalleryItem> items) {
            mGalleryItems = items;
        }

        public void onLoadMore() {
            mOnLoadMoreListener.onLoadMore();
        }

        public String dataToJSONString() {
            final Gson gson = new Gson();
            final Type listType = new TypeToken<List<GalleryItem>>() {
            }.getType();
            final String JSONString = gson.toJson(mGalleryItems, listType);
            Log.i(TAG, "Saving adapter data: " + JSONString);
            return JSONString;
        }

        public List<GalleryItem> JSONStringToData(final String JSONString) {
            final Gson gson = new Gson();
            final Type listType = new TypeToken<List<GalleryItem>>() {
            }.getType();
            final List<GalleryItem> restoredItems = gson.fromJson(JSONString, listType);
            Log.i(TAG, "Restoring adapter data, found  " + restoredItems.size() + " items");
            return restoredItems;
        }
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(final Integer... params) {
            return new FlickrFetcher().fetchXanFlickrGalleryPhotos(params[0]);

        }

        @Override
        protected void onPostExecute(final List<GalleryItem> galleryItems) {
            mItems.addAll(galleryItems);
            Log.i(TAG, "added new elements. new size is " + mItems.size());
            setupAdapter(mItems);
        }
    }


}
