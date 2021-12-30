package com.appframework;

import com.tengmoney.gui.AppPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 〈〉
 *
 * @author zhzh.yin
 * @create 2021/9/28
 */
@Slf4j
public class MePage extends AppPage {
    private final By everydayProfit = byText("每日福利");
    private final By signNow = byText("立即签到");
    private final By confirm = byText("确定");
    private final By goToLaunchGame = By.xpath("//*[@text='启动游戏']/*[@text='去完成']");
    private final By launchGame = byText("启动游戏");



    public MePage(AppiumDriver<WebElement> driver) {
        super(driver);
    }
    public MePage receiveBenefits(){
        click(everydayProfit);
        click(signNow);
        click(confirm);
        click(goToLaunchGame);
        click(launchGame);
        return this;
    }

}
