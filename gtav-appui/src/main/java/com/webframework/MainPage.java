package com.webframework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
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
    private By newsManage;
    @FindBy(xpath = "//span[text()=\"每日早报\"]")
    private By morningPaper;

    public MainPage() {
        log.info("创建MainPage");
        initDriver();
        PageFactory.initElements(driver,this);
        this.beforeAll();
        driver.manage().window().maximize();

    }
    private void initDriver(){
        try {
            WebDriverManager.chromedriver().setup();
        } catch (WebDriverManagerException e) {
            log.info("连接不上webDrvierManager");
            System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        }
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

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
    }

    /**
     * 登录工作
     * 如果本地没有存cookie,就调用登录；如果存了cookie，但是时效过期，就调用登录；
     * 否则直接读取cookie文件
     */
    void beforeAll() {
        File file = new File("cookies.yaml");
        if (!file.exists()) {
            login();
        } else {
            long changeTime = getChangeTime(file);
            if (System.currentTimeMillis() - changeTime > 1000 * 60 * 5) {
                login();
            } else {
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                TypeReference<List<HashMap<String, Object>>> typeReference = new TypeReference<List<HashMap<String, Object>>>() {
                };
                driver.get(OP_URL);
                List<HashMap<String, Object>> cookies = null;
                try {
                    cookies = mapper.readValue(file, typeReference);
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

    private long getChangeTime(File file) {
        BasicFileAttributes bAttributes = null;
        try {
            bAttributes = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long changeTime = bAttributes.lastModifiedTime().toMillis();
        return changeTime;
    }

    public MorPaperPage jumpToMorPaper() {

        if (newsManage == null) {
            log.error("没找到文章管理,强制退出");
            System.exit(0);
        } else {
            log.info("没啥问题");
        }
        click(newsManage);
//        click(By.xpath("//span[text()=\"文章管理\"]"));
        click(morningPaper);
//        click(By.xpath("//span[text()=\"每日早报\"]"));
        return new MorPaperPage(driver);
    }
}
