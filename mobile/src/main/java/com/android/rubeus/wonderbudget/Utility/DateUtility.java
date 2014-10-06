package com.android.rubeus.wonderbudget.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by rubeus on 10/6/14.
 */
public class DateUtility {

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
