package com.tengmoney.gui;

import com.tengmoney.autoframework.DriverFactory;
import com.tmoney.foundation.utils.Configuration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class AppPage extends DriverHelper {
    private final static int DURING_TIME = 1000;
    private static final int DEFAULT_TIME_OUT_SECOND = Configuration.getInt(Configuration.Parameter.IMPLICIT_TIMEOUT);
    protected AppiumDriver<MobileElement>driver;
    private final static String PIC_FILE_PATH = "src\\main\\resources\\resultPic\\";
    private final static String PIC_SUFFIX = ".png";
/*    public boolean hasElement(By by) {
        return super.hasElement(this.driver,by);
    }*/
    public AppPage(){
        super();
        driver = DriverFactory.create("device");
        //todo :应该有别的设计方法。暂时还没想到
        super.driver = driver;
        driver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,EXPLICIT_TIMEOUT );
    }

    public AppPage(AppiumDriver<MobileElement> driver) {
        super(driver);
        log.info(driver+"");
        log.info(this.driver+"");
        log.info(super.driver+"");
        this.driver = driver;
        driver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver,EXPLICIT_TIMEOUT );
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
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            driver.findElement(by).click();
        }catch(TimeoutException e){
            log.error("没找到，超时了"+e);
            driver.manage().timeouts().implicitlyWait(DEFAULT_TIME_OUT_SECOND, TimeUnit.SECONDS);
            driver.getPageSource();
            driver.findElement(by).click();
        }catch (NoSuchElementException e) {
            log.error("没找到" + e);
            e.printStackTrace();
        }catch (Exception e) {
            log.error("click的元素没找到");
            e.printStackTrace();
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
