package org.example.payment.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
    public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";

    public static String formatDate(Date d){
        return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(d);
    }

    public static Date parseDate(String scheduledDateStr) throws ParseException {
       return new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(scheduledDateStr);
    }
}
