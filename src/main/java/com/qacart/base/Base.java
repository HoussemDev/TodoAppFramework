package com.qacart.base;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Base {

    public Base(){
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    protected static AndroidDriver<MobileElement> driver;
    protected FileInputStream inputStream;
    protected Properties prop;

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
