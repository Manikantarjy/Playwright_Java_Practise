package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.Collections;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UiValidationsTest {

    Playwright playwright;
    Browser browser;
    Page page;

    //it always runs if we dont declare in any group.
    @BeforeMethod(alwaysRun = true)
    public void setup(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
                .setArgs(Collections.singletonList("--start-maximized")));
        //maximize window
        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(null)
        );
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }

    @Test(groups = {"smoke"})
    public void popupValidations(){
    //to check its visible or not
    assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
    page.locator("#hide-textbox").click();
    //checking element is hidden
    assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();

    //We have to listen to the alert and handle it, we should place the code befor
    //so it verifies the entire page
    page.onDialog(Dialog::accept); //also can be written -page.onDialog(dialog-> dialog.accept());
        //java popup which is not present in dom
     page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Alert")).click();
     page.locator("#mousehover").hover();
     page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();
     page.waitForTimeout(3000);

     //Handling frames..
        //Traversing from parent to child
        FrameLocator framesPage = page.frameLocator("#courses-iframe");
        framesPage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        //perform operations on child frame - validation to check learning paths
         String testCheck = framesPage.locator(".inner-box h1").textContent();
        System.out.println(testCheck);
}

    @Test(groups= {"smoke"})
    public void screenShotTest(){
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.png")));
        //taking element level screenshot
        Locator displayEditBox = page.getByPlaceholder("Hide/Show Example");
        displayEditBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("editboxscreenshot.png")));
        //we are hiding the editBox and we cannot take element level screenshot it fails, so taking page level screenshot
        page.locator("#hide-textbox").click();
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("postPageScreenshot.png")));

    }
}
