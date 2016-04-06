package es.uam.eps.tfg.cas.android.examples.photogallery.model;


import com.google.gson.annotations.SerializedName;

public class GalleryItem {
    @SerializedName("title")
    private String mCaption;
    @SerializedName("id")
    private String mId;
    @SerializedName("url_s")
    private String mUrl;

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(final String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(final String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
