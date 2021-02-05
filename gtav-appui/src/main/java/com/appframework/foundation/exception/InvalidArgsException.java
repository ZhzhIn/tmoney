package com.appframework.foundation.exception;/**
 * @author zhzh.yin
 * @create 2021-02-04 14:35
 */

/**
 * 〈无效测试参数异常〉
 *
 * @author zhzh.yin
 * @create 2021/2/4
 */
public class InvalidArgsException extends RuntimeException
{
    private static final long serialVersionUID = 1204359727358878608L;

    public InvalidArgsException()
    {
        super("Invalid test arguments exception");
    }

    public InvalidArgsException(String msg)
    {
        super("Invalid test arguments exception: " + msg);
    }
}
