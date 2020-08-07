package poexception;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static poexception.ErrorCodeConstant.CONFIG_NOT_FOUND_MSG;

/**
 * tmoney
 * 2020/5/8 18:06
 *
 * @author zhzh.yin
 **/
@Slf4j
@Data
public class ConfigNotFoundException extends BaseException {
    public ConfigNotFoundException() {
        super(CONFIG_NOT_FOUND_MSG);
    }
    public ConfigNotFoundException(String msg) {
        super(msg);
    }
}
