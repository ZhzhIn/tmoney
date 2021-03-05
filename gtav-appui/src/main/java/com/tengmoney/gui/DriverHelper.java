package com.tengmoney.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tengmoney.autoframework.PageObjectModel;
import com.tengmoney.autoframework.UITestCase;
import com.tmoney.foundation.utils.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.HandelYaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Slf4j
/**
 *  将所有page放入集合，按照用例步骤进行page的调用和执行
 */
public abstract class DriverHelper {
    protected static final Logger LOGGER = Logger.getLogger(DriverHelper.class);
    protected static final long IMPLICIT_TIMEOUT = Configuration.getLong(Configuration.Parameter.IMPLICIT_TIMEOUT);
    protected static final long EXPLICIT_TIMEOUT = Configuration.getLong(Configuration.Parameter.EXPLICIT_TIMEOUT);
    protected static final long RETRY_TIME = Configuration.getLong(Configuration.Parameter.RETRY_TIMEOUT);
    protected static  WebDriverWait wait;
    protected WebDriver driver;

    //    public WebDriver getDriver(){
//        return driver;
//    }
    public DriverHelper() {
        log.info("driver helper init ");
//        driver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT, TimeUnit.SECONDS);
//        wait = new WebDriverWait(driver,EXPLICIT_TIMEOUT );
    }

    public DriverHelper(WebDriver driver) {
        log.info("driverhelper init with driver ");
        this.driver = driver ;
    }

    List<PageObjectModel> pages = new ArrayList<>();

    public UITestCase load(String path) {
        UITestCase uiTestCase = HandelYaml
                .getYamlConfig(path, UITestCase.class);
        return uiTestCase;
    }

    public void run(UITestCase uiTestcase) {
        uiTestcase.steps.stream().forEach(
                step -> {
                    action(step);
                });
    }

    /**
     * 执行每个用例中的步骤,如果是page层面的，就调用对应的page和对应方法
     * 如果是操作层面的，就调用pageObject里面的操作处理
     * 如果是断言层面的，就调用断言方法处理
     *
     * @param map
     */
    public void action(HashMap map) {

    }

    public void loadPages(String dir) {
        Stream.of(new File(dir).list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains("page");
            }
        })).forEach(path -> {
            path = dir + "/" + path;
            System.out.println(path);
            pages.add(loadPage(path));
        });
    }

    /**
     * 加载所有的pageObject
     *
     * @param path
     * @return
     */
    public PageObjectModel loadPage(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        PageObjectModel pom = null;
        try {
            pom = mapper.readValue(
//                    Thread.currentThread().getStackTrace()[2].getClass().getResourceAsStream(path),
                    new File(path),
                    PageObjectModel.class
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pom;
    }

    /**
     * 单个操作的判定和执行
     */
    public void click(HashMap map) {
        log.info("click");
    }

    public void sendKeys(HashMap map) {
        log.info("sendKeys");
    }

    public void quit() {
        log.info("web quit");
        driver.quit();
    }

    public void wait4visible(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void wait4visible(WebElement by) {
        wait.until(ExpectedConditions.visibilityOf(by));
    }

    public By byText(String text) {
        return By.xpath("//*[@text='" + text + "']");
    }

    public void sendKeys(By by, String content) {
        driver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).sendKeys(content);
    }

    public void click(String text) {
    }

    public void savePic(String name) {
    }

    public void click(By by) {
    }

    public void upload(By by, String path) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        driver.findElement(by).sendKeys(path);
    }

    public Boolean hasElement(By by) {

        try {
            wait.until(
                            x ->
                            {
                                log.info(String.valueOf(System.currentTimeMillis()));
//                                String source = driver.getPageSource();
                                log.info("是否有by:"+by.toString());
                                return  driver.findElement(by).isDisplayed();
//                                Boolean exist =source.contains(workSpace) /*|| source.contains("腾银信息")*/;
                               
//                                return exist;
                            }
//                                driver.getPageSource().contains("工作台")
//                        ExpectedConditions.invisibilityOfElementLocated(by)
                    );
        } catch (NoSuchElementException e) {
            log.warn("没找到这个元素：" + by.toString());

        } catch (TimeoutException e){
            log.error("找不到元素，超时了："+by.toString());

        }catch (Exception e){
            log.error("没找到元素："+by.toString());

        }
       return  false;
    }

    public boolean hasElement(WebElement element) {
        try {
            log.info("等待找到元素");
            return element.isDisplayed() ? true : false;
        } catch (Exception e) {
            log.error("没有找到元素");
            return false;
        }
    }

    public void sendKeys(WebElement element, String word) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.sendKeys(word);
    }

    @Deprecated
    public void waitSecond(int second) {
        driver.manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);
    }
}
