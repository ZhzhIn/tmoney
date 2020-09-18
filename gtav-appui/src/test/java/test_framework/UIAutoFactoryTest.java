package test_framework;

import com.tengmoney.autoframework.BasePage;
import com.tengmoney.autoframework.UITestCase;
import org.junit.jupiter.api.Test;

class UIAutoFactoryTest {

    @Test
    void create() {
        BasePage web= UIAutoFactory.create("web");
        UITestCase uiAuto=web.load("/test_framework/webauto.yaml");
        web.run(uiAuto);
    }
}