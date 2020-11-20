package com.webframework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
@Slf4j
public class MainPage extends WebPage {
    static String AUTH_LOGIN_URL = "https://test.tengmoney.com/caizhi_mkto/index/ty/auth.do?userId=YinZhenZhi&corpId=ww8c83d949a80b562d";
    static String OP_URL = "https://test.tengmoney.com/caizhi_op/#/";

    @FindBy(xpath = "//span[text()=\"文章管理\"]")
    private By 文章管理;
    @FindBy(xpath = "//span[text()=\"每日早报\"]")
    private By 每日早报;

    public MainPage() {
//        super();
        log.info("创建MainPage");
        WebDriverManager.chromedriver().setup();
        driver=new ChromeDriver();
        wait=new WebDriverWait(driver, 10);
        this.beforeAll();
        driver.manage().window().maximize();
    }

    void login() {
        driver.get(AUTH_LOGIN_URL);
        Set<Cookie> cookies = driver.manage().getCookies();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("cookies.yaml");
        try {
            mapper.writeValue(file, cookies);
        } catch (IOException e) {
            e.printStackTrace();
        }
        waitSecond(5);
        driver.get(OP_URL);
//        System.exit(0);
    }

    void beforeAll() {
        File file = new File("cookies.yaml");
        if (!file.exists()) {
            login();
        } else {
            BasicFileAttributes bAttributes = null;
            try {
                bAttributes = Files.readAttributes(file.toPath(),
                        BasicFileAttributes.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long changeTime = bAttributes.lastModifiedTime().toMillis();

            if (System.currentTimeMillis() - changeTime > 1000 * 60 * 5) {
                login();
            } else {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                TypeReference typeReference = new TypeReference<List<HashMap<String, Object>>>() {
                };
                driver.get(OP_URL);
                List<HashMap<String, Object>> cookies = null;
                try {
                    cookies = (List<HashMap<String, Object>>) mapper.readValue(file, typeReference);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cookies.forEach(cookieMap -> {
                    driver.manage().addCookie(new Cookie(cookieMap.get("name").toString(), cookieMap.get("value").toString()));
                });
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                driver.get(OP_URL);
            }
        }
    }

    public MorPaperPage jumpToMorPaper() {
//todo 替换成findby，并且不被反复的创建新窗口
        //todo 修改每日早报无法点击的问题
//        click(文章管理);
        click(By.xpath("//span[text()=\"文章管理\"]"));
//        click(每日早报);
        click(By.xpath("//span[text()=\"每日早报\"]"));
        return new MorPaperPage(driver);
    }
}
