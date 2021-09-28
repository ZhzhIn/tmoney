package com.webframework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tengmoney.gui.WebPage;
import com.tmoney.foundation.utils.FileChangeTime;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.WebDriverManagerException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 〈manage 首页〉
 *
 * @author zhzh.yin
 * @create 2021/3/31
 */
@Slf4j
public class ManagePage extends WebPage {
    static String AUTH_LOGIN_URL = "https://test.tengmoney.com/caizhi_web_manage/index/ty/auth.do?userId=YinZhenZhi&corpId=ww8c83d949a80b562d";
    static String OP_URL = "https://test.tengmoney.com/caizhi_web_manage/#";
    @FindBy(xpath = "//span[text()=\"内容管理\"]")
    WebElement contentManage;
    @FindBy(xpath = "//span[text()=\"所有产品\"]")
    WebElement allProduct;

    public ManagePage() {
        super();
        log.info("创建MainPage");
        initDriver();
        this.beforeAll();
        driver.manage().window().maximize();
        PageFactory.initElements(driver, this);
    }

    private void initDriver() {
        try {
            WebDriverManager.chromedriver().setup();
        } catch (WebDriverManagerException e) {
            log.info("连接不上webDrvierManager");
            System.setProperty("webdriver.chrome.driver", "src/main/resources/browserDriver/chromedriver.exe");
        }
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);

    }

    private void login() {
        driver.get(AUTH_LOGIN_URL);
        Set<Cookie> cookies = driver.manage().getCookies();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File file = new File("cookies.yaml");
        try {
            mapper.writeValue(file, cookies);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        waitSecond(5);
        driver.get(OP_URL);
    }

    /**
     * 登录工作
     * 如果本地没有存cookie,就调用登录；如果存了cookie，但是时效过期，就调用登录；
     * 否则直接读取cookie文件
     */
    private void beforeAll() {
        File file = new File("cookies.yaml");
        if (!file.exists()) {
            login();
        } else {
            long changeTime = FileChangeTime.getChangeTime(file);
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

    public AllProductPage jumpToAllProductPage() {
        click(contentManage);
        click(allProduct);
        return new AllProductPage(driver);
    }
}
