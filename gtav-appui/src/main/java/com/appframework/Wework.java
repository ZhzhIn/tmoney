package com.appframework;

import com.tengmoney.autoframework.DriverFactory;
import com.tengmoney.gui.AppPage;
import com.tmoney.foundation.utils.Configuration;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.Driver;

import static com.tmoney.foundation.utils.Configuration.Parameter.*;

@Slf4j
public class Wework extends AppPage {
    private final String workSpace = "工作台";
    private By messageTab = byText("消息");
    private By workbench = byText("工作台");
    //手机号输入框
    private By phoneInput = By.id("com.tencent.wework:id/fow");

    private By nextStep = By.id("com.tencent.wework:id/di");
    private static boolean flag = false;
    //验证码
//    private By varifyCode = By.id("com.tencent.wework:id/dq2");
    private By h5app = byText(Configuration.get(H5APPLICATIONNAME));
    //手机号输入框
    private By loginWithNum= By.id("com.tencent.wework:id/fop");
    private By companyName = byText(Configuration.get(COMPANY));
    private By miniproName = byText(Configuration.get(MINIPRONAME));
    private By h5Station = byText(Configuration.get(H5APPLICATIONNAME));
    private static String phoneNum = "13242424028";
    public Wework(){
       super();
    }

    public Wework(AppiumDriver<WebElement> driver) {
        super(driver);

        log.info("wework init with driver");
    }

    private final void initWework() {
        if(flag==true){
            return ;
        }else {
        log.info("wework init");
        //TODO 进入配置的公司
            if(hasElement(loginWithNum)){
                click(loginWithNum);
//            click(phoneInput);
                sendKeys(phoneInput,phoneNum);
                click(nextStep);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("输入验证码");
                click(nextStep);
                //同意
                driver.findElementById("com.tencent.wework:id/gg5").click();
                click(companyName);
                //同意
                driver.findElementById("com.tencent.wework:id/cey").click();
                //权限按钮
                driver.findElementById("com.android.packageinstaller:id/permission_allow_button").click();
                //权限按钮
                driver.findElementById("com.android.packageinstaller:id/permission_allow_button").click();
                //权限按钮
                driver.findElementById("com.android.packageinstaller:id/permission_allow_button").click();
            }
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
        click(workbench);
        click(miniproName);
        return new MiniproPage(driver);
    }
    public final H5Page jumpToH5Page() {
        log.info("start jump to h5Page");
        click(workbench);
        click(h5Station);
        return new H5Page(driver);
    }
    public final MessagePage jumpToMessage() {
        click(messageTab);
        //这里必须带上参数driver，否则会新开一个窗口
        return new MessagePage(driver);
    }

    public final 日程Page 日程() {
        click(By.xpath("//*[@text='日程']"));
        return new 日程Page(driver);
    }

    public Boolean isWeworkMainPage() {
        return hasElement(messageTab);
    }

}
