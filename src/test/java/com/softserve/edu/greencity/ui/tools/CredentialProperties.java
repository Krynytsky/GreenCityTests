package com.softserve.edu.greencity.ui.tools;


import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Properties;

public class CredentialProperties {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void checkCredentialsExist(){
        if (!isCredentialsPropertiesExist()){
            setCredentialsProperties();
            logger.warn("resources\\credentials.properties was absent, please enter your credentials to the new generated file. All passwords set ass global variable");
        }
    }
    private Properties properties = new Properties();
    @SneakyThrows(IOException.class)
    private void setCredentialsProperties() {


        properties.setProperty("invalidPassDigit","  ");
        properties.setProperty("defaultName","  ");
        properties.setProperty("nameForRegistration","  ");
        properties.setProperty("emailForRegistration","  ");
        properties.setProperty("DEFAULT_PASS","  ");
        properties.setProperty("DEFAULT_PASS","  ");
        properties.setProperty("comfTemporaryPass","  ");
        properties.setProperty("invalidName","  ");
        properties.setProperty("invalidPassLowercase","  ");
        properties.setProperty("invalidPass","  ");
        properties.setProperty("invalidPassSpecChar","  ");
        properties.setProperty("invalidPassSpace","  ");
        properties.setProperty("invalidPassLength","  ");
        properties.setProperty("invalidPassUppercase","  ");
        properties.setProperty("validIncorrectPassword","  ");
        properties.setProperty("temporaryLoginName","  ");
        properties.setProperty("defaultEmail","  ");
        properties.setProperty("validUnregisterEmail","  ");
        properties.setProperty("invalidEmail","  ");
        properties.setProperty("passwordForRegistration","  ");
        properties.setProperty("validUnregisterEmail","  ");

        FileOutputStream out = new FileOutputStream("src/test/resources/credentials.properties");
        properties.store(out,"Please setup credentials");   }

    private String getRandom() {
        return String.format("%s, %d", "+", (int) (Math.random() * ((Integer.MAX_VALUE - 1) - 10 + 1) + 1))
                .replaceAll("\\s+", "")
                .replace("-", "")
                .replace(",", "");
    }

    private boolean isCredentialsPropertiesExist()  {
        return new File("src/test/resources/credentials.properties").isFile();
    }

}
