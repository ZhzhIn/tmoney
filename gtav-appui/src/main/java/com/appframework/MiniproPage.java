package com.appframework;/**
 * @author zhzh.yin
 * @create 2021-03-16 15:14
 */
import com.tengmoney.autoframework.DriverFactory;
import lombok.extern.slf4j.Slf4j;
/**
 * 〈小程序页面〉
 *
 * @author zhzh.yin
 * @create 2021/3/16
 */
@Slf4j
public class MiniproPage extends Wework {
    public MiniproPage() {
        super();
        driver = DriverFactory.create("MINIPRO");
        log.info("miniproPage init");
    }

}