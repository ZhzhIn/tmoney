package com.tengmoney.foundation.log;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 按照sessionid记录webdriver日志
 *
 * @author zhzh.yin
 * @create 2021/12/22
 */
public class TestLogCollector {
    private static Map<String, StringBuilder> collector = new HashMap<String, StringBuilder>();
    private static Map<String, String> screenSteps = new HashMap<String, String>();

    /**
     * Clears messages in driver session context.
     * @param sessionId
     */
    public static void clearSessionLogs(String sessionId)
    {
        if (collector.containsKey(sessionId))
        {
            collector.remove(sessionId);
        }
    }

    /**
     * Adds message to test logs.
     *
     * @param sessionId
     *
     * @param msg
     *
     */
    public static synchronized void logToSession(String sessionId, String msg)
    {
        msg += "\n\r";
        if (collector.containsKey(sessionId))
        {
            collector.get(sessionId).append(msg);
        }
        else
        {
            collector.put(sessionId, new StringBuilder(msg));
        }
    }

    /**
     * Returns test log by webdriver session ID.
     *
     * @param sessionId
     *
     * @return test logs
     */
    public static synchronized String getSessionLogs(String sessionId)
    {
        return collector.containsKey(sessionId) ? collector.get(sessionId).toString() : "";
    }

    /**
     * Stores comment for screenshot.
     *
     * @param screenId
     *
     * @param msg
     *
     */
    public static synchronized void addScreenshotComment(String screenId, String msg)
    {
        if (!StringUtils.isEmpty(screenId))
        {
            screenSteps.put(screenId, msg);
        }
    }

    /**
     * Return comment for screenshot.
     *
     * @param screenId
     *
     * @return screenshot comment
     */
    public static synchronized String getScreenshotComment(String screenId)
    {
        return screenSteps.get(screenId);
    }
}
