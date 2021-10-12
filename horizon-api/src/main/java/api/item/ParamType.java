package api.item;/**
 * @author zhzh.yin
 * @create 2020-08-05 16:10
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 断言的参数类型
 *
 * @author zhzh.yin
 * @create 2020/8/5
 */

public enum ParamType {
    /**
     * 不同类型的参数校验
     */
    BODY("body"),
    PATH("path");
    private String param;
    ParamType(String param){
        this.param=param;
    }
    @JsonCreator
    public static ParamType fromString(String param){
        return param ==null
                ? null
                : ParamType.valueOf(param.toUpperCase());
    }
    @JsonValue
    public String getParamType() {
        return param;
    }
}
