package com.webframework;
import com.tengmoney.autoframework.PageHandler;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
@Slf4j
public class WebPage extends PageHandler {
    WebDriver driver;
    WebDriverWait wait;
    private static final String BROWSER_CHROME="chrome";

    public WebPage(){
        log.info("创建WebPage");
    }
    public WebPage(String browserName) {
        if(browserName.toLowerCase().contains(BROWSER_CHROME)){
            WebDriverManager.chromedriver().setup();
            driver=new ChromeDriver();
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        wait=new WebDriverWait(driver, 10);
    }

    public WebPage(WebDriver driver) {
        this.driver = driver;
        wait=new WebDriverWait(driver,10);
    }
    public void waitSecond(long second){
        driver.manage().timeouts().implicitlyWait(second, TimeUnit.SECONDS);
    }
    @Override
    public void quit() {
        log.info("web quit");
        driver.quit();
    }
    @Override
    public void click(By by){
        //todo: 异常处理
        wait.until(ExpectedConditions.elementToBeClickable(by));
        driver.findElement(by).click();
    }
    @Override
    public void sendKeys(By by, String content){
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        driver.findElement(by).sendKeys(content);
    }
    @Override
    public void upload(By by, String path){
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
        driver.findElement(by).sendKeys(path);
    }

    @Override
    public void click(HashMap map) {
        super.click(map);
        String key= (String) map.keySet().toArray()[0];
        String value= (String) map.values().toArray()[0];

        By by = null;
        if(key.toLowerCase().equals("id")){
            by=By.id(value);
        }
        if(key.toLowerCase().equals("linkText".toLowerCase())){
            by=By.linkText(value);
        }

        if(key.toLowerCase().equals("partialLinkText".toLowerCase())){
            by=By.partialLinkText(value);
        }

        click(by);
    }


}
