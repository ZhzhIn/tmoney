package test_framwork.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test_api_framework.ApiTestCaseModel;
import test_api_framework.BaseApi;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ApiTestCaseModelTest {

    private static BaseApi baseApi;
    private static ApiTestCaseModel apiTestCase;

    @BeforeAll
    static void beforeAll() throws IOException {
        baseApi = new BaseApi();
        baseApi.load("src/main/resources/test_framework_service/api");
        apiTestCase = ApiTestCaseModel.load("src/main/resources/test_framework_service/test_add.yaml");

    }

    @Test
    void load() throws IOException {
        assertThat(apiTestCase.name, equalTo("add"));
    }

    @Test
    void run() {
        apiTestCase.run(baseApi);
    }
}