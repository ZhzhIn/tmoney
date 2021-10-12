package poexception;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈错误码和对应的提示语〉
 *  由对应的Exception的构造函数调用
 * @author zhzh.yin
 * @create 2020/8/14
 */
public class ErrorCodeConstant {
    public static Map<Integer,String> codeMap = new HashMap<>();
    public static final int YAML_NOT_FOUND_CODE = 1001;
    public static final String YAML_NOT_FOUND_MSG = "yaml not found.";
    public static final int JSON_NOT_FOUND_CODE = 1002;
    public static final String JSON_NOT_FOUND_MSG = "json not found.";
    public static final String CONFIG_NOT_FOUND_MSG = "config not found.";
    public static final String YAML_NEED_TO_EDIT = "yaml内容编辑不符合要求，文件内容需要修改.";
    public static final String TESTCASE_NEED_TO_EDIT = "testcase内容编辑不符合要求，文件内容需要修改.";
    public static final String JSON_NEED_TO_EDIT = "json内容编辑不符合要求，文件内容需要修改.";
    static{
        codeMap.put(YAML_NOT_FOUND_CODE,YAML_NOT_FOUND_MSG);
        codeMap.put(JSON_NOT_FOUND_CODE,JSON_NOT_FOUND_MSG);

    }
}
