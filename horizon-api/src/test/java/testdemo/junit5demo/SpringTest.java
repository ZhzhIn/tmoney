package testdemo.junit5demo;

import org.junit.Test;

import static io.restassured.RestAssured.given;

/**
 * 〈接口测试覆盖率demo测试类〉
 *
 * @author zhzh.yin
 * @create 2020/12/25
 */
public class SpringTest {
    @Test
    public void est(){
        String url = "http://localhost:8080/greeting?name=User";
        given().when().get(url).then().log().all();
    }
}
