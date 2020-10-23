package com.softserve.edu.greencity.ui.tests.runner;

import com.softserve.edu.greencity.ui.api.google.sheets.ValueProvider;
import org.testng.*;

import java.lang.reflect.Method;

public class RemoteSkipTestAnalyzer implements IInvokedMethodListener {
    public void beforeInvocation(IInvokedMethod invokedMethod, ITestResult result) {
        Method method = result.getMethod().getConstructorOrMethod().getMethod();

        if (method == null) {
            return;
        }
        if (method.isAnnotationPresent(LocalOnly.class) && isRemote()) {
            throw new SkipException("These Tests shouldn't be run on remote");
        }
            return;
    }
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    }
    public boolean isRemote(){
        return ValueProvider.remote();
    }

}


