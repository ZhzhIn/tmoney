package zelda.page;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zelda.po.PageObjectModel;
import zelda.util.InitializeDriver;

/**
 * @ClassName: LoginConfig
 * @Description: login with yaml ,use userId and corpId
 * @Author: zhzh.yin
 * @Date: 2020-04-29 15:34
 * @Verion: 1.0
 */
public class AppLogin {
    static final Logger log = LoggerFactory.getLogger(AppLogin.class);

    public AppLogin loginWithCookie(String userId, String corpId) {
        log.info("执行loginWithCookie方法");
        PageObjectModel
                .parseSteps(AppLogin.class);
        return this;
    }

    public WebDriver getDriver() {
        return InitializeDriver.getDriver();
    }
}
