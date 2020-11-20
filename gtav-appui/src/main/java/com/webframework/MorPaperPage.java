package com.webframework;/**
 * @author zhzh.yin
 * @create 2020-11-19 19:41
 */

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * 〈〉
 *
 * @author zhzh.yin
 * @create 2020/11/19
 */
public class MorPaperPage extends WebPage{
    public MorPaperPage(WebDriver driver){
        super(driver);
    }
    @FindBy(xpath = "//div[@text=\"每日早报\"]")
    private By 每日早报;

/*    public Boolean isMorPaperPage(){

        return driver.findElement(By.xpath("//div[@text=\"每日早报\"]")) ? true:false;
//                hasElement(By.xpath("//div[@text=\"每日早报\"]")) ?  true : false;
    }*/
}
