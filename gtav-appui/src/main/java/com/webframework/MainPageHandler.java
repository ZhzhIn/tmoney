package com.webframework;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;

public class MainPageHandler extends WebPageHandler {

    public MainPageHandler(String platform) {
        super(platform);
//        System.setProperty("webdriver.gecko.driver", "/Users/seveniruby/ke/java_3/selenium/drivers/geckodriver");

        String url = "https://test.xx.com/login";
//        FirefoxDriver driver=new FirefoxDriver();
        driver.get(url);
        driver.manage().deleteAllCookies();

        //todo: 改成从文件读取

        //todo: 使用自己的cookie
        driver.manage().addCookie(new Cookie("H5xxxx", "7369917120"));
        driver.manage().addCookie(new Cookie("H5xxxx", "7369917120"));

        System.out.println(driver.manage().getCookies());
        driver.get(url);

    }

    public ContactPageHandler toContact() {
        //todo:
        click(By.cssSelector("#menu_contacts"));
//        driver.findElement(By.cssSelector("#menu_contacts")).click();
        return new ContactPageHandler(driver);
    }

}
