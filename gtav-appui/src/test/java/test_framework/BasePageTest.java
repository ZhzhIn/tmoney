package test_framework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tengmoney.autoframework.BasePage;
import com.tengmoney.autoframework.UITestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test_web_framework.WebBasePage;

class BasePageTest {

    private static BasePage basePage;

    @BeforeAll
    static void beforeAll(){
        basePage = new WebBasePage();
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void run() {
        UITestCase uiauto=basePage.load("/test_framework/uiauto.yaml");
        basePage.run(uiauto);
    }

    @Test
    void runPOM(){
        basePage.loadPages("src/main/resources/test_framework");
        UITestCase uiauto=basePage.load("/test_framework/webauto_3.yaml");
        basePage.run(uiauto);

    }

    @Test
    void load() throws JsonProcessingException {
        UITestCase uiauto=basePage.load("/test_framework/uiauto.yaml");
        ObjectMapper mapper=new ObjectMapper();
        System.out.println(mapper.writeValueAsString(uiauto));
    }
}