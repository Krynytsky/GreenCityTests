package com.softserve.edu.greencity.ui.tests.signin;

import com.softserve.edu.greencity.ui.data.User;
import com.softserve.edu.greencity.ui.data.UserRepository;
import com.softserve.edu.greencity.ui.pages.common.ForgotPasswordComponent;
import com.softserve.edu.greencity.ui.tests.runner.GreenCityTestRunner;
import com.softserve.edu.greencity.ui.api.mail.GoogleMailAPI;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.*;

public class ForgotPasswordTests extends GreenCityTestRunner {
    String cssBorderColorProperty;
    String expectedBorderColorRBG;
    private final String SIGN_IN_TITLE = "Welcome back!";
    private final String FORGOT_PASS_TITLE = "Problems sign in?";
    private final String FORGOT_PASS_SUB_TITLE = "Enter your email address and we'll send you a link to regain access to your account.";
    private final String EMAIL_PLACEHOLDER_TEXT = "example@email.com";
    private final String FORGOT_PASS_EMAIL_VALIDATION_ERROR = "Please check that your e-mail address is indicated correctly";
    private final String EMPTY_EMAIL_ERROR_MESSAGE = "Email is required";
    private final String BAD_EMAIL_ERROR_MESSAGE = "Bad email or password:";
    private final String NOT_EXISTING_EMAIL_MESSAGE = "The user does not exist by this email:";
    private final String BACK_TO_SIGN_IN_LABEL = "Remember your password? Back to Sign-in";
    private final String RESTORE_EMAIL_ERROR_MESSAGE = "Password restore link already sent, please check your email:";
    private final String FORGOT_PASS_MAIL_SUBJECT = "Confirm restoring password";

    @BeforeClass
    public void beforeClass() {
        cssBorderColorProperty = "border-color";
        expectedBorderColorRBG = "rgb(240, 49, 39)";
    }

    private GoogleMailAPI googleMailAPI() {
        return new GoogleMailAPI();
    }

    @Test(testName = "GC-503")
    @Description("Verify that user can sign in with valid credentials")
    public void isForgotPasswordPopup() {
        logger.info("Starting isForgotPasswordPopup");
        String forgotPasswordTitle = loadApplication()
                .signIn()
                .clickForgotPasswordLink()
                .getForgotTitleText();

        Assert.assertEquals(forgotPasswordTitle, FORGOT_PASS_TITLE);
    }

    @Test(testName = "GC-511")
    @Description("Verify that 'Email' field is highlighted and error-message is shown after user leaves 'Email' field empty")
    public void forgotPasswordWithEmptyEmailFieldValidation() {
        logger.info("Starting forgotPasswordWithEmptyEmailFieldValidation");
        String emailErrorMessage = loadApplication()
                .signIn()
                .clickForgotPasswordLink()
                .pressTabEmail()
                .getEmailValidationErrorText();

        Assert.assertEquals(emailErrorMessage, EMPTY_EMAIL_ERROR_MESSAGE);
    }

    @DataProvider
    private Object[] getIncorrectEmails() {
        return new Object[]{
                "qwertygmail.com",
                "qwerty"
        };
    }

    @Test(dataProvider = "getIncorrectEmails", testName = "GC-505")
    @Description("Verify that 'Email' field is highlighted and error-message is shown after user enters not valid e-mail address")
    public void forgotPasswordWithInCorrectEmailFieldValidation(String incorrectEmail) {
        logger.info("Starting forgotPasswordWithInCorrectEmailFieldValidation");
        ForgotPasswordComponent forgotPasswordComponent = loadApplication()
                .signIn()
                .clickForgotPasswordLink()
                .inputEmail(incorrectEmail);

        String emailFieldBorderColor = forgotPasswordComponent.getEmailField().getCssValue(cssBorderColorProperty);

        softAssert.assertEquals(emailFieldBorderColor, expectedBorderColorRBG);
        softAssert.assertEquals(forgotPasswordComponent.getEmailValidationErrorText(), FORGOT_PASS_EMAIL_VALIDATION_ERROR);
        softAssert.assertAll();
    }

    @Test(testName = "GC-515")
    @Description("Verify that User is directed back to Sign In page after he clicks on 'Back to Sign in' link")
    public void directedBackToSignInFromForgot() {
        logger.info("Starting directedBackToSignInFromForgot");
        String signInTitle = loadApplication()
                .signIn()
                .clickForgotPasswordLink()
                .clickBackLink()
                .getTitleText();

        Assert.assertEquals(signInTitle, SIGN_IN_TITLE);
    }

    @Test(testName = "GC-518")
    @Description("Verify UI of the 'Forgot Password' popup according to the mock up")
    public void forgotPassFormValidation() {
        logger.info("Starting forgotPassFormValidation");
        ForgotPasswordComponent forgotPasswordComponent = loadApplication()
                .signIn()
                .clickForgotPasswordLink();

        softAssert.assertEquals(forgotPasswordComponent.getForgotTitleText(), FORGOT_PASS_TITLE);
        softAssert.assertEquals(forgotPasswordComponent.getSubTitleText(), FORGOT_PASS_SUB_TITLE);
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedEmailField());
        softAssert.assertEquals(forgotPasswordComponent.getEmailPlaceholderText(), EMAIL_PLACEHOLDER_TEXT);
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedSubmitButton());
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedGoogleSignInButton());
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedBackLink());
        softAssert.assertEquals(forgotPasswordComponent.getBackLinkLabelText(), BACK_TO_SIGN_IN_LABEL);
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedCloseFormButton());

        forgotPasswordComponent.changeWindowWidth(800);

        softAssert.assertEquals(forgotPasswordComponent.getForgotTitleText(), FORGOT_PASS_TITLE);
        softAssert.assertEquals(forgotPasswordComponent.getSubTitleText(), FORGOT_PASS_SUB_TITLE);
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedEmailField());
        softAssert.assertEquals(forgotPasswordComponent.getEmailPlaceholderText(), EMAIL_PLACEHOLDER_TEXT);
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedSubmitButton());
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedGoogleSignInButton());
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedBackLink());
        softAssert.assertEquals(forgotPasswordComponent.getBackLinkLabelText(), BACK_TO_SIGN_IN_LABEL);
        softAssert.assertTrue(forgotPasswordComponent.isDisplayedCloseFormButton());

        forgotPasswordComponent.maximizeWindow();

        softAssert.assertAll();
    }

    @Test(testName = "GC-520")
    @Description("Verify that Unregistered User cannot restore password")
    public void restorePassForUnregisterUser() {
        logger.info("Starting restorePassForUnregisterUser");
        User user = UserRepository.get().unregisterUser();

        googleMailAPI().clearMail(user.getEmail(), user.getPassword());

        ForgotPasswordComponent forgotPasswordComponent = loadApplication()
                .signIn()
                .clickForgotPasswordLink()
                .unsuccessfullySubmit(user);

        String emailFieldBorderColor = forgotPasswordComponent.getEmailField().getCssValue(cssBorderColorProperty);
        expectedBorderColorRBG = "rgb(135, 135, 135)";
        softAssert.assertEquals(emailFieldBorderColor, expectedBorderColorRBG);
        softAssert.assertTrue(forgotPasswordComponent.getEmailValidationErrorText().contains(NOT_EXISTING_EMAIL_MESSAGE));

        googleMailAPI().waitFroMassagesWithSubject(FORGOT_PASS_MAIL_SUBJECT, true, 3, 10, user.getEmail(), user.getPassword());
        int numberOfEmail = new GoogleMailAPI().getNumberMailsBySubject(user.getEmail(), user.getPassword(), FORGOT_PASS_MAIL_SUBJECT, 50);
        softAssert.assertEquals(numberOfEmail, 0);
        softAssert.assertAll();
    }

    @Test(testName = "GC-504")
    @Description("Verify that Registered User receives an e-mail with link to enter new password")
    public void successMailToRestorePass() {
        logger.info("Starting successMailToRestorePass");
        User user = UserRepository.get().googleUserCredentials();

        googleMailAPI().clearMail(user.getEmail(), user.getPassword());

        loadApplication()
                .signIn()
                .clickForgotPasswordLink()
                .successfullySubmit(user);

        googleMailAPI().waitFroMassagesWithSubject(FORGOT_PASS_MAIL_SUBJECT, true, 3, 10, user.getEmail(), user.getPassword());
        int numberOfEmail = new GoogleMailAPI().getNumberMailsBySubject(user.getEmail(), user.getPassword(), FORGOT_PASS_MAIL_SUBJECT, 3);
        Assert.assertEquals(numberOfEmail, 1);
    }

    //@Test(testName = "GC-521")
    @Description("Verify that Registered User does not receive email to restore password twice")
    public void unSuccessRestorePassTwice() {
        logger.info("Starting unSuccessRestorePassTwice");
        User user = UserRepository.get().googleUserCredentials();

        ForgotPasswordComponent forgotPasswordComponent = loadApplication()
                .signIn()
                .clickForgotPasswordLink()
                .unsuccessfullySubmit(user);

        String emailFieldBorderColor = forgotPasswordComponent.getEmailField().getCssValue(cssBorderColorProperty);

        softAssert.assertEquals(emailFieldBorderColor, "rgb(135, 135, 135)");//expectedBorderColorRBG
        softAssert.assertTrue(forgotPasswordComponent.getEmailValidationErrorText().contains(RESTORE_EMAIL_ERROR_MESSAGE));

        googleMailAPI().waitFroMassagesWithSubject(FORGOT_PASS_MAIL_SUBJECT, true, 3, 10, user.getEmail(), user.getPassword());
        int numberOfEmail = new GoogleMailAPI().getNumberMailsBySubject(user.getEmail(), user.getPassword(), FORGOT_PASS_MAIL_SUBJECT, 20);

        softAssert.assertEquals(numberOfEmail, 1);
        softAssert.assertAll();
    }
}


