package com.qacart.testcases;

import com.qacart.base.Base;
import com.qacart.screens.LoginScreen;
import org.testng.annotations.Test;

public class LoginScreenTest extends Base {

    LoginScreen loginScreen;

    @Test
    public void testCase1(){
        loginScreen = new LoginScreen();
        loginScreen.fillEmailAndPassword("hatem@gmail.com","test123");

    }
}
