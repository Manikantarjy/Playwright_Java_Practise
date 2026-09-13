package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.Collections;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {


    //Network - We can mock the existing response(original one) and instead we can send the fake response.

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false)
         .setArgs(Collections.singletonList("--start-maximized")));
        //maximize window
        BrowserContext context = browser.newContext(
                new Browser.NewContextOptions().setViewportSize(null)
        );

        page = context.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
    }

    @Test(description = "Sandbox banner is shown when we have 6 events in place")
    public void TestMock() {
        page.getByLabel("Email").fill("learnautomationqa93@gmail.com");
        page.getByLabel("Password").fill("Admin@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

        //Validation to check the browse events locator
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");

        //We have route class for mocking
        //The original response don't give it instead give the response from the events
        page.route("**/api/events**", route -> route.fulfill(new Route.FulfillOptions()
                .setPath(Paths.get("src/test/resources/events_6.json"))));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        // page.waitForTimeout(7000);
        //Now we validate
        Locator eventCards = page.getByTestId("event-card");
        assertThat(eventCards.first()).isVisible();   //5 seconds
        Assert.assertEquals(eventCards.count(), 6);
        //we use playwright assertion and verify first element only
        assertThat(page.locator(".mx-1").first()).isVisible();
        page.waitForTimeout(7000);
        //validating with 4 events

    }

        @Test(description = "Verify events in place")
        public void routeResumeTest(){
            page.getByLabel("Email").fill("learnautomationqa93@gmail.com");
            page.getByLabel("Password").fill("Admin@123");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
            assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

            //Bookings
            page.getByTestId("nav-bookings").click();
            page.route("**/api/bookings**",route -> route.resume(new Route.ResumeOptions()
                .setUrl("https://api.eventhub.rahulshettyacademy.com/api/bookings/730123")));
            page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("View Details")).first().click();
            assertThat(page.getByText("Booking not found")).isVisible();
   }

}