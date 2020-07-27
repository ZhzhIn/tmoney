package api.item;/**
 * @author zhzh.yin
 * @create 2020-08-05 13:37
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 〈ApiType〉
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */
public enum AppType {
    /**
     * 接口根据不同的传参类型，读取不同的yaml中的数据
     * 包括但不限于在 请求参数中的小程序/自建应用的appId,agentId等
     * 登录时auth.do使用哪个登录链接
     * MINIPRO 小程序
     * H5STATION 小站
     * H5PRODUCT 推产品
     * MANAGE 管理平台
     * MARKET 运营平台
     */
    MINIPRO("minipro"),
    H5STATION("h5station"),
    H5PRODUCT("h5product"),
    MANAGE("manage"),
    MARKET("market");
    private final String apiType;
    AppType(String apiType){
        this.apiType=apiType;
    }
    @JsonCreator
    public static AppType fromString(String apiType){
        return apiType ==null
                ? null
                : AppType.valueOf(apiType.toUpperCase());
    }
    @JsonValue
    public String getEnv() {
        return apiType;
    }
}
