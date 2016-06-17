package es.uam.eps.tfg.app.tfgapp.util;


/**
 * Generic utils for the app
 */
public final class Utils {
    public static final String LOG_TAG = "TFG-APP";
    public static final String FONT_PATH = "fonts/lmromanslant10-regular-ExpView.otf";

    private Utils() {
    }

    public static String createLongSampleExpression() {

        return "+[#[3],#[5]]";
    }

    public static String createShortSampleExpression() {
        return "=[*[#[x],+[#[3],#[2]]],-R[$[x],#[5]]]";
    }

    public static String createMediumSampleExpression() {

        return "+[#[3],#[5]]";
    }

    public static String createUltraLongSampleExpression() {

        return "+[#[3],#[5]]";
    }

}
