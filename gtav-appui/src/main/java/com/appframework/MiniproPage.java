package com.appframework;/**
 * @author zhzh.yin
 * @create 2021-03-16 15:14
 */
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * 〈小程序页面〉
 *
 * @author zhzh.yin
 * @create 2021/3/16
 */
@Slf4j
public class MiniproPage extends Wework {
    public MiniproPage() {
        super();
        log.info("miniproPage init");
    }

    public MiniproPage(AppiumDriver<MobileElement> driver) {
        super(driver);
        log.info("miniproPage init");
    }

    public void show(){

        System.out.println(this.driver.getPageSource());

        driver.getContextHandles().forEach
            (context ->
                System.out.println(context.toString())
            );
        findTopWindows();
        String webview = driver.getContextHandles().stream()
                .filter(
                        context->
                                context.toString().contains("webView")
                ).findFirst().get().toString();
        System.out.println(webview);
        driver.context(webview);
//        findTopWindow();
        screenshot();
        driver.findElement(byText("客户")).click();
    }
    public void findTopWindows(){
        for(String win:driver.getWindowHandles()){
            if(driver.getTitle().contains(":VISIBLE")){
                log.info(driver.getTitle());
                log.info(driver.findElement(By.cssSelector("body")).getAttribute("is"));
            }else{
                driver.switchTo().window(win);
            }
        }
        log.info(driver.getPageSource());
    }
}