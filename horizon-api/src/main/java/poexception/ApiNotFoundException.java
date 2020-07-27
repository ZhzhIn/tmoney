package poexception;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * tmoney
 * 2020/5/8 18:06
 *
 * @author zhzh.yin
 **/
@Slf4j
@Data
public class ApiNotFoundException extends Exception {
    public ApiNotFoundException(String message) {
    }

}
