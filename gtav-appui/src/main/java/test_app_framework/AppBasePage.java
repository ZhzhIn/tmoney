package test_app_framework;

import com.tengmoney.autoframework.BasePage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AppBasePage extends BasePage {
    private final int timeOutInSecondsDefault = 60;
    //    AndroidDriver<MobileElement> driver;
    static AppiumDriver<MobileElement> driver;
    //    IOSDriver
    WebDriverWait wait;
    String packageName;
    String activityName;

    public AppBasePage() {
    }

    public AppBasePage(String packageName, String activityName) {
        log.info("packageName is :" + packageName + ",activityName is :" + activityName);
        this.packageName = packageName;
        this.activityName = activityName;
        startApp(this.packageName, this.activityName);

    }

    public AppBasePage(AppiumDriver<MobileElement> driver) {

        this.driver = driver;
        wait = new WebDriverWait(driver, timeOutInSecondsDefault);
    }

    public List<MobileElement> findElements(By by) {

        return driver.findElements(by);

    }

    public MobileElement findElement(By by) {
        try {
            return driver.findElement(by);
        } catch (Exception e) {
            handleAlert();
            return driver.findElement(by);
        }
    }


    public void startApp(String packageName, String activityName) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "android");
        desiredCapabilities.setCapability("deviceName", "hogwarts");
        desiredCapabilities.setCapability("appPackage", packageName);
        desiredCapabilities.setCapability("appActivity", activityName);
        desiredCapabilities.setCapability("noReset", "true");
        desiredCapabilities.setCapability("udid", "");
        desiredCapabilities.setCapability("dontStopAppOnReset", "true");
        desiredCapabilities.setCapability("skipLogcatCapture", "true");
        URL remoteUrl = null;
        try {
            remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }


        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);

//        wait = new WebDriverWait(driver, timeOutInSecondsDefault);
        new WebDriverWait(driver, 30)
                .until(
                        x ->
                        {
                            log.info(String.valueOf(System.currentTimeMillis()));
                            String source = driver.getPageSource();
                            Boolean exist =source.contains("工作台") || source.contains("腾银信息");
                            log.info("寻找工作台/腾银信息"+exist);
                            return exist;
                        }
//                                driver.getPageSource().contains("工作台")
//                        ExpectedConditions.visibilityOfElementLocated(By.id(""))
                );
    }


    public void quit() {
        driver.quit();
    }


    public By byText(String text) {
        return By.xpath("//*[@text='" + text + "']");
    }

    public MobileElement find(By by) {
        return driver.findElement(by);
    }

    public MobileElement find(String text) {
        return driver.findElement(byText(text));
    }

    public void click(By by) {
        //todo: 异常处理
        try {
            findElement(by).click();
        } catch (Exception e) {
            handleAlert();
            driver.findElement(by).click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(by)).click();
    }

    public void click(String text) {
        //todo: 异常处理
        find(text).click();
    }

    public void sendKeys(By by, String content) {
        driver.findElement(by).sendKeys(content);
    }

    //todo:
    public void waitElement() {

    }

    /**
     * 弹框 确认框 广告弹窗
     */
    private static void handleAlert() {
        List<By> alertBoxs = new ArrayList<>();
        By tips = By.id("com.xueqiu.android:id/snp_tip_text");
        alertBoxs.add(By.id("com.xueqiu.android:id/image_cancle"));
        alertBoxs.add(tips);
        driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
        alertBoxs.forEach(alert -> {

            List<MobileElement> ads = driver.findElements(alert);

            if (alert.equals(tips)) {
                log.info("snb_tip found");
                Dimension size = driver.manage().window().getSize();
                try {
                    if (driver.findElements(tips).size() >= 1) {
                        new TouchAction<>(driver).tap(PointOption.point(size.width / 2, size.height / 2));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    log.info("tips checked");
                }
            } else if (ads.size() >= 1) {
                ads.get(0).click();
            }
        });
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
    }


}
