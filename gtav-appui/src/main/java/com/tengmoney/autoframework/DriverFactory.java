package com.tengmoney.autoframework;


import com.tengmoney.foundation.exception.InvalidArgsException;
import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.Configuration.Parameter;
import com.tmoney.foundation.utils.R;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static com.tmoney.foundation.utils.Configuration.Parameter.*;
import static org.openqa.selenium.remote.BrowserType.*;

/**
 * 根据平台的不同，创建不同的驱动
 */
public interface DriverFactory {
    static <T extends WebDriver> T createDriver() {
        return null;
    }


    static <T extends WebDriver> T createDriver(String testName) {
        return null;
    }

    static DesiredCapabilities initBaseCapabilities(DesiredCapabilities capabilities, Platform platform, String... args) {
        return null;
    }



}
