package poexception;

import static poexception.ErrorCodeConstant.YAML_NEED_TO_EDIT;

/**
 * 〈yaml文件格式不符合要求〉
 *
 * @author zhzh.yin
 * @create 2020/8/14
 */
public class YamlNeedToEdit extends BaseException{
    public YamlNeedToEdit() {
        super(YAML_NEED_TO_EDIT);
    }
    public YamlNeedToEdit(String msg) {
        super(msg);
    }
}
