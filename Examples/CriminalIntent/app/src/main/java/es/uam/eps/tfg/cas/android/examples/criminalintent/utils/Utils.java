package es.uam.eps.tfg.cas.android.examples.criminalintent.utils;

import android.text.format.DateFormat;

import java.util.Date;


public class Utils {

    public static String formatDateToString(final Date date) {
        final DateFormat df = new DateFormat();
        return df.format("EEEE, MMM dd, yyyy", date).toString();
    }

    public static final String APP_PATH = "es.uam.eps.tfg.cas.android.examples.criminalintent";
}
