package api.framework;

import api.item.AppType;
import org.junit.Test;

public class LoginHelper {
    private static ApiModel model = ApiModel.load("src/test/resources/miniapi/loginhelper.yaml");
    @Test
    public void test(){
        login(AppType.MINIPRO);
    }
    public static void login(){
        model.get("minipro").importDefaultConfig().run();
    }
    public static void login(AppType type){
        switch (type){
            case MANAGE:
                model.get("api.framework.manage").importDefaultConfig().run();
                break;
            case MARKET:
                model.get("market").importDefaultConfig().run();
                break;
            case MINIPRO:
                model.get("minipro").importDefaultConfig().run();
                break;
            default:
                model.get("h5").importDefaultConfig().run();
        }
    }
}