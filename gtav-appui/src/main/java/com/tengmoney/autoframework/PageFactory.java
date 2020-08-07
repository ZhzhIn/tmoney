package com.tengmoney.autoframework;

import test_app_framework.AppBasePage;

public class PageFactory {
    public static BasePage create(String driverName){
        if(driverName=="web" || driverName=="selenium"){
//            return new WebBasePage();
        }

        if(driverName=="app" || driverName=="appium"){
            return new AppBasePage("com.tencent.wework",".launch.LaunchSplashActivity");
        }

        if(driverName=="uiautomator"){
//            return new AppBasePage();
        }

        if(driverName=="atx"){
//            return new AppBasePage();
        }

        if(driverName=="macaca"){
//            return new AppBasePage();
        }
        return null;
    }
}
