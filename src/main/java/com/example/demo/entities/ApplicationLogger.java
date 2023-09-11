package com.example.demo.entities;

import org.springframework.stereotype.Component;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ApplicationLogger {
    private static final Logger logger = Logger.getLogger(ApplicationLogger.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("D:\\Users\\DucNM\\DH\\Code\\LTLT\\FsoftFresher\\JWTDemo\\demo\\src\\main\\resources\\static\\log.txt");
            logger.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
        }catch (Exception e) {
            System.err.println("Error in FileHandler.");
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
