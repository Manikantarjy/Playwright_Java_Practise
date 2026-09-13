package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

public class NewTab {

    Playwright playwright;
    Browser browser;
    Page page;

    //BrowserContext: an isolated browser session, similar to an incognito profile.
    BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setup(){
       playwright= Playwright.create();
       browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
       context = browser.newContext();

        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

       page = context.newPage();
       page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
    }


    @Test(description = "SwitchTab Verification")
    public void testSwitch(){
        Page newPage = context.waitForPage(() ->{
            page.getByText("Free Access to InterviewQues/ResumeAssistance/Material").click();
        });
        newPage.waitForLoadState();
        System.out.println("New tab URL: " +newPage.url());
        System.out.println("New tab title: " + newPage.title());

        // Interact with the new tab
        String childText = newPage.locator(".red").textContent();
        String emailId = childText.split("at ")[1].split(" ")[0];
       // System.out.println(emailId);
        page.locator("#username").fill(emailId);
        page.waitForTimeout(3000);
        System.out.println(page.getByLabel("Username:").inputValue());
    }


    @Test(description="UI controls")
    public void uiControls(){
       Locator userRdBtn =  page.getByRole(AriaRole.RADIO,new Page.GetByRoleOptions().setName("User"));
        userRdBtn.click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(userRdBtn.isChecked());

        Locator checkbox = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I agree to the terms"));
        checkbox.check(); //click also works
        Assert.assertTrue(checkbox.isChecked());

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
        page.waitForTimeout(3000);


    }


    @AfterMethod
    public void tearDown(){

        // Stop tracing and export it into a zip archive.
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }
}
