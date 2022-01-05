package com.appframework.autocase;

import com.appframework.page.KingOfGloryHome;
import org.junit.Test;

/**
 * 〈〉
 *
 * @author zhzh.yin
 * @create 2021/9/28
 */
public class GloryTest {
    @Test
    public void test(){
        KingOfGloryHome homePage = new KingOfGloryHome();
        homePage.jumpToMe().receiveBenefits();
    }
}
