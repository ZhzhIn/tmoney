package com.appframework;

import org.openqa.selenium.By;

public class Wework extends AppPageHandler {
    public Wework() {
        super("com.tencent.wework", ".launch.LaunchSplashActivity"
        ,"APH0219430006864","Android");
    }

    public 日程PageHandler 日程(){
        click(By.xpath("//*[@text='日程']"));
        return new 日程PageHandler(driver);
    }


}
