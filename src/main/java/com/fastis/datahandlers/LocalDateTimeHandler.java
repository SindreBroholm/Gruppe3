package com.fastis.datahandlers;

import com.fastis.data.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class LocalDateTimeHandler {

    public String getDateString(LocalDateTime localDateTime){
        String date = getDayOfWeek(localDateTime) + " " + getDayOfMonth(localDateTime) +"/"+getMonth(localDateTime.getMonthValue());
        return date;
    }

    public String getMonth(int month) {
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
    public int getNumbOfDaysInMonth(int month) {
        switch (month) {
            case 1:
                return 31;
            case 2:
                return 28;
            case 3:
                return 31;
            case 4:
                return 30;
            case 5:
                return 31;
            case 6:
                return 30;
            case 7:
                return 31;
            case 8:
                return 31;
            case 9:
                return 30;
            case 10:
                return 31;
            case 11:
                return 30;
            case 12:
                return 31;
            default:
                return 30;
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
        return dayOfMonth +"/"+getMonth(month);
    }

    public String getHourAndMin(LocalDateTime localDateTime){
        int hour = localDateTime.getHour();
        int min = localDateTime.getMinute();
        if(min < 10){
            return hour + ":0"+min;
        }
        return hour + ":"+min;
    }


    private boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    //Kan være null.
    public List<Event> cheackForDobbelBooking(List<Event> userEvents, Event newEvent){
        List<Event> overlappingEvents = null;
        for (Event e : userEvents) {
            if (isOverlapping(e.getDatetime_from(),e.getDatetime_to(),newEvent.getDatetime_from(),newEvent.getDatetime_to())){
                    overlappingEvents.add(e);
            }
        }
        return overlappingEvents;
    }
}
