package ua.matvienko_apps.controlyourbudget.classes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import org.joda.time.DateTime;


/**
 * Created by Alexandr on 19/01/2017.
 */

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    DatePickerDialog datePickerDialog;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DateTime dateTime = new DateTime(System.currentTimeMillis());
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();

        datePickerDialog = new DatePickerDialog(getActivity(), this, year, month - 1, day);

        return datePickerDialog;
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {


    }
}
