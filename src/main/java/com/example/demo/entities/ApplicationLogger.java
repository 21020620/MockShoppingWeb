package com.example.demo.entities;

import com.example.demo.service.logrecord.DBLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Component
public class ApplicationLogger {
    private static final Logger logger = Logger.getLogger(ApplicationLogger.class.getName());
    private final DBLogHandler dbLogHandler;

    @Autowired
    public ApplicationLogger(DBLogHandler dbLogHandler) {
        this.dbLogHandler = dbLogHandler;
        try {
            FileHandler fileHandler = new FileHandler("D:\\Users\\DucNM\\DH\\Code\\LTLT\\FsoftFresher\\JWTDemo\\demo\\src\\main\\resources\\static\\log.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
            logger.addHandler(dbLogHandler);
        } catch (Exception e) {
            System.err.println("Error in FileHandler.");
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
