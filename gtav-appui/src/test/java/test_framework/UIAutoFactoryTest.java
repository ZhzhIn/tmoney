package test_framework;

import com.tengmoney.autoframework.BasePage;
import com.tengmoney.autoframework.UIAuto;
import org.junit.jupiter.api.Test;

class UIAutoFactoryTest {

    @Test
    void create() {
        BasePage web= UIAutoFactory.create("web");
        UIAuto uiAuto=web.load("/test_framework/webauto.yaml");
        web.run(uiAuto);
    }
}