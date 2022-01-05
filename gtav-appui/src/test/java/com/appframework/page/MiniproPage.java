package com.appframework.page;/**
 * @author zhzh.yin
 * @create 2021-03-16 15:14
 */
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 〈小程序页面〉
 *
 * @author zhzh.yin
 * @create 2021/3/16
 */
@Slf4j
public class MiniproPage extends Wework {
    public MiniproPage(AppiumDriver<WebElement> driver) {
        super(driver);
        log.info("miniproPage init");
    }

    public void show(){
        //切换到context,再切换到handle中
        //context对应top进程名
        //handle需要遍历，找到pagesource有需要的元素的

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.getContextHandles().forEach
            (context ->
                System.out.println(context.toString())
            );
        String webview = driver.getContextHandles().stream()
                .filter(
                        context->
                                context.toString().contains("WEBVIEW")
                ).findFirst().get().toString();
        System.out.println("------------");
        System.out.println(webview);
        System.out.println("------------");
//        System.out.println(driver.getPageSource());
        //现切换到元素所在的context
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