package poexception;
import static poexception.ErrorCodeConstant.YAML_NOT_FOUND_MSG;

/**
 * tmoney
 * 2020/5/8 18:06
 *
 * @author zhzh.yin
 **/
public class TestCaseNeedToEditException extends BaseException {
    public TestCaseNeedToEditException() {
        super(YAML_NOT_FOUND_MSG);
    }
    public TestCaseNeedToEditException(String msg) {
        super(msg);
    }
}
