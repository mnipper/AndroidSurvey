package org.adaptlab.chpir.android.survey;

import java.text.DateFormatSymbols;

public class FormatUtils {
    public static String pluralize(int number, String singular, String plural) {
        if (number == 1) {
            return singular;
        } else {
            return plural;
        }
    }
       
    public static String formatDate(int month, int day, int year) {
        String monthName = new DateFormatSymbols().getMonths()[month];
        return (monthName + " " + day + ", " + year);
    }
       
    // Format: HH:MM AM/PM
    public static String formatTime(int hour, int minute) {
        String period = (hour < 12 ? "AM" : "PM");
        hour = (hour > 13 ? hour - 12 : hour);
        if (hour == 0) hour = 12;
        return hour + ":" + formatMinute(minute) + " " + period;
    }

    // Add a 0 to minute values less than 10 to look more natural
    public static String formatMinute(int minute) {
        if (minute < 10) {
            return "0" + minute;
        } else {
            return String.valueOf(minute);
        }
    }
}
