package com.appframework.autocase;

import com.appframework.page.Wework;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Slf4j
public class H5PageTest {
    private static Wework wework= new Wework();

    @BeforeTest
      void beforeAll() {
    }
    @Test
    public void screenshotStation() throws InterruptedException {
        wework.jumpToH5Page().clickStation();
        Thread.sleep(3000);
        wework.screenshot(Thread.currentThread().getStackTrace()[1].getMethodName(),
                Thread.currentThread().getContextClassLoader().getResource(".").getPath()+"\\resultPic\\");
    }
    @AfterTest
      void afterAll(){
        wework.quit();
    }
}