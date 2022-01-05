package com.tengmoney.item;/**
 * @author zhzh.yin
 * @create 2020-09-18 15:34
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 〈页面的操作类型，比如点击，拖拽等〉
 *
 * @author zhzh.yin
 * @create 2020/9/18
 */
public enum ActionType {
    CLICK("click");
    private String action;
    ActionType(String action){
        this.action=action;
    }
    @JsonCreator
    public static ActionType fromString(String action){
        return action ==null
                ? null
                : ActionType.valueOf(action.toUpperCase());
    }
    @JsonValue
    public String getActionType() {
        return action;
    }
}
