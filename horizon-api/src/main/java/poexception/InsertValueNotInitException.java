package poexception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * tmoney
 * 2020/7/25 18:34
 * 插入的数据没有设置
 *
 * @author zhzh.yin
 **/
@Data
@Slf4j
public class InsertValueNotInitException extends Exception {
        public InsertValueNotInitException(String message) {
        }

}
