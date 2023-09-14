package com.example.demo.service.logrecord;

import com.example.demo.entities.LogRec;
import com.example.demo.repository.LogRecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogService implements ILogService{
    @Autowired
    private LogRecRepository logRepository;

    public LogService(LogRecRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void addLog(LogRec logRec) {
        logRepository.save(logRec);
    }

    @Override
    public Long numberOfLogs() {
        return logRepository.count();
    }

    @Override
    public void clearLogs() {
        logRepository.deleteAll();
    }
}
