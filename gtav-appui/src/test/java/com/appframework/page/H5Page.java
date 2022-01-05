package com.appframework.page;
import com.tengmoney.gui.AppPage;
import io.appium.java_client.AppiumDriver;
import lombok.extern.slf4j.Slf4j;

/**
 * 〈消息tab〉
 * @author zhzh.yin
 * @create 2021/1/26
 */
@Slf4j
public class H5Page extends AppPage {
    private final String stationName = "云工作室";


    public H5Page(AppiumDriver driver) {
        super(driver);
        log.info("H5page init with driver ");
    }

    //进入 工作台H5
    public H5Page clickStation( ) {
/*        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        click(byText(stationName));
        return this;
    }

}