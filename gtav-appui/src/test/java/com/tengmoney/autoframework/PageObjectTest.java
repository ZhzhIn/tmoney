package com.tengmoney.autoframework;/**
 * @author zhzh.yin
 * @create 2020-09-22 14:31
 */

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * 〈pageobject test〉
 *
 * @author zhzh.yin
 * @create 2020/9/22
 */
public class PageObjectTest {
    PageHandler page=  PageHandlerFactory
            .create("web","chrome");

    @Test
    public void test2() {
        System.out.println(page.getClass().getCanonicalName());
        //父类引用子类对象，
        PageObjectModel pom = page.loadPage("src/test/resources/test_framework/contact_page.yaml");
        System.out.println(pom.toString());
        UITestCase testcase = page.load("src/main/resources/test_framework/webauto_1.yaml");
        page.run(testcase);
        page.quit();
    }

    @Test
    public void test3(){
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.baidu.com");
    }
}
