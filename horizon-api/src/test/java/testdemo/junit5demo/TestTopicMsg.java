package testdemo.junit5demo;/**
 * @author zhzh.yin
 * @create 2020-10-13 16:10
 */

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    String TEST_URL = "https://test.tengmoney.com/caizhi_mkto/api/fund/sop/type/save";
    @Test
    void test1() throws ParseException {
        System.out.println(String.valueOf(randomTimeStamp()));
    }
    @RepeatedTest(93)
    void test() throws ParseException {
        String nowtime = randomTimeStamp()+"";
        Cookies cookies =given().get("https://test.tengmoney.com/caizhi_mkto/index/ty/auth.do?userId=YinZhenZhi&corpId=ww8c83d949a80b562d")
                .getDetailedCookies();
        System.out.println(cookies);
        Map<String,String> map = new HashMap();
        map.put("typeName",String.valueOf(randomTimeStamp()).substring(9,13));
        map.put("typeDesc","Desc");
        map.put("imgId","1");
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(map)
                .when()
                .post(TEST_URL)
                .then()
                .statusCode(200)
                .log().all();
    }
}
