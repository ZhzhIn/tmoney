package com.appframework;

import org.junit.Test;

/**
 * 〈〉
 *
 * @author zhzh.yin
 * @create 2021/9/28
 */
public class GloryTEst {
    @Test
    public void test(){
        KingOfGloryHome homePage = new KingOfGloryHome();
        homePage.jumpToMe().receiveBenefits();
    }
}
