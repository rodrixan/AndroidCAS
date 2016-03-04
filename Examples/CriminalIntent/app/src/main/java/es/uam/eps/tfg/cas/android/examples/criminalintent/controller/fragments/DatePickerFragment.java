package es.uam.eps.tfg.cas.android.examples.criminalintent.controller.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import es.uam.eps.tfg.cas.android.examples.criminalintent.R;
import es.uam.eps.tfg.cas.android.examples.criminalintent.utils.Utils;


public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private static final String EXTRA_DATE = Utils.APP_PATH + ".date";

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(final Date date) {
        final Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        final DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static Date getSetDate(final Intent result) {
        return (Date) result.getSerializableExtra(EXTRA_DATE);
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        final Date date = (Date) getArguments().getSerializable(ARG_DATE);

        setDatePicker(v, date);

        return createDateAlertDialog(v);
    }

    private void setDatePicker(final View v, final Date date) {
        final int[] yearMonthDay = getYearMonthDay(date);

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2], null);
    }

    private int[] getYearMonthDay(final Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new int[]{year, month, day};
    }

    private int[] getYearMonthDay(final DatePicker datePicker) {

        final int year = datePicker.getYear();
        final int month = datePicker.getMonth();
        final int day = datePicker.getDayOfMonth();

        return new int[]{year, month, day};
    }

    private Dialog createDateAlertDialog(final View v) {
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        final Date date = getDateFromPicker();
                        sendResult(Activity.RESULT_OK, date);
                    }
                })
                .create();
    }


    private Date getDateFromPicker() {
        final int[] yearMonthDay = getYearMonthDay(mDatePicker);
        return new GregorianCalendar(yearMonthDay[0], yearMonthDay[1], yearMonthDay[2]).getTime();
    }

    private void sendResult(final int resultcode, final Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        final Intent i = new Intent();
        i.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultcode, i);
    }
}
