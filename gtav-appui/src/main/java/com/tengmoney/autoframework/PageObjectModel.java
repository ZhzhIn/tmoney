package com.tengmoney.autoframework;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

/**
 * 单个页面的操作定义
 */
@Slf4j
@Data
public class PageObjectModel {
    /**
     * pageName :
     * e.g loginPage
     */
    public String name;
    /**
     * action ,<using ,value>
     * element = findElemnt(String using ,String value)
     * element.action();
     * 具体执行操作的定义不能写在这里，不然工厂模式没有意义
     */
    public HashMap<String, List<HashMap<String, Object>>> methods;

}
