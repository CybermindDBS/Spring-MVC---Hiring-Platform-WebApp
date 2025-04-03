package com.example.springmvchiringplatformapp.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    public static String newLine = "\n";
    static Logger logger;

    static {
        logger = LoggerFactory.getLogger(LoggerUtil.class);
    }

    public static Logger getLogger() {
        return logger;
    }
}
