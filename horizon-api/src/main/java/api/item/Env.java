package api.item;/**
 * @author zhzh.yin
 * @create 2020-08-05 16:10
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 〈环境〉
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */

public enum Env {
    /**
     * 不同的环境
     * 根据环境设置文件application.yaml中的current的值读取不同的参数
     * TEST 测试环境
     * DEV 开发环境
     * UAT testserver2
     * PROD 生产环境
     */
    TEST("test"),
    DEV("dev"),
    PROD("prod"),
    UAT("uat");
    private String env;
    Env(String env){
        this.env=env;
    }
    @JsonCreator
    public static Env fromString(String env){
        return env ==null
                ? null
                : Env.valueOf(env.toUpperCase());
    }
    @JsonValue
    public String getEnv() {
        return env;
    }
}
