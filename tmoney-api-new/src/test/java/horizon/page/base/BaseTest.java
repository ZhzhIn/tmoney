package horizon.page.base;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import po.PageObjectModel;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

/**
 * @ClassName: TestLogin
 * @Description: testcase of login
 * @Author: zhzh.yin
 * @Date: 2020-04-22 20:51
 * @Verion: 1.0
 */
public class BaseTest {
    private BaseAPI api = BaseAPI.getInstance();
    @Test
    @Description("登录成功")
    public void testLoginSuccess()   {
        HashMap map = PageObjectModel.parseParam(BaseAPI.class);
        api.login(map)
                .then()
                .body("ret", equalTo(0));
    }

    @Test
    @Description("登录失败")
    public void testLoginFailure()   {
        HashMap map = PageObjectModel.parseParam(BaseAPI.class);
        api.login(map)
                .then()
                .body("ret", not(0));
    }



}
