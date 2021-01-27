package com.appframework;

import org.openqa.selenium.By;

public class Wework extends AppPage {
    private By messageTab=By.xpath("//*[@text='消息']");
    public Wework() {
        super("com.tencent.wework", ".launch.LaunchSplashActivity"
        ,"127.0.0.1:7555","Android");
    }
    public MessagePage jumpToMessage(){
        click(messageTab);
        return new MessagePage(driver);
    }
    public 日程Page 日程(){
        click(By.xpath("//*[@text='日程']"));
        return new 日程Page(driver);
    }


}
