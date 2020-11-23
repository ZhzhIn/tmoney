package com.webframework;

import com.tengmoney.autoframework.PageHandler;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
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

    @Override
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

    public boolean hasElement(By by) {
        try {
            log.info("等待找到by元素");
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            return driver.findElements(by).size()>0;
        } catch (Exception e) {
            log.error("没有找到by元素");
            return false;
        }
    }

    @Override
    public void sendKeys(By by, String content) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).sendKeys(content);
    }

    @Override
    public void upload(By by, String path) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        driver.findElement(by).sendKeys(path);
    }

    @Override
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
