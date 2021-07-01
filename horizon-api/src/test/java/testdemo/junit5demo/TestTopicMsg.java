package testdemo.junit5demo;/**
 * @author zhzh.yin
 * @create 2020-10-13 16:10
 */

import io.restassured.http.Cookies;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.parallel.ExecutionMode.CONCURRENT;

/**
 * 〈任务测试〉
 *
 * @author zhzh.yin
 * @create 2020/10/13
 */
@Execution(CONCURRENT)
public class TestTopicMsg {

    /**
     * 时间戳
     * @return
     * @throws ParseException
     */
    long randomTimeStamp() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String beginDate = "2020-11-30";
        String endDate = "2021-3-30";
        Date start = format.parse(beginDate);
        Date end = format.parse(endDate);
        long begin = start.getTime();
        long last = end.getTime();
        long rtn = begin + (long)(Math.random() * (last - begin));
        return rtn;
    }
    String TEST_URL = "";

    @RepeatedTest(1)
    void test() throws ParseException {
        String nowtime = randomTimeStamp()+"";
        Cookies cookies =given().get("https://test.tengmoney.com/caizhi_mkto/index/ty/auth.do?userId=YinZhenZhi&corpId=ww8c83d949a80b562d")
                .getDetailedCookies();
        System.out.println(cookies);
        given()
                .cookies(cookies)
                .body("{\"name\":\"专题消息" +
                        +randomTimeStamp()+"\",\"msgType\":10,\"relationContents\":[{\"pic\":\"\",\"textContent\":\"\",\"relatedParam\":121}]}")
                .when()
                .post(TEST_URL)
                .then()
                .statusCode(200)
                .log().all()
                .assertThat().extract()
                .path("ret").equals(0);
    }
}
