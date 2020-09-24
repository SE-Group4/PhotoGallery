package com.example.photogallery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchHelper {

    public static boolean searchByKeyword(String[] keywords, String keyWord) {
        for(int i = 0; i < keywords.length; i++) {
            if(keywords[i].compareTo(keyWord) == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchByDate(String inputDate, String startDate, String endDate){
        Date date;
        Date fromDate;
        Date toDate;
        String dateText;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat lazyFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(inputDate != null && !inputDate.isEmpty() && !inputDate.equals("null")){
            dateText = inputDate.split(" ")[0];
        } else {
            Date today = Calendar.getInstance().getTime();
            dateText = sdf.format(today);
        }
        try {
            date = sdf.parse(dateText);

            if(startDate != null && !startDate.isEmpty() && !startDate.equals("null")) {
                fromDate = lazyFormat.parse(startDate);
            } else {
                fromDate = lazyFormat.parse("0900-01-01");
            }

            if(endDate != null && !endDate.isEmpty() && !endDate.equals("null")) {
                toDate = lazyFormat.parse(endDate);
            } else {
                toDate = lazyFormat.parse("9999-01-01");
            }

            if (date.after(fromDate) && date.before(toDate)) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
