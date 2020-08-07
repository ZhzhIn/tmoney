package poexception;
/**
 * 〈业务类的异常〉
 * 给一个返回码和返回信息，便于排查
 * @author zhzh.yin
 * @create 2020/8/14
 */
public class BusinessException extends  BaseException {

    private  int  code;
    private  String    errorMsg;

    public BusinessException(int code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public BusinessException(int code) {
        this.code = code;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
