package com.fastis.datahandlers;

import com.fastis.data.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class LocalDateTimeHandler {


    private int plussyear = 0;
    private int year = 2020;


    public int getPlussyear() {
        return plussyear;
    }

    public void setPlussyear(int plussyear) {
        this.plussyear = plussyear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    public LocalDateTime getMonth(int month, int plussyear) {
        year += plussyear;
        switch (month) {
            case 1:
                String jan = year +"-01-01T00:00:00";
                return LocalDateTime.parse(jan);
            case 2:
                String feb = year +"-02-01T00:00:00";
                return LocalDateTime.parse(feb);
            case 3:
                String mar = year +"-03-01T00:00:00";
                return LocalDateTime.parse(mar);
            case 4:
                String apr =  year +"-04-01T00:00:00";
                return LocalDateTime.parse(apr);
            case 5:
                String may =  year+"-05-01T00:00:00";
                return LocalDateTime.parse(may);
            case 6:
                String jun = year +"-06-01T00:00:00";
                return LocalDateTime.parse(jun);
            case 7:
                String jul = year +"-07-01T00:00:00";
                return LocalDateTime.parse(jul);
            case 8:
                String aug = year +"-08-01T00:00:00";
                return LocalDateTime.parse(aug);
            case 9:
                String sep = year +"-09-01T00:00:00";
                return LocalDateTime.parse(sep);
            case 10:
                String oct =  year +"-10-01T00:00:00";
                return LocalDateTime.parse(oct);
            case 11:
                String nov =  year +"-11-01T00:00:00";
                return LocalDateTime.parse(nov);
            case 12:
                String des = year +"-12-01T00:00:00";
                return LocalDateTime.parse(des);
            default:
                String janu = year +"-01-01T00:00:00";
                return LocalDateTime.parse(janu);
        }
    }

    public LocalDateTime getLastDayOfMonth(int month, int plussyear) {
        year += plussyear;
        int daysInFeb = 28;
        if (year % 4 == 0){
            daysInFeb = 29;
        }
        switch (month) {
            case 1:
                String jan = year +"-01-31T23:59:59";
                return LocalDateTime.parse(jan);
            case 2:
                String feb = year +"-02-"+daysInFeb+"T23:59:59";
                return LocalDateTime.parse(feb);
            case 3:
                String mar = year +"-03-31T23:59:59";
                return LocalDateTime.parse(mar);
            case 4:
                String apr = year +"-04-30T23:59:59";
                return LocalDateTime.parse(apr);
            case 5:
                String may =  year+"-05-31T23:59:59";
                return LocalDateTime.parse(may);
            case 6:
                String jun = year +"-06-30T23:59:59";
                return LocalDateTime.parse(jun);
            case 7:
                String jul = year +"-07-31T23:59:59";
                return LocalDateTime.parse(jul);
            case 8:
                String aug = year +"-08-31T23:59:59";
                return LocalDateTime.parse(aug);
            case 9:
                String sep = year +"-09-30T23:59:59";
                return LocalDateTime.parse(sep);
            case 10:
                String oct =  year +"-10-31T23:59:59";
                return LocalDateTime.parse(oct);
            case 11:
                String nov =  year +"-11-30T23:59:59";
                return LocalDateTime.parse(nov);
            case 12:
                String des = year +"-12-31T23:59:59";
                return LocalDateTime.parse(des);
            default:
                String janu = year +"-01-01T23:59:59";
                return LocalDateTime.parse(janu);
        }
    }



    public String getCurrentMonth(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "Desember";
            default:
                return "ops";
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
        return dayOfMonth +"/"+getCurrentMonth(month);
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
