package test_framwork.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test_api_framework.BaseApi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

class BaseApiTest {
    private static BaseApi baseApi;

    @BeforeAll
    static void beforeAll(){
        baseApi=new BaseApi();
        baseApi.load("src/main/resources/test_framework_service/api");
    }

    @Test
    void load() {
        assertThat(baseApi.apis.size(), greaterThan(1));
    }

    @Test
    void run() {
        baseApi.run("test_web/wework", "get_token");
        baseApi.run("tags", "list");
    }
}