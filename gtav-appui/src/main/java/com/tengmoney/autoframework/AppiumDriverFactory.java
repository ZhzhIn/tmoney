package com.tengmoney.autoframework;

import com.appframework.foundation.exception.InvalidArgsException;
import com.tmoney.foundation.utils.Configuration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static com.tmoney.foundation.utils.Configuration.Parameter.*;
import static org.openqa.selenium.remote.BrowserType.*;

/**
 * 〈web driver factory〉
 *
 * @author zhzh.yin
 * @create 2021/12/28
 */
@Slf4j
public class AppiumDriverFactory/* extends DriverFactory*/ {
     //todo
}
