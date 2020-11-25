package com.webframework;/**
 * @author zhzh.yin
 * @create 2020-11-25 18:14
 */

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
public class AddPaperPage extends WebPage{
    List<String> urls = new ArrayList<String>(){{
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
    @FindBy(xpath="//span[text()=\"宏观时事\"]")
    private WebElement 宏观时事;
    @FindBy(xpath="//span[text()=\"政策方针\"]")
    private WebElement 政策方针;
    @FindBy(xpath="//span[text()=\"金融股市\"]")
    private WebElement 金融股市;
    @FindBy(xpath="//span[text()=\"理财观察\"]")
    private WebElement 理财观察;
    @FindBy(xpath="//span[text()=\"民生民情\"]")
    private WebElement 民生民情;
    @FindBy(xpath="//span[text()=\"地产楼市\"]")
    private WebElement 地产楼市;
    @FindBy(xpath="//span[text()=\"公司行业\"]")
    private WebElement 公司行业;
    @FindBy(xpath="//span[text()=\"国际时事\"]")
    private WebElement 国际时事;
    @FindBy(xpath="//span[text()=\" 增加文章\"]")
    private WebElement 增加文章;

    private class AddNewsPage extends AddPaperPage{
        @FindBy(xpath = "//input[@placeholder=\"请输入链接地址，仅支持“公众号”和“新浪财经”地址链接\"]")
        private WebElement 文章地址;
        @FindBy(xpath = "//span[text()=\"确 定\"]")
        private WebElement 确定;
        public AddNewsPage(WebDriver driver){
            super(driver);
            PageFactory.initElements(driver,this);
        }
        public void addNews(WebElement element ,String url){
            click(element);
            click(增加文章);
            click(element);
            sendKeys(文章地址,url);
            click(确定);
        }
    }

    public Boolean isAddPaperPage(){
        return hasElement(早报标题);
    }
    private ArrayList<WebElement> newsType(){
        return new ArrayList<WebElement>(){{
            add(宏观时事);
            add(政策方针);
            add(金融股市);
            add(理财观察);
            add(民生民情);
            add(地产楼市);
            add(公司行业);
            add(公司行业);
        }};
    }
    private void addNews(){
        click(增加文章);
        AddNewsPage page = new AddNewsPage(driver);
        page.addNews(宏观时事,urls.get(0));
    }
    public void addNewsTest(){
        addNews();
    }
}
