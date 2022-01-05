package com.tengmoney.item;/**
 * @author zhzh.yin
 * @create 2020-09-18 15:35
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 〈元素定位方法，比如xpath,id,cssselector等〉
 *
 * @author zhzh.yin
 * @create 2020/9/18
 */
public enum FocusType {
    XPATH("xpath");
    private String focus;
    FocusType(String action){
        this.focus=focus;
    }
    @JsonCreator
    public static FocusType fromString(String focus){
        return focus ==null
                ? null
                : FocusType.valueOf(focus.toUpperCase());
    }
    @JsonValue
    public String getFocusType() {
        return focus;
    }
}
