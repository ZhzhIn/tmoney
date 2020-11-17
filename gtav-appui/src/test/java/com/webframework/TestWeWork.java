package com.webframework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TestWeWork {

    static WebDriver driver;

    @BeforeAll
    static void setUp()  {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    void login() throws InterruptedException, IOException {
        String url = "https://work.weixin.qq.com/wework_admin/frame";
        driver.get(url);
        driver.manage().deleteAllCookies();
        Thread.sleep(15000);
        //扫码记录cookie
        Set<Cookie> cookies = driver.manage().getCookies();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(new File("cookies.yaml"), cookies);
    }

    @Test
    void loginWithCookie() throws InterruptedException, IOException {
        WebDriverManager.chromedriver().setup();
        driver.get("https://work.weixin.qq.com/wework_admin/frame");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        TypeReference<List<HashMap<String, Object>>> typeReference = new TypeReference<List<HashMap<String, Object>>>() {
        };
        List<HashMap<String, Object>> cookies = mapper.readValue(new File("cookies.yaml"), typeReference);
        cookies.forEach(cookieMap -> {
            driver.manage().addCookie(new Cookie(cookieMap.get("name").toString(), cookieMap.get("value").toString()));
        });
        Thread.sleep(5000);
        //刷新界面
        driver.navigate().refresh();
        Thread.sleep(5000);
    }

    @AfterAll
    static void shutDown() {
        driver.quit();
    }
}
