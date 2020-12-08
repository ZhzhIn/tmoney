package com.webframework;/**
 * @author zhzh.yin
 * @create 2020-11-19 19:41
 */

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * 〈〉
 *
 * @author zhzh.yin
 * @create 2020/11/19
 */
public class MorPaperPage extends WebPage {
    @FindBy(xpath = "//button/span[text()=\"新增今日早报\"]")
    private WebElement 新增今日早报;
    @FindBy(xpath = "//button/span[text()=\"编辑今日早报\"]")
    private WebElement 编辑今日早报;


    public AddPaperPage jumpToAddPaperPage() {
        if(!click(新增今日早报) ){
            click(编辑今日早报);
        }
        return new AddPaperPage(driver);
    }


    public MorPaperPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[text()=\"每日早报\"]")
    private WebElement 每日早报;

    public Boolean isMorPaperPage() {
        return hasElement(每日早报) ? true : false;
    }

}
