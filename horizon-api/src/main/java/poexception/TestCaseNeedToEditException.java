package poexception;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static poexception.ErrorCodeConstant.YAML_NOT_FOUND_MSG;

/**
 * tmoney
 * 2020/5/8 18:06
 *
 * @author zhzh.yin
 **/
@Slf4j
@Data
public class TestCaseNeedToEditException extends BaseException {
    public TestCaseNeedToEditException() {
        super(YAML_NOT_FOUND_MSG);
    }
    public TestCaseNeedToEditException(String msg) {
        super(msg);
    }
}
