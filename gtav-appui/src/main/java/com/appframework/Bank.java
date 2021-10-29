package com.appframework;

import com.tengmoney.gui.AppPage;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@Slf4j
public class Bank extends AppPage {
    private final String workSpace = "工作台";
    private By hotIcon = byText("热门活动");
    private static boolean flag = false;

    public Bank() {
        super();
    }
    public Bank(AppiumDriver<WebElement> driver) {
        super(driver);
    }

    private final void initWework() throws InterruptedException {
        if(flag==true){
            return ;
        }else {
        log.info("bank init");
        Thread.sleep(3000);
        //初始化
            //click "同意“
            //click "X"
            //click 我的
            //click 开启 ，始终允许，始终允许
            wait.until(
                    x ->
                    {
                        log.info(String.valueOf(System.currentTimeMillis()));
                        String source = driver.getPageSource();
                        Boolean exist = source.contains(workSpace);
                        log.info("寻找工作台/腾银信息" + exist);
                        return exist;
                    }
            );
            flag = true;
        }
    }
    /**
     * appium不支持小程序操作
     */
    public final MiniproPage jumpToMiniproPage() {
        log.info("start jump to minipro");
//        click(miniproName);
        return new MiniproPage(driver);
    }

    public Boolean isWeworkMainPage() {
        return hasElement(hotIcon);
    }

}
