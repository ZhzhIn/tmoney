package com.tengmoney.autoframework;

import com.tengmoney.foundation.exception.InvalidArgsException;
import com.tmoney.foundation.utils.Configuration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
public class AppiumDriverFactory implements DriverFactory {
    private final static String MINIPRO = "minipro";
    private static WebDriver driver;
    private static DesiredCapabilities capabilities = null;
//    private static DriverFactory factory = null;
    //    private DriverFactory(){
//        log.info("init a DriverFactory");
//    }
//    public static DriverFactory getDriverFactory(){
//        if(factory ==null){
//            factory = new DriverFactory();
//        }
//        return factory;
//    }
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
                log.info("try to init appiumDriver");
                if (IPHONE.equalsIgnoreCase(Configuration.get(BROWSER)))
                {
                    if (Configuration.isNull(Configuration.Parameter.MOBILE_OS)
                            || Configuration.isNull(Configuration.Parameter.MOBILE_VERSION)
                            || Configuration.isNull(Configuration.Parameter.MOBILE_PLATFORM)
                            || Configuration.isNull(MOBILE_APP)
                            || Configuration.isNull(Configuration.Parameter.MOBILE_DEVICE)) {
                        throw new InvalidArgsException("'MOBILE_OS', 'MOBILE_DEVICE', 'MOBILE_VERSION', 'MOBILE_PLATFORM', 'MOBILE_APP' should be set!");
                    }
                    capabilities = getIphoneCapabilities(testName);
                    driver =   new IOSDriver(new URL(Configuration.get(SELENIUM_HOST)), capabilities);
                }
                else if (ANDROID.equalsIgnoreCase(Configuration.get(BROWSER)))
                {

                    if (StringUtils.isAllBlank(Configuration.get(MOBILE_APP),
                            Configuration.get(MOBILE_PLATFORM),
                            Configuration.get(MOBILE_PACKAGE),
                            Configuration.get(MOBILE_ACTIVITY),
                            Configuration.get(MOBILE_DEVICE) )){
                        log.error("'MOBILE_APP', 'MOBILE_VERSION', 'MOBILE_PLATFORM', 'MOBILE_PACKAGE', 'MOBILE_ACTIVITY', 'MOBILE_DEVICE' should be set!");
                        throw new InvalidArgsException("'MOBILE_APP', 'MOBILE_VERSION', 'MOBILE_PLATFORM', 'MOBILE_PACKAGE', 'MOBILE_ACTIVITY', 'MOBILE_DEVICE' should be set!");
                    }
                    log.info("driver factory init AndroidDriver :"+Configuration.get(MOBILE_APP) +
                            Configuration.get(MOBILE_PLATFORM) +
                            Configuration.get(MOBILE_PACKAGE) +
                            Configuration.get(MOBILE_ACTIVITY) +
                            Configuration.get(MOBILE_DEVICE));
                    capabilities = getAndroidCapabilities(testName);
                    try{
                    driver = new AndroidDriver(new URL(Configuration.get(ANDROID_HOST)), capabilities);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    log.info("here ???");
                }
                else if(MINIPRO.equalsIgnoreCase(Configuration.get(BROWSER))){
                    capabilities = getMiniProCapabilities(testName);
                    driver =   new AppiumDriver(new URL(Configuration.get(SELENIUM_HOST)),capabilities);
                }
                else
                {
                    //todo getAppiumCapabilities
                    capabilities = getAndroidCapabilities(testName);
                    driver =   new AppiumDriver(new URL(Configuration.get(SELENIUM_HOST)), capabilities);
//                throw new Exception("没有该类型的driver");
                }
            }
            catch (MalformedURLException e)
            {
                throw new RuntimeException("Can't connect to selenium server: " + Configuration.get(SELENIUM_HOST));
            } catch (Exception e) {
                e.printStackTrace();
            }
            log.info(driver==null? "null":"not null");
            return (T) driver;
        }
    }

    private static DesiredCapabilities getIphoneCapabilities(String testName) throws MalformedURLException
    {
        if(capabilities !=null){
            return capabilities;
        }
        DesiredCapabilities capabilities = DesiredCapabilities.iphone();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, Configuration.get(Configuration.Parameter.MOBILE_OS));
        capabilities.setCapability("device", Configuration.get(Configuration.Parameter.MOBILE_DEVICE));
        capabilities.setCapability(CapabilityType.VERSION, Configuration.get(Configuration.Parameter.MOBILE_VERSION));
        capabilities.setCapability(CapabilityType.PLATFORM_NAME, Configuration.get(Configuration.Parameter.MOBILE_PLATFORM));
        if(!new File(Configuration.get(MOBILE_APP)).exists())
        {
            throw new InvalidArgsException("No application found: " + Configuration.get(MOBILE_APP));
        }
        capabilities.setCapability("app", Configuration.get(MOBILE_APP));
        capabilities.setCapability("name", testName);
        return capabilities;
    }
    @Deprecated
    private static DesiredCapabilities getMiniProCapabilities(String testName){
        //todo iOS
        if(capabilities !=null){
            return capabilities;
        }
        //小程序的进程名和报名不一样，需要加上这个参数
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("androidProcess","com.tencent.mm:appbrand0");
        DesiredCapabilities desiredCapabilities = getAndroidCapabilities(testName);
        //小程序相关设置
        chromeOptions.setExperimentalOption("androidProcess","com.tencent.mm:tools");
        desiredCapabilities.setCapability("goog:chromeOptions",chromeOptions);
        //默认生成的browserName = chrome 的设置需要去掉
        desiredCapabilities.setCapability("browserName","");
        desiredCapabilities.setCapability("chromedriverExecutable",DriverFactory.class.getClassLoader().getResource("/chromeDriver"));
        desiredCapabilities.setCapability("showChromedriverLog",true);

        //通过自己的adb代理修复chromeDriver的bug并解决@xweb_devtools_remote的问题
//        desiredCapabilities.setCapability("adbPort","5038");*/
        return  desiredCapabilities;
    }
    private static DesiredCapabilities getAndroidCapabilities(String testName) {
        if(capabilities !=null){
            return capabilities;
        }
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        //这里不能用.android()方法生成，会报错browsername和appactivity不能同时出现
        desiredCapabilities.setCapability(CapabilityType.VERSION, Configuration.get(Configuration.Parameter.MOBILE_VERSION));
        desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, Configuration.get(Configuration.Parameter.MOBILE_PLATFORM));
        desiredCapabilities.setCapability("appPackage", Configuration.get(Configuration.Parameter.MOBILE_PACKAGE));
        desiredCapabilities.setCapability("noReset", Configuration.get(Configuration.Parameter.MOBILE_NORESET));
        desiredCapabilities.setCapability("appActivity", Configuration.get(Configuration.Parameter.MOBILE_ACTIVITY));
        desiredCapabilities.setCapability("newCommandTimeout", Configuration.get(Configuration.Parameter.MOBILE_NEW_COMMAND_TIMEOUT));
        desiredCapabilities.setCapability("deviceName", Configuration.get(Configuration.Parameter.MOBILE_DEVICE));
        //加速
        desiredCapabilities.setCapability("skipLogcatCapture","true");
        desiredCapabilities.setCapability("showChromedriverLog",true);
        //通过自己的adb代理修复chromeDriver的bug并解决@xweb_devtools_remote的问题
        //desiredCapabilities.setCapability("adbPort","5038");
        desiredCapabilities.setCapability("automationName", "uiautomator2");
        log.info("init Android capabilities"+desiredCapabilities.toString());/**/
        return desiredCapabilities;
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
