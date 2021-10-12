package com.webframework;

import com.tengmoney.gui.WebPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

/**
 * 〈产品页〉
 *
 * @author zhzh.yin
 * @create 2021/3/31
 */
@Slf4j
public class AllProductPage extends WebPage {
    @FindBy(xpath = "//span[text()=\"内容管理\"]")
    WebElement newsManage;
    @FindBy(xpath = "//span[text()=\"客户管理\"]")
    WebElement morningPaper;

    public AllProductPage(WebDriver driver) {
        super();
        log.info("创建AllProductPage");
        PageFactory.initElements(driver, this);
    }


}
