package com.qacart.base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.PublicKey;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Base {



    protected static AndroidDriver<MobileElement> driver;
    protected FileInputStream inputStream;
    protected Properties prop;
    public static ExtentReports extent;
    public static ExtentTest logger;

    public Base(){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @BeforeSuite
    public void beforeSuite(){
        extent = new ExtentReports("Reports/index.html");
        extent.addSystemInfo("Framework Type", "Appium Page Object");
        extent.addSystemInfo("Author","Houssem");
        extent.addSystemInfo("Environment", "Linux");
        extent.addSystemInfo("App0", "To Do QACart");

    }

    @BeforeMethod
    public void beforeMethod(Method method){
        logger = extent.startTest(method.getName());
    }

    @AfterSuite
    public void afterSuite(){
        extent.flush();
    }

    @AfterMethod
    public void afterMethod(Method method, ITestResult result){
        if(result.getStatus() == ITestResult.SUCCESS){
            logger.log(LogStatus.PASS, "Test is passed because there is no error");
        }else if(result.getStatus() == ITestResult.FAILURE){
            logger.log(LogStatus.FAIL, "Test is Failed");
            logger.log(LogStatus.FAIL, result.getThrowable());

        }else {
            logger.log(LogStatus.SKIP, "Test is skipped");
        }
    }

    @Parameters({"deviceName", "platformName", "platformVersion"})
    @BeforeClass
    public void beforeClass(String deviceName, String platformName, String platformVersion) throws IOException {

        File propFile = new File("src/main/resources/Config/config.properties");
        inputStream = new FileInputStream(propFile);
        prop = new Properties();
        prop.load(inputStream);

        if(platformName.equalsIgnoreCase("Android")){
        File androidApp = new File(prop.getProperty("androidAppPath"));
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.APPLICATION_NAME, platformName);
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, prop.getProperty("androidAutomationName"));
        caps.setCapability(MobileCapabilityType.APP, androidApp.getAbsolutePath());

        driver = new AndroidDriver<MobileElement>(new URL(prop.getProperty("appiumServer")), caps);
        } else if(platformName.equalsIgnoreCase("ios")){

            File iosApp = new File(prop.getProperty("iosAppPath"));
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(MobileCapabilityType.APPLICATION_NAME, platformName);
            caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
            caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
            caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, prop.getProperty("iosAutomationName"));
            caps.setCapability(MobileCapabilityType.APP, iosApp.getAbsolutePath());
            driver = new AndroidDriver<MobileElement>(new URL(prop.getProperty("appiumServer")), caps);

        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @AfterClass
    public void afterClass(){
        driver.quit();
    }
}
