package com.tengmoney.autoframework;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 用例操作
 */
public class UITestCase {
    public String name="";
    public String description="";
    public List<HashMap<String, Object>> steps;
    public static UITestCase load(String path) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        UITestCase uiTestcase = null;
        try {
            uiTestcase = mapper.readValue(
                    UITestCase.class.getResourceAsStream(path),
//                    Thread.currentThread().getStackTrace()[2];
                    UITestCase.class
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uiTestcase;
    }
}
