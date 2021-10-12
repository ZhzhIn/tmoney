package com.appframework;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WebViewTest {

    private AndroidDriver driver;
    private int index = 0;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "android");
        desiredCapabilities.setCapability("platformVersion", "10.0");
        desiredCapabilities.setCapability("deviceName", "device");
        desiredCapabilities.setCapability("udid", "127.0.0.1:7555");
        desiredCapabilities.setCapability("appPackage", "com.xueqiu.android");
        desiredCapabilities.setCapability("appActivity", "view.WelcomeActivityAlias");
        desiredCapabilities.setCapability("noReset", "true");
        desiredCapabilities.setCapability("dontStopAppOnReset", "true");
        URL remoteUrl = new URL("http://127.0.0.1:4723/wd/hub");

        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        //todo: 等待优化
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        System.out.println(driver.getPageSource());
    }

//    @Test
    public void webview_native() {
        driver.findElement(By.xpath("//*[@text='交易']")).click();
        driver.findElement(By.xpath("//*[@text='基金开户']")).click();
    }

//    @Test
    public void webview_web() throws InterruptedException {
        driver.findElement(By.xpath("//*[@text='交易']")).click();
        for (int i = 0; i < 2; i++) {
            driver.getContextHandles().forEach(context -> System.out.println(context.toString()));
            Thread.sleep(500);
        }
        driver.context(driver.getContextHandles().toArray()[1].toString());

        driver.getWindowHandles().forEach(window -> {
            System.out.println(window);
            System.out.println(driver.getTitle());
            driver.switchTo().window(window);
            System.out.println(driver.getPageSource());
        });
        Object[] array = driver.getWindowHandles().toArray();
        driver.switchTo().window(array[array.length - 1].toString());

        driver.findElement(By.cssSelector(".trade_home_info_3aI")).click();

    }

    @Test
    public void sampleTest() {
        MobileElement el4 = (MobileElement) driver.findElementById("com.xueqiu.android:id/home_search");
        el4.click();
        MobileElement el5 = (MobileElement) driver.findElementById("com.xueqiu.android:id/search_input_text");
        el5.sendKeys("alibaba");
        MobileElement el6 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.RelativeLayout/android.widget.FrameLayout/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.RelativeLayout[1]/android.widget.LinearLayout/android.widget.TextView[1]");
        el6.click();
    }

    @After
    public void tearDown() throws InterruptedException {
        Thread.sleep(20000);
        driver.quit();
    }
    public void screenshot() {
        //截图
        index += 1;
        String path = ".";
        try {
            FileUtils.copyFile(driver.getScreenshotAs(OutputType.FILE),
                    new File(String.format("%s/wx_%s.png", path, index)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void findTopWindow() {
        for (String win : driver.getWindowHandles()) {
            if (driver.getTitle().contains(":VISIBLE")) {
                System.out.println(driver.getTitle());
                System.out.println(driver.findElement(By.cssSelector("body")).getAttribute("is"));
            } else {
                driver.switchTo().window(win);
            }
        }
        System.out.println(driver.getPageSource());
    }


}
