package poexception;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static poexception.ErrorCodeConstant.JSON_NOT_FOUND_MSG;

/**
 * tmoney
 * 2020/5/8 18:06
 *
 * @author zhzh.yin
 **/
@Slf4j
@Data
public class JsonNotFoundException extends BaseException {
    public JsonNotFoundException() {
        super(JSON_NOT_FOUND_MSG);
    }
    public JsonNotFoundException(String msg) {
        super(msg);
    }
}
