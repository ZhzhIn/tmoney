package com.tengmoney.autoframework;

import item.ActionType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 单个页面的操作定义
 */
public class PageMethod {
    public String name;
    /**
     * action ,<using ,value>
     * element = findElemnt(String using ,String value)
     * element.action();
     */
    public HashMap<ActionType, List<HashMap<String, Object>>> methods;
    @Test
    void test(){
        HashMap usingValue = new HashMap();
        usingValue.put("xpath","kw");
        ArrayList array = new ArrayList();
        array.add(usingValue);
        methods = new HashMap<>();
        methods.put(ActionType.CLICK,array);
        System.out.println(methods);
    }
}
