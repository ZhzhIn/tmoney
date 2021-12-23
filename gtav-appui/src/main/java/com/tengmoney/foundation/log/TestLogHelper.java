package com.tengmoney.foundation.log;

import com.tengmoney.autoframework.DriverPool;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * 〈helps to collect session logs〉
 *
 * @author zhzh.yin
 * @create 2021/12/22
 */
public class TestLogHelper {
    private String prefix = "";
    private String PREFIX_FORMAT = "%s - ";
    private String sessionID;

    public TestLogHelper(String sessionID)
    {
        this.sessionID = sessionID;
    }

    public TestLogHelper(WebDriver driver)
    {
        this.sessionID = ((RemoteWebDriver) driver).getSessionId().toString();
    }

    public TestLogHelper(WebDriver driver, String prefix)
    {
        this.sessionID = ((RemoteWebDriver) driver).getSessionId().toString();
        this.prefix = String.format(PREFIX_FORMAT, prefix);
        TestLogCollector.clearSessionLogs(sessionID);
    }

    public void log(String msg)
    {
        TestLogCollector.logToSession(sessionID, prefix + msg);
    }

    public String getSessionLogs()
    {
        return TestLogCollector.getSessionLogs(sessionID);
    }

    public static String getSessionLogs(String test)
    {
        return TestLogCollector.getSessionLogs(DriverPool.getSessionIdByTestName(test));
    }

    public void setPrefix(String prefix)
    {
        this.prefix = String.format(PREFIX_FORMAT, prefix);
    }
}
