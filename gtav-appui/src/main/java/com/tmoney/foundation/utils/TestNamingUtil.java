package com.tmoney.foundation.utils;

import org.apache.commons.text.StringEscapeUtils;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

import java.lang.reflect.Method;

/**
 * 〈coomon naming utility for unique test method identification〉
 *
 * @author zhzh.yin
 * @create 2022/1/5
 */
public class TestNamingUtil
{
    private static INamingStrategy namingStrategy;

    static
    {
        try
        {
            namingStrategy = (INamingStrategy) Class.forName(R.CONFIG.get("naming_strategy")).newInstance();
        } catch (Exception e)
        {
            throw new RuntimeException("Can't create naming strategy: " + R.CONFIG.get("naming_strategy"));
        }
    }

    public static String getCanonicalTestNameBeforeTest(XmlTest xmlTest, Method testMethod)
    {
        return StringEscapeUtils.escapeHtml4(namingStrategy.getCanonicalTestNameBeforeTest(xmlTest, testMethod));
    }

    public static String getCanonicalTestName(ITestResult result)
    {
        return StringEscapeUtils.escapeHtml4(namingStrategy.getCanonicalTestName(result));
    }

    public static String getPackageName(ITestResult result)
    {
        return StringEscapeUtils.escapeHtml4(namingStrategy.getPackageName(result));
    }
}

