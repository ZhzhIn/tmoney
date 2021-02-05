package com.tmoney.foundation.utils;
/**
 * 〈字段判断〉
 *
 * @author zhzh.yin
 * @create 2021/2/4
 */
public class DefaultEnvArgResolver implements IEnvArgResolver{
    @Override
    public String get(String env, String key) {
        if(Configuration.isNull(Configuration.Parameter.ENV))
        {
            throw new RuntimeException("Configuration parameter 'env' should be set!");
        }
        return R.CONFIG.get(Configuration.get(Configuration.Parameter.ENV) + "." + key);
    }
}
