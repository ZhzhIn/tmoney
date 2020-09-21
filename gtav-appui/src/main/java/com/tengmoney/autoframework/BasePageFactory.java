package com.tengmoney.autoframework;


import test_app_framework.AppBasePage;
import test_web_framework.WebBasePage;

/**
 * 根据平台的不同，创建不同的page
 *
 */
public class BasePageFactory {
    private static final String WEB = "WEB";
    private static final String APP = "APP";
    public static BasePage create(String driverName){
        if(driverName.toUpperCase().equals(WEB)){
            return new WebBasePage();
        }
        if(driverName.toUpperCase().equals(APP)){
            return new AppBasePage();
        }

        if(driverName.equals("uiautomator")){
//            return new AppBasePage();
        }

        if(driverName.equals("atx")){
//            return new AppBasePage();
        }

        return null;
    }
}
