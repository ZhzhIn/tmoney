package testdemo.junit5demo;/**
 * @author zhzh.yin
 * @create 2020-10-13 16:10
 */

import api.framework.LoginHelper;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class TestTask {
//    @BeforeAll
    void setUp(){
        LoginHelper.login();
    }

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
    String TEST_URL = "https://test.tengmoney.com/caizhi_miniapi/api/sop/snapshot/test";
    String dailyRandom(){
        ArrayList<String> list = new ArrayList<String>();
        list.add("EVERY_DAY");
        list.add("NO_DUPLICATION");
        list.add("PER_MONTH");
        list.add("WEEKLY");
        int random = (int)(Math.random() * (4 - 1 ) + 1);
        String get = list.get(random);
        return get;
    }
    @RepeatedTest(100)
    void test() throws ParseException {
        String nowtime = randomTimeStamp()+"";
        Cookies cookies =given().get("https://test.tengmoney.com/caizhi_miniapi/index/ty/auth.do?userId=WenJie&corpId=ww8c83d949a80b562d")
                .getDetailedCookies();
        given()
                .queryParam("corpId","ww8c83d949a80b562d")
                .cookies(cookies)
//                .cookie("JSESSIONID","E17FEC9C3A73AA2DE120AEF3716B398C")
//                .cookie("sensorsdata2015jssdkcross","%7B%22distinct_id%22%3A%224769b53af26bd544bd96d59823b091f8f17c71b8ca92a9f1af5cf6bae32782d6%22%2C%22first_id%22%3A%221764823879868-04a4111f6947af-5260792e-304704-176482387996ab%22%2C%22props%22%3A%7B%22%24latest_traffic_source_type%22%3A%22%E7%9B%B4%E6%8E%A5%E6%B5%81%E9%87%8F%22%2C%22%24latest_search_keyword%22%3A%22%E6%9C%AA%E5%8F%96%E5%88%B0%E5%80%BC_%E7%9B%B4%E6%8E%A5%E6%89%93%E5%BC%80%22%2C%22%24latest_referrer%22%3A%22%22%7D%2C%22%24device_id%22%3A%221764823879868-04a4111f6947af-5260792e-304704-176482387996ab%22%7D")
//                .cookie("caizhi_h5_key","8bd53cca439446639ae59c0b73662f88%3B25ed77300c60c1b0668bcc40d5b2bb9b")
                //=; =; =3; =
//                .contentType(JSON)
//                .body("{\"type\":0,\"content\":\"\",\"deptIdList\":[\"d1492f090d134220a1bb3a02a6463d66\"],\"category\":0,\"onTime\":\"2020-12-15 16:46:29\",\"offTime\":\"2020-12-31 16:46:34\",\"name\":\"yz测试123看视频\",\"objectId\":\"\",\"url\":\"https://fe-www-tenmoney-1301390158.cos.ap-shanghai.myqcloud.com/misc/test-demo.mp4\",\"videoCoverUrl\":\"https://testimg.tengmoney.com/company/3796c22b71e282b7e886e456f215730d/f8b6b309dbb941aeaf1328cfdff35357.png\",\"posterDiyType\":\"1\",\"codeLong\":100,\"codeWidth\":100,\"codePositionX\":0,\"codePositionY\":0,\"posterLong\":100,\"posterWidth\":100,\"classification\":\"5\",\"synchronizeToMoment\":false,\"multigraph\":false}")
                .when()
                .get(TEST_URL)
                .then()
                .statusCode(200)
                .log().all()
        ;
    }



}
