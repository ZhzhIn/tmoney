package com.tengmoney.autoframework;


import test_app_framework.AppPageHandler;
import test_web_framework.WebPageHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static io.appium.java_client.remote.MobileCapabilityType.APP;

/**
 * 根据平台的不同，创建不同的page
 */
public class PageHandlerFactory {
    private static final String WEB = "web";

    public static <T extends PageHandler> T create(String driverName, String platform) {
        PageHandler page = null;
        String[] params = new String[]{platform};
        try {
            if (driverName.toLowerCase().contains(WEB)) {
                Class clazz = WebPageHandler.class;
                Constructor c = null;
                c = clazz.getConstructor(String.class);
                page = (T) c.newInstance(platform);
            }else if(driverName.toLowerCase().contains(APP)){
                Class clazz = AppPageHandler.class;
                Constructor c = null;
                c = clazz.getConstructor(String.class);
                page = (T) c.newInstance(platform);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return (T) page;
    }
}
