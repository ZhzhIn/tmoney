package com.webframework.autocase;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 〈test〉
 *
 * @author zhzh.yin
 * @create 2021/3/4
 */
public class TestWework {
    @Test
    public void test() throws InterruptedException {
        try {
            WebDriverManager.chromedriver().setup();
        } catch (WebDriverManagerException e) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        }
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        driver.get("https://work.weixin.qq.com/wework_admin/frame#contacts");
        Thread.sleep(15000);
        driver.findElement(By.xpath("//a[text()=\"尹侦测试账号\"]")).click();
        driver.findElement(By.xpath("//a[text()=\"尹侦测试账号\"]/span")).click();
    }
}
