package com.appframework;

import com.tengmoney.autoframework.PageHandler;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class AppPage extends PageHandler {
    private final static int DURING_TIME = 1000;
    private final static int DEFAULT_TIME_OUT_SECOND = 5;
    private final static String PLATFORM_NAME = "platformName";
    private final static String DEVICE_NAME = "deviceName";
    private final static String APP_PACKAGE = "appPackage";
    private final static String APP_ACTIVITY = "appActivity";
    private final static String NO_RESET = "noReset";
    private final static String DONT_STOP_APP_ON_RESET = "dontStopAppOnReset";
    private final static String SKIP_LOGCAT_CAPTURE = "skipLogcatCapture";
    private final static String REMOTE_URL = "http://127.0.0.1:4723/wd/hub";
    private final static String PIC_FILE_PATH = "src\\main\\resources\\resultPic\\";
    private final static String PIC_SUFFIX = ".png";
    static AppiumDriver<MobileElement> driver;
    static WebDriverWait wait;
    String packageName;
    String activityName;
    String platform;
    String deviceName;

/*    public boolean hasElement(By by) {
        return super.hasElement(this.driver,by);
    }*/
    public AppPage(String packageName, String activityName, String deviceName, String platform) {

        log.info("packageName is :" + packageName + ",activityName is :" + activityName);
        this.packageName = packageName;
        this.activityName = activityName;
        this.deviceName = deviceName;
        this.platform = platform;
        startApp(this.packageName, this.activityName, this.deviceName, this.platform);
    }

    public AppPage(AppiumDriver<MobileElement> driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,DEFAULT_TIME_OUT_SECOND);
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
    @Override
    public void savePic(String picName)  {
        super.savePic(picName);
        log.info("current method is："+picName);
        String timestamp = new Date().toString();
        File file = new File(PIC_FILE_PATH+picName+PIC_SUFFIX);
        log.info("pic path is :"+file);
        File screenShotFile = driver.getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenShotFile, file);
        }
        catch (IOException e) {e.printStackTrace();}
    }
    public void startApp(String packageName, String activityName, String deviceName, String platform) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(PLATFORM_NAME, platform);
        desiredCapabilities.setCapability(DEVICE_NAME, deviceName);
        desiredCapabilities.setCapability(APP_PACKAGE, packageName);
        desiredCapabilities.setCapability(APP_ACTIVITY, activityName);
        desiredCapabilities.setCapability(NO_RESET, "true");
        desiredCapabilities.setCapability(DONT_STOP_APP_ON_RESET, "true");
        desiredCapabilities.setCapability(SKIP_LOGCAT_CAPTURE, "true");
        URL remoteUrl = null;
        try {
            remoteUrl = new URL(REMOTE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }
        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
        new WebDriverWait(driver, DEFAULT_TIME_OUT_SECOND)
                .until(
                        x ->
                        {
                            log.info(String.valueOf(System.currentTimeMillis()));
                            String source = driver.getPageSource();
                            Boolean exist = source.contains("工作台") || source.contains("腾银信息");
                            log.info("寻找工作台/腾银信息：" + exist);
                            Boolean existI = source.contains("消息");
                            log.info("寻找消息：" + existI);
                            return exist;
                        }
                );
        //todo 不知道怎么优化
        super.setDriver(driver);
        wait = new WebDriverWait(driver,DEFAULT_TIME_OUT_SECOND);
        super.setWait(wait);
    }

    @Override
    public void quit() {
        driver.quit();
    }



    public MobileElement find(By by) {
        return driver.findElement(by);
    }

    public MobileElement find(String text) {
        By by = byText(text);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        return driver.findElement(byText(text));
    }

    @Override
    public void click(By by) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by)).click();
        }catch(TimeoutException e){
            log.error("没找到，超时了"+e);
            driver.manage().timeouts().implicitlyWait(DEFAULT_TIME_OUT_SECOND, TimeUnit.SECONDS);
            driver.getPageSource();
            driver.findElement(by).click();
        }catch (NoSuchElementException e){
            log.error("没找到"+e);
            e.printStackTrace();
        }catch (Exception e) {
            //handleAlert();
        }
    }

    //控件内部滑来滑去
    public  void innerElementSweepToLeftUp(MobileElement element) {
        //Y不变，X由大变小，y取center，x取center->locate
        Point center = element.getCenter();//中心点
        Point locate = element.getLocation();//元素左上角相对于屏幕左上角的偏移量
        pointToPoint(PointOption.point(center),PointOption.point(locate),DURING_TIME);
    }
    public  void innerElementSweepToRightDown(MobileElement element) {
        //Y不变，X由大变小，y取center，x取center->locate
        Point center = element.getCenter();//中心点
        Point locate = new Point(2*element.getCenter().getX()-element.getLocation().getX(),
                2*element.getCenter().getY()-element.getLocation().getY());//元素左上角相对于屏幕左上角的偏移量
        pointToPoint(PointOption.point(center),PointOption.point(locate),DURING_TIME);
    }
    //控件内部滑来滑去
    public  void innerElementSweepToLeftUp(By by) {
        //Y不变，X由大变小，y取center，x取center->locate
        MobileElement element  = driver.findElement(by);
        Point center = element.getCenter();//中心点
        Point locate = element.getLocation();//元素左上角相对于屏幕左上角的偏移量
        pointToPoint(PointOption.point(center),PointOption.point(locate),DURING_TIME);
    }
    public  void innerElementSweepToRightDown(By by) {
        //Y不变，X由大变小，y取center，x取center->locate
        MobileElement element  = driver.findElement(by);
        Point center = element.getCenter();//中心点
        Point locate = new Point(2*element.getCenter().getX()-element.getLocation().getX(),
                2*element.getCenter().getY()-element.getLocation().getY());//元素左上角相对于屏幕左上角的偏移量
        pointToPoint(PointOption.point(center),PointOption.point(locate),DURING_TIME);
    }

    private void pointToPoint(PointOption start, PointOption end, int during) {
        TouchAction ta = new TouchAction(driver);
        ta.press(start)
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(during)))
                .moveTo(end)
                .release().perform();
    }

    /**
     * 上滑
     *
     * @param during
     * @param num
     */
    public  void swipeToUp(int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        PointOption start = PointOption.point(width / 2, height * 7 / 8);
        PointOption end = PointOption.point(width / 2, height / 8);
        for (int i = 0; i < num; i++) {
            pointToPoint(start, end, during);
        }
    }

    public  void swipeToUp() {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        PointOption start = PointOption.point(width / 2, height * 7 / 8);
        PointOption end = PointOption.point(width / 2, height / 8);
        pointToPoint(start, end, DURING_TIME);
    }


    public void longPress(By by, int during) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        TouchAction ta = new TouchAction(driver);
        ta.press(PointOption.point(driver.findElement(by).getLocation()))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(during)))
                .release().perform();
    }

    /**
     * 下拉
     *
     * @param during 下拉耗时
     * @param num    下拉次数
     */
    public  void swipeToDown(int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        PointOption start = PointOption.point(width / 2, height / 8);
        PointOption end = PointOption.point(width / 2, height * 7 / 8);
        for (int i = 0; i < num; i++) {
            pointToPoint(start, end, during);
        }
    }

    public  void swipeToDown() {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        PointOption start = PointOption.point(width / 2, height / 8);
        PointOption end = PointOption.point(width / 2, height * 7 / 8);
        pointToPoint(start, end, DURING_TIME);
    }

    /**
     * 向左滑动
     *
     * @param during
     * @param num
     */
    public  void swipeToLeft(int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;

        PointOption start = PointOption.point(width * 3 / 4, height / 2);
        PointOption end = PointOption.point(width / 4, height / 2);
        for (int i = 0; i < num; i++) {
            pointToPoint(start, end, during);
        }

    }

    public  void swipeToLeft() {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        PointOption start = PointOption.point(width * 3 / 4, height / 2);
        PointOption end = PointOption.point(width / 4, height / 2);
        pointToPoint(start, end, DURING_TIME);
    }

    /**
     * 向右滑动
     *
     * @param during
     * @param num
     */
    public  void swipeToRight(int during, int num) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        PointOption start = PointOption.point(width / 4, height / 2);
        PointOption end = PointOption.point(width * 3 / 4, height / 2);
        for (int i = 0; i < num; i++) {
            pointToPoint(start, end, during);
        }
    }

    public  void swipeToRight() {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        PointOption start = PointOption.point(width / 4, height / 2);
        PointOption end = PointOption.point(width * 3 / 4, height / 2);
        pointToPoint(start, end, DURING_TIME);
    }

    @Override
    public void click(String text) {
        find(text).click();
    }

    /**
     * 弹框 确认框 广告弹窗
     */
    private  void handleAlert() {
        List<By> alertBoxs = new ArrayList<>();
        By tips = By.id("com.xueqiu.android:id/snp_tip_text");
        alertBoxs.add(By.id("com.xueqiu.android:id/image_cancle"));
        alertBoxs.add(tips);
        driver.manage().timeouts().implicitlyWait(DEFAULT_TIME_OUT_SECOND, TimeUnit.SECONDS);
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
        driver.manage().timeouts().implicitlyWait(DEFAULT_TIME_OUT_SECOND, TimeUnit.SECONDS);
    }
}
