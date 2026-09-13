package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
    }

    @Test(description = "Create Event - Book that event and verify if that booked")
    public void TestBase() {
        page.getByLabel("Email").fill("learnautomationqa93@gmail.com");
        page.getByLabel("Password").fill("Admin@123");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();

        //Validation to check the browse events locator
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
//        page.locator("#event-title-input").fill("testQA");
//        page.locator("#admin-event-form textarea").fill("This is test event!");
//
//        //Dropdown
//        page.getByLabel("Category").selectOption("Concert");
//        page.locator("#city").fill("Hyderabad");
//        page.locator("#venue").fill("Ravindra Bharati");
//
//        //Calendar
//        page.getByLabel("Event Date & Time").fill("2026-08-19T22:17");
//        page.getByLabel("Price ($)").fill("50");
//        page.locator("#total-seats").fill("20");
//
//        //Add event button
//        page.locator("#add-event-btn").click();
//
//        //By default the wait timeout is 5 seconds for assertThat
//        PlaywrightAssertions.assertThat(
//                page.getByText("Event created!", new Page.GetByTextOptions().setExact(true))
//        ).isVisible();
//        //Default timeout for actions like fill, click will be 10 seconds
//        //we can override default time for actions like below
//        page.setDefaultTimeout(8000);
//
//        //How do you change the assertion timeout globally for all assertions in a test suite?
//       // PlaywrightAssertions.setDefaultAssertionTimeout(7000); //global assertion timeout 7 seconds
//
        page.locator("#nav-events").click();
        page.waitForURL("**/events");
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Locator eventCards = page.getByTestId("event-card");
        System.out.println(eventCards.count());


        //This is filtering of a particular card from list of cards
        //this way we can act upon other operations uniquely from list of items in page related to the card: seats verification, book now button
        Locator targetCard = page
                .getByTestId("event-card")
                .filter(new Locator.FilterOptions().setHasText("testQA"));
        PlaywrightAssertions.assertThat(targetCard).isVisible();

        //How do you override the assertion timeout for a single, specific assertion call (step-level)?
        assertThat(targetCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        //next we need to verify booked seats - specific card
        String cardSeats = targetCard.getByText("seats").innerText();
        System.out.println(cardSeats);

        //if we use page. here it will check all the elements with bookNow,
        // so we use targetCard variable which is already filtered for specific card
        //this way we handle specific operations in playwright
        targetCard.getByTestId("book-now-btn").click();
        page.getByPlaceholder("Your full name").fill("Manikanta");
        page.getByLabel("Email").fill("test@gmail.com");
        page.locator("#phone").fill("1234567890");
        page.getByText("Confirm Booking").click();
        //We will capture the Booking Reference and after viewing my bookings

        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        //Instead of button its tagged to link, so we use AriRole.LINK instead of button
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //Verify in Booking History
        Locator bookingCards =  page.locator("#booking-card");
        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(targetCard).isVisible();
        page.locator("#nav-events").click();

        Locator eventCardsAfterBooking = page.getByTestId("event-card");
        Locator targetCardsAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("testQA"));
        String seatsTextAfterBooking = targetCardsAfterBooking.getByText("seats").innerText();
        System.out.println(seatsTextAfterBooking);
        //AfterBooking seats are less compared to before bookings
    }


    @AfterMethod
    public void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
