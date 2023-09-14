package com.example.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "log_record")
public class LogRec {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "level")
    private String level;

    @Column(name = "time")
    private String time;

    public LogRec() {
    }

    public LogRec(String message, String level, String time) {
        this.message = message;
        this.level = level;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
