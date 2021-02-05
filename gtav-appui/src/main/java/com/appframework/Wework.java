package com.appframework;

import com.tengmoney.gui.AppPage;
import org.openqa.selenium.By;

public class Wework extends AppPage {
    private By messageTab=By.xpath("//*[@text='消息']");
    public Wework() {
        super();
    }
    public MessagePage jumpToMessage(){
        click(messageTab);
        return new MessagePage();
    }
    public 日程Page 日程(){
        click(By.xpath("//*[@text='日程']"));
        return new 日程Page();
    }


}
