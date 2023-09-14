package com.example.demo.service.logrecord;

import com.example.demo.entities.LogRec;

public interface ILogService {
    void addLog(LogRec logRec);
    Long numberOfLogs();
    void clearLogs();
}
