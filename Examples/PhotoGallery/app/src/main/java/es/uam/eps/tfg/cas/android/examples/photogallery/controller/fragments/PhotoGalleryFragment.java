package es.uam.eps.tfg.cas.android.examples.photogallery.controller.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.cas.android.examples.photogallery.R;
import es.uam.eps.tfg.cas.android.examples.photogallery.controller.handlers.FlickrFetcher;
import es.uam.eps.tfg.cas.android.examples.photogallery.controller.handlers.ThumbnailDownloader;
import es.uam.eps.tfg.cas.android.examples.photogallery.model.GalleryItem;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "APP_PRUEBA";
    private static final String SAVED_CURRENT_PAGE = "current page";
    private static final String SAVED_DATA = "adapter data";
    private static final String SAVED_NO_MORE_DATA = "no more data";

    private RecyclerView mPhotoRecyclerView;
    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
    private int mCurrentPage = 1;
    private boolean mNoMoreData = false;

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
            mNoMoreData = savedInstanceState.getBoolean(SAVED_NO_MORE_DATA);
            Log.i(TAG, "data restored " + mItems.toString());
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CURRENT_PAGE, mCurrentPage);
        outState.putBoolean(SAVED_NO_MORE_DATA, mNoMoreData);
        outState.putString(SAVED_DATA, mPhotoAdapter.dataToJSONString());
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getDataFromFlickr();

        configureHandler();

        Log.i(TAG, "Background thread started");
    }

    private void configureHandler() {
        final Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
            @Override
            public void onThumbnailDownloaded(final PhotoHolder target, final Bitmap thumbnail) {
                final Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                target.bindGalleryItem(drawable);
            }
        });
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        restoreData(savedInstanceState);

        setupAdapter(mItems);
        mPhotoRecyclerView.setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnLoadMoreListener(createOnLoadMoreListener());
        mPhotoRecyclerView.addOnScrollListener(createOnScrollListener());

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAdapter(mItems);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailDownloader.clearQueue();
    }

    private void setupAdapter(final List<GalleryItem> items) {
        if (isAdded()) {
            if (mPhotoAdapter == null) {
                mPhotoAdapter = new PhotoAdapter(items);
            } else {
                mPhotoAdapter.setGalleryItems(items);
                mPhotoAdapter.notifyDataSetChanged();
            }
        }
    }

    private OnLoadMoreListener createOnLoadMoreListener() {
        return new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i(TAG, "on Load More called");
                if (mNoMoreData) {
                    final Toast toast = Toast.makeText(getActivity(), R.string.toast_no_more_data, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                showProgressBar();
                launchItemLoaderHandler();
                Log.i(TAG, "Loading = false");
            }
        };
    }

    private void showProgressBar() {
        mItems.add(null);
        mPhotoAdapter.notifyDataSetChanged();
    }

    private void launchItemLoaderHandler() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                hideProgressBar();

                //load data
                getDataFromFlickr();
                mPhotoAdapter.setLoading(false);
            }
        });
    }

    private void hideProgressBar() {
        mItems.remove(mItems.size() - 1);
        mPhotoAdapter.notifyDataSetChanged();
    }

    private RecyclerView.OnScrollListener createOnScrollListener() {
        return new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);

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
        private final ImageView mItemImageView;

        public PhotoHolder(final View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_photo_gallery_image_view);
        }

        public void bindGalleryItem(final Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
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
                final LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View view = inflater.inflate(R.layout.gallery_item, parent, false);
                return new PhotoHolder(view);
            } else {
                final View v = LayoutInflater.from(getActivity()).inflate(
                        R.layout.gallery_item_loading, parent, false);

                return new LoadingHolder(v);

            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final GalleryItem galleryItem = mGalleryItems.get(position);
            if (holder instanceof PhotoHolder) {
                final Drawable placeHolder = ResourcesCompat.getDrawable(getResources(), R.drawable.blank, null);
                ((PhotoHolder) holder).bindGalleryItem(placeHolder);
                mThumbnailDownloader.queueThumbnail((PhotoHolder) holder, galleryItem.getUrl());
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
            return FlickrFetcher.fetchXanFlickrGalleryPhotos(params[0]);

        }

        @Override
        protected void onPostExecute(final List<GalleryItem> galleryItems) {
            if (galleryItems.isEmpty()) {
                mNoMoreData = true;
                return;
            }
            mItems.addAll(galleryItems);
            Log.i(TAG, "added new elements. new size is " + mItems.size());
            setupAdapter(mItems);
        }
    }


}
