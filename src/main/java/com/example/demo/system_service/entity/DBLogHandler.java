package com.example.demo.system_service.entity;

import com.example.demo.system_service.entity.LogRec;
import com.example.demo.system_service.repository.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

@Component
public class DBLogHandler extends Handler {
    @Autowired
    private LogService logService;

    public DBLogHandler() {
    }

    @Override
    public void publish(LogRecord record) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(record.getMillis()));
        LogRec logRec = new LogRec(record.getMessage(), record.getLevel().toString(), timestamp);
        if(logService.numberOfLogs() >= 20) logService.clearLogs();
        logService.addLog(logRec);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
