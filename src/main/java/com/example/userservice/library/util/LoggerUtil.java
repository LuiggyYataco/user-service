package com.example.userservice.library.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

    public static void info(String msg) { logger.info(msg); }
    public static void error(String msg, Throwable t) { logger.error(msg, t); }
}
