package com.example.demo.service.logrecord;

import com.example.demo.entities.LogRec;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

@Component
public class DBLogHandler extends Handler {
    @Autowired
    private ILogService logService;

    public DBLogHandler() {
    }


    @PostConstruct
    public void init() {
        System.out.println("log service: " + logService);;
    }

    @Override
    public void publish(LogRecord record) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(record.getMillis()));
        LogRec logRec = new LogRec(record.getMessage(), record.getLevel().toString(), timestamp);
        logService.addLog(logRec);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}