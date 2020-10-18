package com.fastis.data;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Board board;

    private String message;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime datetimeCreated;

    //this sets level access for notifications relative to users
    //can use the enum directly
    @Enumerated(EnumType.STRING)
    private MembershipType notificationType;

    public Notification(Board board, String message,
                        @NotNull LocalDateTime datetimeCreated,
                        MembershipType notificationType
    ) {
        this.board = board;
        this.message = message;
        this.datetimeCreated = datetimeCreated;
        this.notificationType = notificationType;
    }

    public Notification() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDateTimeCreated(LocalDateTime datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public MembershipType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(MembershipType notificationType) {
        this.notificationType = notificationType;
    }
}

