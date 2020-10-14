package com.fastis.datahandlers;

import java.time.LocalDateTime;

public class LocalDateTimeHandler {

    public String getDateString(LocalDateTime localDateTime){
        String date = getDayOfWeek(localDateTime) + " " + getDayOfMonth(localDateTime) +"/"+getMonth(localDateTime);
        return date;
    }

    public String getMonth(LocalDateTime localDateTime) {
        int month = localDateTime.getMonth().getValue();
        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Des";
            default:
                return "Not found";
        }
    }

    public String getDayOfWeek(LocalDateTime localDateTime){
        int dayOfWeek = localDateTime.getDayOfWeek().getValue();
        switch (dayOfWeek){
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thu";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            case 7:
                return "Sun";
            default:
                return "Day not Found";
        }
    }

    public String getDayOfMonth(LocalDateTime localDateTime){
        int month = localDateTime.getMonth().getValue();
        int dayOfMonth = localDateTime.getDayOfMonth();
        return dayOfMonth +"/"+month;
    }

    public String getHourAndMin(LocalDateTime localDateTime){
        int hour = localDateTime.getHour();
        int min = localDateTime.getMinute();
        if(min < 10){
            return hour + ":0"+min;
        }
        return hour + ":"+min;
    }
}
