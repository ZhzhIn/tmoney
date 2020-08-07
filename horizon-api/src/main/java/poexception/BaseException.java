package poexception;

/**
 * 〈基础异常类，所有异常类需要继承它〉
 * Exception ：编译受检查的异常，这种异常是强制我们catch或throw的异常。你遇到这种异常必须进行catch或throw，如果不处理，编译器会报错
 * runTimeException 运行时异常，这种异常我们不需要处理，完全由虚拟机接管。
 * @author zhzh.yin
 * @create 2020/8/14
 */
public class BaseException extends RuntimeException{
    private static final String DEFAULT_MESSAGE="当前代码有错误";
    private static final long serialVersionUID=1L;
    public BaseException(){
        super(DEFAULT_MESSAGE);
    }
    public BaseException(String msg){
        super(msg);
    }
    public BaseException(Throwable throwable) {
        super(throwable);
    }

    public BaseException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
