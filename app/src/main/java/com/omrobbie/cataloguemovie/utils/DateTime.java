/*
 * Created by omrobbie.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 9/27/17 3:00 PM.
 */

package com.omrobbie.cataloguemovie.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by omrobbie on 27/09/2017.
 */

public class DateTime {

    private static String formatDate(String date, String format) {
        String result = "";

        DateFormat old = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date oldDate = old.parse(date);
            DateFormat newFormat = new SimpleDateFormat(format);
            result = newFormat.format(oldDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getShortDate(String date) {
        return formatDate(date, "dd MMMM yyyy");
    }

    public static String getLongDate(String date) {
        return formatDate(date, "EEEE, MMM d, yyyy");
    }
}
