package com.appframework.page;

import com.tengmoney.gui.AppPage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 〈王者营地〉
 *
 * @author zhzh.yin
 * @create 2021/9/27
 */

public class KingOfGloryHome extends  AppPage {
    //todo:多版本app、多平台的app 定位符通常有差别
    private final By weChatLogin = byText("微信登录");
    private final By QQLogin = byText("QQ登录");
    private final By agree = byText("同意");
    private final By confirm = byText("确定");
    private final By me = byText("我");

    public KingOfGloryHome() {
        super();
    }
    public KingOfGloryHome(AppiumDriver<WebElement> driver) {
        super(driver);
    }

    public KingOfGloryHome wechatLogin(){
        click(weChatLogin);
        click(agree);
        click(confirm);
        return this;
    }
    public MePage jumpToMe(){
        click(me);
        return new MePage(this.driver);
    }

}
