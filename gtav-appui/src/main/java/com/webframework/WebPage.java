package com.webframework;

import com.tengmoney.autoframework.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
public class WebPage extends PageHandler {
    WebDriver driver;
    WebDriverWait wait;
    private static final String BROWSER_CHROME = "chrome";

    public WebPage() {
        log.info("创建WebPage");
    }

    @Deprecated
    public WebPage(String browserName) {
        if (browserName.toLowerCase().contains(BROWSER_CHROME)) {
//            WebDriverManager.chromedriver().setup();
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            driver = new ChromeDriver();
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);
    }

    public WebPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    public void waitSecond(long second) {
        driver.manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);
    }

    @Override
    public void quit() {
        log.info("web quit");
        driver.quit();
    }

    public boolean click(WebElement element) {
        boolean flag = false;
        //todo: 异常处理
        try {
            moveTo(element);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            log.info("click element ");
            flag = true;
        } catch (ElementClickInterceptedException e) {
            //坐标并不对,这个位置到底是啥
            log.info("不可点击 使用action点击");
            Actions action = new Actions(driver);
            action.click(element).perform();
            flag=true;
        }catch(StaleElementReferenceException e){
            log.info("按钮被遮挡了");
            flag = false;
        }catch(NoSuchElementException e){
            log.info("按钮消失了");
            flag = false ;
        }
        return flag;

    }

    @Override
    @Deprecated
    public void click(By by) {
        //todo: 异常处理
        wait.until(ExpectedConditions.elementToBeClickable(by));
        try {
            driver.findElement(by).click();
            log.info("click by ");
        } catch (ElementClickInterceptedException e) {
            log.info("by is not clickable");
            /*
            //移动到元素
            JavascriptExecutor jse2 = (JavascriptExecutor)driver;
            jse2.executeScript("arguments[0].scrollIntoView()", driver.findElement(by));
             */
            wait.until(ExpectedConditions.elementToBeClickable(by));
            log.info("当前坐标" + driver.findElement(by).getLocation().toString());
            //坐标并不对,这个位置到底是啥
            log.info("使用action点击");
            Actions action = new Actions(driver);
            action.click(driver.findElement(by)).perform();
        }
    }

    public boolean hasElement(WebElement element) {
        try {
            log.info("等待找到元素");
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            log.error("没有找到元素");
            return false;
        }
    }
    @Deprecated
    public boolean hasElement(By by) {
        try {
            log.info("等待找到by元素");
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return driver.findElements(by).size() > 0;
        } catch (Exception e) {
            log.error("没有找到by元素");
            return false;
        }
    }

    @Override
    @Deprecated
    public void sendKeys(By by, String content) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).sendKeys(content);
    }
    @Deprecated
    public void wait4visible(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public void wait4visible(WebElement by) {
        wait.until(ExpectedConditions.visibilityOf(by));
    }

    //    @Override
    public void sendKeys(WebElement element, String path) {
        wait.until(ExpectedConditions.visibilityOf(element));
        moveTo(element);
        element.sendKeys(path);
    }

    @Override
    @Deprecated
    public void upload(By by, String path) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        driver.findElement(by).sendKeys(path);
    }
    public void moveTo(WebElement element ){
        Actions action =new Actions(driver);
        action.moveToElement(element);
    }
    @Override
    @Deprecated
    public void click(HashMap map) {
        super.click(map);
        String key = (String) map.keySet().toArray()[0];
        String value = (String) map.values().toArray()[0];

        By by = null;
        if (key.toLowerCase().equals("id")) {
            by = By.id(value);
        }
        if (key.toLowerCase().equals("linkText".toLowerCase())) {
            by = By.linkText(value);
        }

        if (key.toLowerCase().equals("partialLinkText".toLowerCase())) {
            by = By.partialLinkText(value);
        }
        click(by);
    }
}
