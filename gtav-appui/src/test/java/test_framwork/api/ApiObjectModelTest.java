package test_framwork.api;

//import io.restassured.response.Response;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test_api_framework.ApiObjectModel;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ApiObjectModelTest {

    private static ApiObjectModel api;

    @BeforeAll
    static void beforeAll() throws IOException {
        api = ApiObjectModel.load("src/main/resources/test_framework_service/api/wework_api.yaml");
    }

    @Test
    void load() {
        assertThat(api.name, equalTo("test_web/wework"));
    }

    @Test
    void run() {
        Response response = api.methods.get("get_token").run();
        response.then().statusCode(200);
    }
}