package es.uam.eps.tfg.cas.android.examples.photogallery.controller.persistance;


import android.content.Context;
import android.preference.PreferenceManager;

public final class QueryPreferences {
    private static final String PREF_SEARCH_KEY = "searchQuery";

    private QueryPreferences() {
    }

    public static String getStoredQuery(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SEARCH_KEY, null);
    }

    public static void setStoredQuery(final Context context, final String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SEARCH_KEY, query)
                .apply();
    }

}
