package com.appframework;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
class MessagePageTest {
    private DateTimeFormatter dtf= DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
    private static String FILEPATH = "src\\main\\resources\\xiaoaojianghu.txt";
    private static MessagePage page;
    private static Wework wework;
    @BeforeAll
    static void beforeAll() {
        wework = new Wework();
    }
    @BeforeEach
    void setUp() {
    }
    @AfterEach
    void tearDown() {
    }
    private String currentTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        return  localDateTime.format(dtf);
    }
//    @Test
    //发送350条私聊
    void sendMessage() {
        page = wework.jumpToMessage().chooseConversation("卢华文");
        sendMessageFromFile(FILEPATH,1);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.hasElement(page.byText("消息")));
    }

//    @Test
//    发送90条群聊
    void sendMessage2() {
        page = wework.jumpToMessage().chooseConversation("梁繁兴");
        sendMessageFromFile(FILEPATH,90);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.hasElement(page.byText("消息")));
    }
    @Test
    //发送20条语音私聊
    void sendVoiceMessage(){
        page = wework.jumpToMessage().chooseConversation("梁繁兴");
        sendVoiceMessage(20);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.hasElement(page.byText("消息")));
    }
//    @Test
    //发送30图片
    void sendPicMessage(){
        page = wework.jumpToMessage().chooseConversation("尹珍枝测试群聊");
        sendPicMessage(30);
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.hasElement(page.byText("消息")));
    }
//    @Test
    void sendBussinessCard(){
        page = wework.jumpToMessage().chooseConversation("尹珍枝测试群聊");
        page.sendBusinessCard("卢华文");
        page.screenshot();
        page.clickBack();
        Assert.assertTrue(page.hasElement(page.byText("消息")));
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