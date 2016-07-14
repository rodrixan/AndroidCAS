package es.uam.eps.tfg.app.tfgapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import es.uam.eps.tfg.app.tfgapp.R;

/**
 * Auxiliary methods for using the preferences along the application
 */
public final class PreferenceUtils {

    private static final String DARK_BOARD_CHECKBOX_PREF_KEY = "color_board_checkbox_preference";
    private static final String EXP_HIGHLIGHT_COLOR_PREF_KEY = "selected_exp_color_list_preference";
    private static final String COLOR_AMBER_ID = "AMBER";
    private static final String COLOR_BLUE_ID = "BLUE";
    private static final String COLOR_RED_ID = "RED";


    private PreferenceUtils() {

    }

    /**
     * Returns the board color according to the user preference
     *
     * @param context activity that invoked the method
     * @return white or dark green color
     */
    public static int getBoardColor(final Context context) {
        if (getDarkBoardPreference(context)) {
            return context.getResources().getColor(R.color.dark_cardview_background);
        } else {
            return context.getResources().getColor(R.color.light_cardview_background);
        }
    }

    /**
     * @param context activity that invoked the method
     * @return value of the dark board preference
     */
    private static boolean getDarkBoardPreference(final Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(DARK_BOARD_CHECKBOX_PREF_KEY, false);
    }

    /**
     * Returns the expression color according to the board color
     *
     * @param context activity that invoked the method
     * @return white or black color
     */
    public static int getExpressionColor(final Context context) {
        if (getDarkBoardPreference(context)) {
            return context.getResources().getColor(R.color.light_expression_color);
        } else {
            return context.getResources().getColor(R.color.dark_expression_color);
        }
    }

    /**
     * Returns the expression highlight color according to the user preference
     *
     * @param context activity that invoked the method
     * @return amber, light blue or red color
     */
    public static int getExpressionHighlightColor(final Context context) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String exphighlightColorId = preferences.getString(EXP_HIGHLIGHT_COLOR_PREF_KEY, COLOR_AMBER_ID);
        switch (exphighlightColorId) {
            case COLOR_AMBER_ID:
                return context.getResources().getColor(R.color.medium_amber);
            case COLOR_BLUE_ID:
                return context.getResources().getColor(R.color.light_blue);
            case COLOR_RED_ID:
                return context.getResources().getColor(R.color.medium_red);
            default:
                return context.getResources().getColor(R.color.medium_amber);
        }
    }

}
