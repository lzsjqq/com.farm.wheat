package com.farm.wheat.share.service.processer;

import com.snow.tiger.ip.proxy.download.PhantomJSDownloaderSetting;
import com.snow.tiger.ip.proxy.util.SeleniumUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xyc
 * @Date: 2019/1/4 15:21
 * @Version 1.0
 */
public class Tickte12306Processor {

    private static String userName = "413280603@qq.com";
    private static String passwd = "ac37292465";
    private static String passengers = "许允冲";
    private static String starts = "菏泽";
    private static String ends = "深圳";
    private static String dtime = "2019-01-07";
    //车次，0代表所有车次，依次从上到下，1代表所有车次，依次类推
    private static int order = 0;
    private static String login_url = "https://kyfw.12306.cn/otn/resources/login.html";
    private static String initMy_url = "https://kyfw.12306.cn/otn/view/index.html";
    private static String ticket_url = "https://kyfw.12306.cn/otn/leftTicket/init";

    public static void main(String[] args) throws Exception {
        PhantomJSDownloaderSetting setting = new PhantomJSDownloaderSetting();
        WebDriver driver = SeleniumUtil.getPhantomJSDriver(setting);
        driver.manage().window().maximize();
        driver.get(login_url);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Thread.sleep(1000L);
//        WebElement userNameElement = driver.findElement(By.id("J-userName"));
//        WebElement pwdElement = driver.findElement(By.id("J-password"));

        WebElement imgElement = driver.findElement(By.id("J-qrImg"));
        String src = imgElement.getAttribute("src");
        System.out.println("请使用手机扫描下面的二维码...");
        System.out.println(src);
        Scanner scan = new Scanner(System.in);
        String read = scan.nextLine();
        System.out.println(read);
        // 设置用户名密码
//        userNameElement.sendKeys(userName);
//        pwdElement.sendKeys(passwd);
//        // 获取登录按钮
//        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
//        WebElement loginButton = driver.findElement(By.id("loginSub"));
//        loginButton.click(); //设置线程休眠时间等待页面加载完成 Thread.sleep(1000L);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.equals(initMy_url)) {
            System.out.println("恭喜你登陆成功...");
        }
        //
        System.out.println("开始购票...");
        Thread.sleep(1000L);
        driver.get(ticket_url);

        System.out.println("currentUrl:" + driver.getCurrentUrl());
        Thread.sleep(1000L);
        int cxTime = 1;
//        Cookie cookie1 = new Cookie("_jc_save_fromStation", starts);
//        Cookie cookie2 = new Cookie("_jc_save_toStation", ends);
//        Cookie cookie3 = new Cookie("_jc_save_fromDate", dtime);
//        driver.manage().addCookie(cookie1);
//        driver.manage().addCookie(cookie2);
//        driver.manage().addCookie(cookie3);
        driver.findElement(By.id("fromStationText")).sendKeys(starts);
        driver.findElement(By.id("toStationText")).sendKeys(ends);
        driver.findElement(By.id("train_date")).sendKeys(dtime);
        while (true) {
            driver.findElement(By.id("query_ticket")).click();
            System.out.println("第" + cxTime++ + "次查询");
            // 进行查询操作
            try {
                driver.findElement(By.linkText("预定")).click();
                System.out.println("开始预订。。。");
                driver.findElement(By.linkText(passengers)).click();
                Thread.sleep(1000);
                driver.findElement(By.id("dialog_xsertcj_ok")).click();
                System.out.println("提交订单...");
                driver.findElement(By.id("submitOrder_id")).click();
                System.out.println("确认选座...");
                driver.findElement(By.id("qr_submit_id")).click();
                System.out.println("预订成功...");
                break;
            } catch (Exception e) {
                System.out.println("预定失败。。。");
            }
        }

    }


}
