package test_framework;

import com.tengmoney.autoframework.BasePage;
import test_app_framework.AppBasePage;
import test_web_framework.WebBasePage;

public class UIAutoFactory {
    public static BasePage create(String web) {
        if(web=="web"){
            return new WebBasePage();
        }
        if(web=="app"){
            return new AppBasePage();
        }
        return null;
    }
}
