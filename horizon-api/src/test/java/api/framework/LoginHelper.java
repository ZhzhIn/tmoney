package api.framework;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.parallel.Execution;
import poexception.ApiNotFoundException;

import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

@Execution(CONCURRENT)  //CONCURRENT表示支持多线程
@Slf4j
@Feature("登录")
@Owner("zhzh.yin")
public class LoginHelper {
    private static ApiListModel model;

    static {
        try {
           model= ApiListModel.load("loginTest");
        } catch (ApiNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void login() {
        Api api = model.get("login");
        api=api.loadParamFromDefaultConfig();
        api.run();
    }

}
