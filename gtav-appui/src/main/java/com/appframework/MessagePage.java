package com.appframework;/**
 * @author zhzh.yin
 * @create 2021-01-26 9:40
 */

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;

/**
 * 〈消息tab〉
 *
 * @author zhzh.yin
 * @create 2021/1/26
 */
@Slf4j
public class MessagePage extends AppPage {

    private final By messageArea = By.id("com.tencent.wework:id/eo7");//输入框
    private final By sendIcon = By.id("com.tencent.wework:id/eo3");//发送
    private final By switchToVoiceMode = By.id("com.tencent.wework:id/dq0");//>)按钮
    private final By voiceIcon = By.id("com.tencent.wework:id/eo5");//按住说话
    private final By backIcon = By.id("com.tencent.wework:id/i63");//<-
    private final By switchToKeyBoardMode = By.id("com.tencent.wework:id/dq0");//键盘按钮
    private final By addIcon = By.id("com.tencent.wework:id/env");//+按钮
    private final By picIcon = By.xpath("//*[@text='图片']");//图片按钮
    private final By sendFileIcon = By.id("com.tencent.wework:id/i6k");//发送图片按钮
    private final String filePreName = "//android.widget.GridView/android.widget.RelativeLayout[";
    private final String fileLastName = "]/android.widget.RelativeLayout/android.widget.ImageView";
    private Boolean isVoiceMode;//是否是语音状态

    public MessagePage(AppiumDriver<MobileElement> driver) {
        super(driver);
    }

    /**
     * 选择任意对话窗口
     * @param conversationName 对话窗名称
     * @return
     */
    public MessagePage chooseConversation(String conversationName) {
        click(byText(conversationName));
        log.info("show the icon 'hold to speak' ?:" + hasElement(voiceIcon));
        if (hasElement(voiceIcon)) {
            isVoiceMode = true;
        } else {
            isVoiceMode = false;
        }
        return this;
    }

    /**
     * 发送文字消息
     * @param message
     * @return
     */
    public MessagePage sendMessage(String message) {
        log.info("voiceFlag is :" + isVoiceMode);
        if (isVoiceMode) {
            click(switchToKeyBoardMode);
            log.info("点击键盘icon");
            isVoiceMode = false;
        }
        sendKeys(messageArea, message);
        click(sendIcon);
        return this;
    }

    /**
     * 点击返回按钮
     * @return
     */
    public MessagePage clickBack() {
        click(backIcon);
        return this;
    }

    /**
     * 发送语音消息
     * @return
     */
    public MessagePage sendVoiceMessage() {
        if (!isVoiceMode) {
            click(switchToVoiceMode);
            isVoiceMode = true;
        }
        int touchtime = (int) (Math.random() * 30000);
        log.info("即将发送时长为" + touchtime + "ms的语音消息");
        longPress(voiceIcon, touchtime);
        return this;
    }

    /**
     * 发送图片
     *
     * @param num 发送第几张图片，图片角标从0开始计算
     * @return
     */
    public MessagePage sendPicMessage(int num) {
        click(addIcon);
        click(picIcon);
        String xpathString = filePreName + String.valueOf(num + 2) + fileLastName;
        log.info("看看xpath：" + xpathString);
        click(By.xpath(xpathString));
        click(sendFileIcon);
        return this;
    }

    /**
     * 发送名片
     *
     * @param num
     * @return
     */
    public MessagePage sendBusinessCard(int num) {
        click(addIcon);
        /*click(picIcon);
        log.info("看看xpath：" + xpathString);
        click(By.xpath(filePreName + num + fileLastName));
        click(sendFileIcon);*/
        return this;
    }
}