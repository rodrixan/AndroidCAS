package es.uam.eps.tfg.cas.android.examples.photogallery.controller.fragments;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.cas.android.examples.photogallery.R;
import es.uam.eps.tfg.cas.android.examples.photogallery.controller.handlers.FlickrFetcher;
import es.uam.eps.tfg.cas.android.examples.photogallery.controller.persistance.QueryPreferences;
import es.uam.eps.tfg.cas.android.examples.photogallery.model.GalleryItem;

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "APP_PRUEBA";
    private static final String SAVED_CURRENT_PAGE = "current page";
    private static final String SAVED_DATA = "adapter data";
    private static final String SAVED_NO_MORE_DATA = "no more data";

    private RecyclerView mPhotoRecyclerView;
    private LinearLayout mEmptyView;
    private PhotoAdapter mPhotoAdapter;
    private List<GalleryItem> mItems = new ArrayList<>();
    //private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;
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
        updateItems(true);

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
        setHasOptionsMenu(true);
        getDataFromFlickr();

        //configureHandler();

        Log.i(TAG, "Background thread started");
    }

    /*private void configureHandler() {
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
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mEmptyView = (LinearLayout) v.findViewById(R.id.loading_list_view);

        restoreData(savedInstanceState);
        updateUI();

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
        //mThumbnailDownloader.clearQueue();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_photo_gallery, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Log.d(TAG, "Query submitted: " + query);
                QueryPreferences.setStoredQuery(getActivity(), query);

                resetData();

                updateItems(false);
                searchView.clearFocus();//hide keyboard when submitted

                //MenuItemCompat.collapseActionView((menu.findItem(R.id.menu_item_search)));
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.d(TAG, "Query changed: " + newText);
                return false;
            }
        });
    }

    private void resetData() {
        mItems = new ArrayList<>();
        mCurrentPage = 1;
    }

    private void updateItems(final boolean usePage) {
        final String query = QueryPreferences.getStoredQuery(getActivity());
        updateUI();
        final FetchItemsTask task = new FetchItemsTask(query);
        task.execute(mCurrentPage);
        if (usePage) {
            mCurrentPage++;
        }
    }

    private void updateUI() {
        if (mEmptyView != null && mPhotoRecyclerView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            mPhotoRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                QueryPreferences.setStoredQuery(getActivity(), null);
                resetData();
                updateItems(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                launchItemLoaderHandler();
                Log.i(TAG, "Loading = false");
            }
        };
    }


    private void launchItemLoaderHandler() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                //load data
                getDataFromFlickr();
                mPhotoAdapter.setLoading(false);
            }
        });
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
                            Log.d(TAG, "Loading = true");
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

        public void bindGalleryItem(final GalleryItem item) {

            Picasso.with(getActivity())
                    .load(item.getUrl())
                    .placeholder(R.drawable.blank)
                    .into(mItemImageView);

        }
    }


    private class PhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

            final LayoutInflater inflater = LayoutInflater.from(getActivity());
            final View view = inflater.inflate(R.layout.gallery_item, parent, false);
            return new PhotoHolder(view);

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final GalleryItem galleryItem = mGalleryItems.get(position);
            if (holder instanceof PhotoHolder) {
                final Drawable placeHolder = ResourcesCompat.getDrawable(getResources(), R.drawable.blank, null);
                ((PhotoHolder) holder).bindGalleryItem(galleryItem);
                //mThumbnailDownloader.queueThumbnail((PhotoHolder) holder, galleryItem.getUrl());
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
        private final String mQuery;

        public FetchItemsTask(final String query) {
            mQuery = query;
        }

        @Override
        protected List<GalleryItem> doInBackground(final Integer... params) {

            if (mQuery == null) {
                return FlickrFetcher.fetchXanFlickrGalleryPhotos(params[0]);
            } else {
                return FlickrFetcher.searchPhotos(mQuery);
            }

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
            if (mEmptyView != null && mPhotoRecyclerView != null) {
                mEmptyView.setVisibility(View.GONE);
                mPhotoRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }


}
