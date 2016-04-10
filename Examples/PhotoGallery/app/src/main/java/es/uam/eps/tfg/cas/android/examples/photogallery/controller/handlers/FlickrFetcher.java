package es.uam.eps.tfg.cas.android.examples.photogallery.controller.handlers;


import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.uam.eps.tfg.cas.android.examples.photogallery.model.GalleryItem;

public class FlickrFetcher {
    private static final int BUFFER_MAX_SIZE = 1024;
    private static final String TAG = "APP_PRUEBA";

    private static final String FLICKR_REST_URL = "https://www.flickr.com/services/rest/";
    private static final String API_KEY = "3c9cd9a3e38a1af28808fa76e40ed76a";
    private static final String USER_ID = "50304076@N04";


    public static byte[] getURLBytes(final String urlSpec) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) new URL(urlSpec).openConnection();

        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            final InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;

            final byte[] buffer = new byte[BUFFER_MAX_SIZE];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }

            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public static String getUrlString(final String urlSpec) throws IOException {
        return new String(getURLBytes(urlSpec));
    }


    public static List<GalleryItem> fetchXanFlickrGalleryPhotos(final int page) {
        final String url = createParsedUrl(page);
        try {
            final String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            final JSONObject jsonBody = new JSONObject(jsonString);
            return parseItems(jsonBody);
        } catch (final IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (final JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }
        return null;
    }

    private static String createParsedUrl(final int page) {
        Log.i(TAG, "Fetching page " + page);
        return Uri.parse(FLICKR_REST_URL)
                .buildUpon()
                .appendQueryParameter("method", "flickr.people.getPublicPhotos")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("user_id", USER_ID)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("page", page + "")
                .appendQueryParameter("per_page", "5")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .build().toString();
    }

    private static List<GalleryItem> parseItems(final JSONObject jsonBody) throws JSONException {
        final List<GalleryItem> items = new ArrayList<>();

        final JSONObject photosJSONObject = jsonBody.getJSONObject("photos");
        final JSONArray photoJSONArray = photosJSONObject.getJSONArray("photo");

        final Gson gson = new Gson();

        final Type listType = new TypeToken<List<GalleryItem>>() {
        }.getType();
        return (List<GalleryItem>) gson.fromJson(photoJSONArray.toString(), listType);

    }

}
