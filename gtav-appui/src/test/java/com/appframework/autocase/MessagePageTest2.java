package com.appframework.autocase;

import com.appframework.page.MessagePage;
import com.appframework.page.Wework;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

@Slf4j
public
class MessagePageTest2 {
    private static Wework wework= null;
    private static String FILEPATH = "src\\main\\resources\\xiaoaojianghu.txt";
    private static MessagePage page=null;

    @BeforeAll
    static void beforeAll() {
        wework = new Wework();
        page = wework.jumpToMessage();
    }
    @BeforeEach
    void setUp() {
    }
    @AfterEach
    void tearDown() {
    }

    @Test
    //发送350条私聊
    void sendMessage() {
        page = page.chooseConversation("梁繁兴");
        sendMessageFromFile(FILEPATH,200);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.isWeworkMainPage());
    }

    @Test
    //发送90条群聊
    void sendMessage2() {
        page = page.chooseConversation("梁繁兴");
        sendMessageFromFile(FILEPATH,1);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.isWeworkMainPage());
    }
    @Test
    //发送20条语音私聊
    void sendVoiceMessage(){
        page = page.chooseConversation("梁繁兴");
        sendVoiceMessage(1);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.isWeworkMainPage());
    }
    @Test
    //发送30图片
    void sendPicMessage(){
        page = page.chooseConversation("梁繁兴");
        sendPicMessage(1);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.isWeworkMainPage());
    }
    @Test
    void sendBussinessCard(){
        page = page.chooseConversation("梁繁兴");
        page.sendBusinessCard("卢华文");
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.isWeworkMainPage());
    }
    public void sendPicMessage(int num){
        int sum = 0;
        while(sum<num){
            log.info("sum："+sum);
            page.sendPicMessage(sum%10);
            sum++;
        }
    }
    public void sendVoiceMessage(int num){
        int sum = 0;
        while(sum<num){
            page.sendVoiceMessage();
            sum++;
        }
    }
    public void sendMessageFromFile(String filePath,int num) {
        StringBuilder result = new StringBuilder();
        try {
            String encoding = "GBK";
            File file = new File(filePath);
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                int sum = 0;
                while (((lineTxt = bufferedReader.readLine()) != null)
                && sum <num){
                    page.sendMessage(lineTxt+sum);
                    sum++;
                }
                read.close();
            } else {
                log.error("找不到指定的文件");
            }
        }catch (NoSuchElementException e){
            log.warn("没找到元素:"+e);
        }catch(TimeoutException e){
            log.warn("找元素超时"+e);
        }catch (Exception e) {
            e.printStackTrace();
            log.error("读取文件内容出错"+e);
        }
    }
}