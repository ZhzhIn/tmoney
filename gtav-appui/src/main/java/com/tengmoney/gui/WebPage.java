package com.tengmoney.gui;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;

@Slf4j
public class WebPage extends DriverHelper {
    protected WebDriver driver;
    protected WebDriverWait wait;
    public WebPage() {
        super();
    }
    public WebPage(WebDriver driver) {
        super();
        super.driver = driver;
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
        moveTo(driver.findElement(by));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(by)).click();
            log.info("click by ");
        } catch (ElementClickInterceptedException e) {
            log.info("by is not clickable");
            log.info("当前坐标" + driver.findElement(by).getLocation().toString());
            //坐标并不对,这个位置到底是啥
            log.info("使用action点击");
            Actions action = new Actions(driver);
            action.click(driver.findElement(by)).perform();
        }
    }


    public void moveTo(WebElement element ){
       /* Actions action =new Actions(driver);
        action.moveToElement(element);*/
        JavascriptExecutor driver_js= (JavascriptExecutor) driver;
        //利用js代码键入搜索关键字
        hasElement(element);
        //向下滑动直到找到元素下一页
        driver_js.executeScript("arguments[0].scrollIntoView(true)",element);
    }
    @Deprecated
    public void scrollTo(WebElement element){
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
