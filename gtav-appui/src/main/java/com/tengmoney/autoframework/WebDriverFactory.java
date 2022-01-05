package com.tengmoney.autoframework;

import com.tmoney.foundation.utils.Configuration;
import com.tmoney.foundation.utils.R;
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
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.util.Arrays;
import static com.tmoney.foundation.utils.Configuration.Parameter.*;
import static org.openqa.selenium.remote.BrowserType.*;
/**
 * 〈web driver factory〉
 *
 * @author zhzh.yin
 * @create 2021/12/28
 */
@Slf4j
public class WebDriverFactory implements DriverFactory {
    private static WebDriver driver;
    private static DesiredCapabilities capabilities = null;

    public static synchronized <T extends WebDriver>T createDriver()
    {
        return  createDriver("");
    }
    public static synchronized <T extends WebDriver>T createDriver (String testName)
    {
        if(driver!=null){
            log.info("沿用已有driver");
            return (T) driver;
        }else{
            log.info("当前DriverFactory没有driver，创建1个");
            try
            {
                log.info("try to init webdriver");
                if (FIREFOX.equalsIgnoreCase(Configuration.get(BROWSER)))
                {
                    capabilities = getFirefoxCapabilities(testName);
                    FirefoxOptions options = new FirefoxOptions(capabilities);
                    driver = new FirefoxDriver(options);

                }
                else if (IEXPLORE.equalsIgnoreCase(Configuration.get(BROWSER)))
                {
                    capabilities = getInternetExplorerCapabilities(testName);
                    InternetExplorerOptions options = new InternetExplorerOptions(capabilities);
                    driver =  new InternetExplorerDriver(options);
                }
                else if (HTMLUNIT.equalsIgnoreCase(Configuration.get(BROWSER)))
                {
                    capabilities = getHtmlUnitCapabilities(testName);
                    ChromeOptions options = new ChromeOptions();
                    options.merge(capabilities);
                    //设置为无头浏览器
                    options.setHeadless(true);
                    driver =  new ChromeDriver(options);
                }
                else
                {
                    capabilities = getChromeCapabilities(testName);
                    ChromeOptions options = new ChromeOptions();
                    options.merge(capabilities);
                    driver =   new ChromeDriver(options) {
                    };
                }
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException("Can't connect to selenium server: " + Configuration.get(SELENIUM_HOST));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (T) driver;
        }
    }

    private static DesiredCapabilities getFirefoxCapabilities(String testName) throws MalformedURLException
    {
        if(capabilities !=null){
            return capabilities;
        }
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities = initBaseCapabilities(capabilities, Platform.WINDOWS, Configuration.get(BROWSER),
                R.CONFIG.get("firefox_version"), "name", testName);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, false);
        FirefoxProfile profile = new FirefoxProfile();
        if (!StringUtils.isEmpty(Configuration.get(Configuration.Parameter.USER_AGENT)) && !"n/a".equals(Configuration.get(Configuration.Parameter.USER_AGENT)))
        {
            profile.setPreference("general.useragent.override", Configuration.get(Configuration.Parameter.USER_AGENT));
        }
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        return capabilities;
    }
    private static DesiredCapabilities getInternetExplorerCapabilities(String testName) throws MalformedURLException
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities = initBaseCapabilities(capabilities, Platform.WINDOWS, Configuration.get(BROWSER),
                R.CONFIG.get("ie_version"), "name", testName);
        capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, false);
        return capabilities;
    }

    private static DesiredCapabilities getChromeCapabilities(String testName) throws MalformedURLException
    {
        if(capabilities !=null){
            return capabilities;
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities = initBaseCapabilities(capabilities, Platform.WINDOWS, Configuration.get(BROWSER),
                R.CONFIG.get("chrome_version"), "name", testName);
        capabilities.setCapability("chrome.switches", Arrays.asList("--start-maximized", "--ignore-certificate-errors"));
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, false);
        return capabilities;
    }

    private static DesiredCapabilities getHtmlUnitCapabilities(String testName) throws MalformedURLException
    {
        if(capabilities !=null){
            return capabilities;
        }
        DesiredCapabilities capabilities = DesiredCapabilities.htmlUnit();
        capabilities.setPlatform(Platform.WINDOWS);
        capabilities.setJavascriptEnabled(true);
        return capabilities;
    }

    private static DesiredCapabilities initBaseCapabilities(DesiredCapabilities capabilities, Platform platform, String... args)
    {
        if(capabilities !=null){
            return capabilities;
        }
        capabilities.setPlatform(platform);
        capabilities.setBrowserName(args[0]);
        capabilities.setVersion(args[1]);
        capabilities.setCapability("name", args[2]);
        return capabilities;
    }
}
