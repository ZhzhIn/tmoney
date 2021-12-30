package com.webframework;/**
 * @author zhzh.yin
 * @create 2020-11-25 18:14
 */

import com.tengmoney.gui.WebPage;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈〉
 *
 * @author zhzh.yin
 * @create 2020/11/25
 */
public class AddPaperPage extends WebPage {
    List<String> urls = new ArrayList<String>() {{
        add("https://mp.weixin.qq.com/s/7ZmIBmoKkCQ9dlYHXP71eg");
        add("https://mp.weixin.qq.com/s/3Pyqeit0-xS3UB4_tEfFmw");
        add("https://mp.weixin.qq.com/s/wFQFujSI3bMRoTQNW2rmzg");
        add("https://mp.weixin.qq.com/s/8d_claIP2PjPph5f13NJ_Q");
        add("https://mp.weixin.qq.com/s/gxGbLI61-_Sth-KBTBQQ-w");
        add("https://mp.weixin.qq.com/s/sxVdiJVewlG3GU_18_227g");
        add("https://mp.weixin.qq.com/s/Up3QBqiHLe7QoxIK2rr9_A");
        add("https://mp.weixin.qq.com/s/gRtFBDbfXRNr2oeyt9pzgw");
        add("https://mp.weixin.qq.com/s/oyXmMEGE-dMRxxfNgKhRBg");
        add("https://mp.weixin.qq.com/s/5C1pa86dm3-CjpIFNmmwHw");
    }};

    public AddPaperPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@placeholder=\"请输入早报标题\"]")
    private WebElement 早报标题;
    //    @FindBy(xpath="//button/span[text()=\"宏观实事\"]")
    @FindBy(xpath = "//span[text()=\"宏观时事\"]")
    private WebElement 宏观时事;
    @FindBy(xpath = "//span[text()=\"政策方针\"]")
    private WebElement 政策方针;
    @FindBy(xpath = "//span[text()=\"金融股市\"]")
    private WebElement 金融股市;
    @FindBy(xpath = "//span[text()=\"理财观察\"]")
    private WebElement 理财观察;
    @FindBy(xpath = "//span[text()=\"民生民情\"]")
    private WebElement 民生民情;
    @FindBy(xpath = "//span[text()=\"地产楼市\"]")
    private WebElement 地产楼市;
    @FindBy(xpath = "//span[text()=\"公司行业\"]")
    private WebElement 公司行业;
    @FindBy(xpath = "//span[text()=\"国际时事\"]")
    private WebElement 国际时事;
    @FindBy(xpath = "//span[text()=\" 增加文章\"]")
    private WebElement 增加文章;
    @FindBy(xpath = "//input[@placeholder=\"请输入链接地址，仅支持“公众号”和“新浪财经”地址链接\"]")
    private WebElement 文章地址;
    @FindBy(xpath = "//span[text()=\"确 定\"]")
    private WebElement 确定;
    @FindBy(xpath = "//span[text()=\"保 存\"]")
    private WebElement 保存;

    @FindBy(xpath = "//textarea[@placeholder=\"请输入概述\"]")
    private WebElement 概述;
    @FindBy(xpath = "//textarea[@placeholder=\"请输入今日综述\"]")
    private WebElement 今日综述;
    @FindBy(xpath = "//textarea[@placeholder=\"请输入点评\"]")
    private WebElement 点评;

    @FindBy(xpath = "//i[@aria-label=\"图标: close\"]")
    private WebElement 关闭;
    @FindBy(xpath = "//span[text()=\"手动添加\"]")
    private WebElement 手动添加;
    @FindBy(xpath = "//input[@placeholder=\"请输入文章标题\"]")
    private WebElement 文章标题;
    @FindBy(xpath = "//input[@placeholder=\"请输入原创信息\"]")
    private WebElement 原创信息;

    @FindBy(xpath = "//input[@type=\"file\"]")
    private WebElement 上传图片;


    @FindBy(xpath = "//textarea[@placeholder=\"请输入一日谈\"]")
    private WebElement 一日谈;

    @Deprecated
    private class AddNewsPage extends AddPaperPage {
        @FindBy(xpath = "//input[@placeholder=\"请输入链接地址，仅支持“公众号”和“新浪财经”地址链接\"]")
        private WebElement 文章地址;
        @FindBy(xpath = "//span[text()=\"确 定\"]")
        private WebElement 确定;

        public AddNewsPage(WebDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        public void addNews(WebElement element, String url) {
            click(element);
            click(增加文章);
            click(element);
            sendKeys(文章地址, url);
            click(确定);
        }
    }

    public Boolean isAddPaperPage() {
        return hasElement(早报标题);
    }

    private ArrayList<WebElement> newsType() {
        return new ArrayList<WebElement>() {{
            add(宏观时事);
            add(政策方针);
            add(金融股市);
            add(理财观察);
            add(民生民情);
            add(地产楼市);
            add(国际时事);
            add(公司行业);
        }};
    }

    //    @Deprecated
    private void addNews() {
        sendKeys(早报标题, System.currentTimeMillis() + "");
        /*click(增加文章);
        AddNewsPage page = new AddNewsPage(driver);
        page.addNews(宏观时事,urls.get(0));*/
        for (int i = 0, j = 0; i < newsType().size(); i++) {
            click(newsType().get(i));
            if (hasElement(增加文章)) {
                click(增加文章);
                click(newsType().get(i));
                sendKeys(文章地址, urls.get(i));
                click(确定);
                if(!hasElement(概述)){
                    click(关闭);
                    click(增加文章);
                    click(手动添加);
                    sendKeys(文章标题, "title" + i);
                    sendKeys(原创信息, "content" + i);
                    click(确定);
                }
            }
            if(hasElement(上传图片)){
                sendKeys(上传图片, "pic/1.png");
            }
            sendKeys(概述, urls.get(i) + 概述.getText());
            sendKeys(今日综述, urls.get(i) + 今日综述.getText());
            sendKeys(点评, urls.get(i) + 点评.getText());
        }
        sendKeys(一日谈,一日谈.getClass().getSimpleName());
        click(保存);
    }

    public void addNewsTest() {
        addNews();
    }
}
