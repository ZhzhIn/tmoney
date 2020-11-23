package com.appframework;

import org.openqa.selenium.By;

public class Wework extends AppPage {
    public Wework() {
        super("com.tencent.wework", ".launch.LaunchSplashActivity"
        ,"APH0219430006864","Android");
    }

    public 日程Page 日程(){
        click(By.xpath("//*[@text='日程']"));
        return new 日程Page(driver);
    }


}
