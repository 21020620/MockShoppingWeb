package com.example.demo.system_service.repository;

import com.example.demo.system_service.entity.LogRec;

public interface LogService {
    void addLog(LogRec logRec);
    Long numberOfLogs();
    void clearLogs();
}
