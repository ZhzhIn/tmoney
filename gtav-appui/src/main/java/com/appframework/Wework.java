package com.appframework;

import com.tengmoney.gui.AppPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
@Slf4j
public class Wework extends AppPage {
    private final String workSpace = "工作台";
    private By messageTab=byText("消息");

    public Wework() {
        super();
        log.info("wework init");
        new WebDriverWait(driver, 30)
                .until(
                        x ->
                        {
                            log.info(String.valueOf(System.currentTimeMillis()));
                            String source = driver.getPageSource();
                            Boolean exist =source.contains("工作台") || source.contains("腾银信息");
                            log.info("寻找工作台/腾银信息"+exist);
                            return exist;
                        }
//                                driver.getPageSource().contains("工作台")
//                        ExpectedConditions.visibilityOfElementLocated(By.id(""))
                );
    }

    public Wework(AppiumDriver<MobileElement> driver) {
        super(driver);
        log.info("wework init");
        new WebDriverWait(driver, 30)
                .until(
                        x ->
                        {
                            log.info(String.valueOf(System.currentTimeMillis()));
                            String source = driver.getPageSource();
                            Boolean exist =source.contains(workSpace) /*|| source.contains("腾银信息")*/;
                            log.info("寻找工作台/腾银信息"+exist);
                            return exist;
                        }
//                                driver.getPageSource().contains("工作台")
//                        ExpectedConditions.visibilityOfElementLocated(By.id(""))
                );
    }

    public MessagePage jumpToMessage(){
        click(messageTab);
        //这里必须带上参数driver，否则会新开一个窗口
        return new MessagePage(driver);
    }
    public 日程Page 日程(){
        click(By.xpath("//*[@text='日程']"));
        return new 日程Page();
    }
    public Boolean isWeworkMainPage(){
        return hasElement(messageTab);
    }

}
