package com.appframework.foundation.exception;
/**
 * 〈不支持的操作〉
 *
 * @author zhzh.yin
 * @create 2021/2/4
 */
public class NotSupportedOperationException extends RuntimeException
{
    private static final long serialVersionUID = 5178910748578834239L;

    public NotSupportedOperationException()
    {
        super("Not supported operation!");
    }

    public NotSupportedOperationException(String msg)
    {
        super("Not supported operation: " + msg + "!");
    }
}
