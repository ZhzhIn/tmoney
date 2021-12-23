package com.tengmoney.autoframework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈webdriver 池〉
 *
 * @author zhzh.yin
 * @create 2021/12/22
 */
public class DriverPool {

    private static Map<String, WebDriver> testDriverMap = new HashMap<String, WebDriver>();

    private static Map<WebDriver, String> driverTestMap = new HashMap<WebDriver, String>();

    private static Map<String, WebDriver> sessionIdDriverMap = new HashMap<String, WebDriver>();

    public static synchronized String registerDriverSession(WebDriver driver)
    {
        String sessionId = ((RemoteWebDriver) driver).getSessionId().toString();
        sessionIdDriverMap.put(sessionId, driver);
        return sessionId;
    }

    public static synchronized void associateTestNameWithDriver(String testInClass, WebDriver driver)
    {
        testDriverMap.put(testInClass, driver);
        driverTestMap.put(driver, testInClass);
    }

    public static synchronized WebDriver getDriverByTestName(String test)
    {
        return testDriverMap.get(test);
    }

    public static synchronized WebDriver getDriverBySessionId(String sessionId)
    {
        return sessionIdDriverMap.get(sessionId);
    }

    public static String getSessionIdByTestName(String test)
    {
        if (testDriverMap.containsKey(test))
        {
            RemoteWebDriver driver = (RemoteWebDriver) testDriverMap.get(test);
            return driver.getSessionId().toString();
        } else
        {
            return null;
        }
    }

    public static String getTestNameByDriver(WebDriver driver)
    {
        return driverTestMap.containsKey(driver) ? driverTestMap.get(driver) : null;
    }
}
