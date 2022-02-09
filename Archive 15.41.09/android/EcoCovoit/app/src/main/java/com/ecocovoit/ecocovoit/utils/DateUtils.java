package com.ecocovoit.ecocovoit.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static String SERVER_DATE_FORMAT_PATTERN = "yyy.MM.dd - HH:mm:ss";
    private static Locale SERVER_DATE_LOCAL = Locale.FRANCE;

    public static String dateToServerString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(SERVER_DATE_FORMAT_PATTERN, SERVER_DATE_LOCAL);
        return dateFormat.format(date);
    }

    public static String dateToLocalString(Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.format(date);
    }

    public static Date localStringToDate(String dateStr) throws ParseException {
        DateFormat dateFormat = DateFormat.getDateInstance();
        return dateFormat.parse(dateStr);
    }

    public static Date serverStringToDate(String dateStr) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(SERVER_DATE_FORMAT_PATTERN, SERVER_DATE_LOCAL);
        return dateFormat.parse(dateStr);
    }

}
