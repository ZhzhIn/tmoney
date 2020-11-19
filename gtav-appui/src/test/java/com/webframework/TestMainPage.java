package com.webframework;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class TestMainPage {
//     InitPage page=new InitPage();
    MainPage page ;
    @Test
     void test(){
        page = new MainPage();
//        PageFactory.initElements(page.driver,MainPage.class);
        Assert.assertTrue(page.jumpToMorPaper().isMorPaperPage());

//        page.mainPage.jumpToMorPaper();
//        Assert.assertTrue(page.mainPage.jumpToMorPaper().isMorPaperPage());
    }
/*    @AfterAll
    static void shutdown(){
        page.getMainPage().quit();
    }*/
}
