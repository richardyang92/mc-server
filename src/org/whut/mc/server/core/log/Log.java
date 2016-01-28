package org.whut.mc.server.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yangyang on 2015/12/2.
 */
public class Log {
    private Logger logger;

    private Log(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    private Log(String loggerName) {
        logger = LoggerFactory.getLogger(loggerName);
    }

    public static Log getLogger(Class clazz) {
        return new Log(clazz);
    }

    public static Log getLogger(String loggerName) {
        return new Log(loggerName);
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void debug(String format, Object... argArray) {
        logger.debug(format, argArray);
    }

    public void debug(String msg, Throwable t) {
        logger.debug(msg, t);
    }

    public void debug(Throwable t) {
        logger.debug(t.getMessage(), t);
    }

    public void warn(String msg) {
        logger.warn(msg);
    }

    public void warn(String format, Object... argArray) {
        logger.warn(format, argArray);
    }

    public void warn(String msg, Throwable t) {
        logger.warn(msg, t);
    }

    public void warn(Throwable t) {
        logger.warn(t.getMessage(), t);
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(String format, Object... argArray) {
        logger.error(format, argArray);
    }

    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }

    public void error(Throwable t) {
        logger.error(t.getMessage(), t);
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(String format, Object... argArray) {
        logger.info(format, argArray);
    }

    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    public void info(Throwable t) {
        logger.info(t.getMessage(), t);
    }

    public void trace(String msg) {
        logger.trace(msg);
    }

    public void trace(String format, Object... argArray) {
        logger.trace(format, argArray);
    }

    public void trace(String msg, Throwable t) {
        logger.trace(msg, t);
    }

    public void trace(Throwable t) {
        logger.trace(t.getMessage(), t);
    }
}
