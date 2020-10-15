package com.fastis.data;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Board board;

    @Size(max = 5000, message = "Description is to long..")
    private String message;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime datetime_from;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime datetime_to;

    private LocalDateTime datetime_created = LocalDateTime.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    private MembershipType event_type;

    private String location;

    @NotEmpty
    @Size(max = 150)
    private String name;

    public Event() {
    }

    public Event(Board board, String message,
                 LocalDateTime datetime_from, LocalDateTime datetime_to,
                 LocalDateTime datetime_created, MembershipType event_type,
                 String location, String name
    ) {
        this.board = board;
        this.message = message;
        this.datetime_from = datetime_from;
        this.datetime_to = datetime_to;
        this.datetime_created = datetime_created;
        this.event_type = event_type;
        this.location = location;
        this.name = name;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDatetime_from() {
        return datetime_from;
    }

    public void setDatetime_from(LocalDateTime datetime_from) {
        this.datetime_from = datetime_from;
    }

    public LocalDateTime getDatetime_to() {
        return datetime_to;
    }

    public void setDatetime_to(LocalDateTime datetime_to) {
        this.datetime_to = datetime_to;
    }

    public LocalDateTime getDatetime_created() {
        return datetime_created;
    }

    public void setDatetime_created(LocalDateTime datetime_created) {
        this.datetime_created = datetime_created;
    }

    public MembershipType getEvent_type() {
        return event_type;
    }

    public void setEvent_type(MembershipType event_type) {
        this.event_type = event_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
