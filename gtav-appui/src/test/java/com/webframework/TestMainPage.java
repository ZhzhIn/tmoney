package com.webframework;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

public class TestMainPage {
//     InitPage page=new InitPage();
    MainPage page ;
    @Test
     void test() throws InterruptedException {
        page = new MainPage();
//        Assert.assertTrue(page.jumpToMorPaper().isMorPaperPage());
        MorPaperPage page1 = page.jumpToMorPaper();
        Thread.sleep(5000);

        String str = "//div[contains(text(),'每日早报')]";
        page1.driver.findElement(By.xpath(str));

//        page.mainPage.jumpToMorPaper();
//        Assert.assertTrue(page.mainPage.jumpToMorPaper().isMorPaperPage());
    }
/*    @AfterAll
    static void shutdown(){
        page.getMainPage().quit();
    }*/
}
