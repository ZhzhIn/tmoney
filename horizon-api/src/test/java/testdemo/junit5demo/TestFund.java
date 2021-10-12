package testdemo.junit5demo;/**
 * @author zhzh.yin
 * @create 2020-10-13 16:10
 */

import org.junit.Assert;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.parallel.ExecutionMode.SAME_THREAD;

/**
 * 主推设置
 *
 * @author zhzh.yin
 * @create 2020/10/13
 */
//@Execution(CONCURRENT)
@Execution(SAME_THREAD)
public class TestFund {
    String cookieStr =
            "experimentation_subject_id=ImFkZTYyNDAwLWNkMDYtNGM0ZC1hM2RmLWMzMzc4YWUwZjViMiI%3D--9be7d06e86d235e224c2d6b24c17accd45ac76db; morning_paper_cookie=ed6db510a3b04246a33e53222bb7db71; caizhi_web_key=d59691bbbcfb4fea94d7bda244ab997f; current_corp_id=ww8c83d949a80b562d; Hm_lvt_350b050ff492de34e38e039a26a0a33c=1623394322,1623403554,1623463920,1623463935; Hm_lpvt_350b050ff492de34e38e039a26a0a33c=1623463935";

    String TEST_URL = "";

    static Stream<String> stringSFx3() {
        //'011605',
        //'002953',
        //'011679'
        return Stream.of(
                "46e857e8d7ae4c47900d308bdfd29421",
                "8599d84fd46d464abefe08befb28e4c2",
                "e3748886ea7c4097911982837c0bb56f");
    }

    static Stream<String> stringSF_HBx2() {
        return Stream.of(
                "d52f20e974ef47179c2a11eb636c4b54",
                "f48ac104c86940378541add33c867cd5",
                "2fab8f0d48b14825b2592aaf55b460cd");
    }

    static Stream<String> stringOtherx3() {
        return Stream.of(
                "078d76a927184425a05275b64395fbf2",
                "1daecd83f96942bfb08e536945b5f69d",
                "0722f653cb554287a21ebe90bbf79720");
    }

    @ParameterizedTest(name = "开启3个首发")
    @MethodSource("stringSFx3")
    void setUp3SF(String fid) {
        Assert.assertTrue(setUpPush(fid));
    }

    @ParameterizedTest(name = "取消3个首发")
    @MethodSource("stringSFx3")
    void setOff3SF(String fid) {
        setOffPush(fid);
    }

    @ParameterizedTest(name = "开启1首发2七日年化")
    @MethodSource("stringSF_HBx2")
    void setUpSF_HBx2(String fid) {
        Assert.assertTrue(setUpPush(fid));
    }

    @ParameterizedTest(name = "取消1首发2七日年化")
    @MethodSource("stringSF_HBx2")
    void setOffSF_HBx2(String fid) {
        setOffPush(fid);
    }

    @ParameterizedTest(name = "开启近1年+成立以来+没有数据")
    @MethodSource("stringOtherx3")
    void setUpOtherX3(String fid) {
        Assert.assertTrue(setUpPush(fid));
    }

    @ParameterizedTest(name = "取消近1年+成立以来+没有数据")
    @MethodSource("stringOtherx3")
    void setOffOtherX3(String fid) {
        setOffPush(fid);
    }

    boolean setUpPush(String fid) {
        return given()
                .queryParam("productId", fid)
                .queryParam("startDate", "2021-06-10")
                .queryParam("endDate", "2021-06-16")
                .queryParam("isPush", 1)
                .cookie(cookieStr)
                .when()
                .get(TEST_URL)
                .then()
                .log().all()
                .assertThat().extract()
                .path("ret").equals(0);
    }

    @ParameterizedTest
    boolean setOffPush(String fid) {
        return given()
                .queryParam("productId", fid)
                .queryParam("startDate", "2021-06-10")
                .queryParam("endDate", "2021-06-16")
                .queryParam("isPush", 0)
                .cookie(cookieStr)
                .when()
                .get(TEST_URL)
                .then()
                .log().all()
                .assertThat().extract()
                .path("ret").equals(0);
    }


}
