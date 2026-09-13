package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import pages.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildTest extends TestBase {

    @Test(groups = {"framework"},description = "Create Event - Book that event and verify if that booked")
    public void TestBase() {
        String titleEvent = "Playwright Framework Test";
        //Creating object for page class
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(titleEvent, "Playwright test event", "Concert","Test City","Test Venue", "2027-03-31T10:00","50","20");

        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        eventsPage.findEventCard(titleEvent);
        int seatsNumBeforeBooking = eventsPage.getSeatsCount(titleEvent);
        BookingFormPage bookingFormPage = eventsPage.proceedToBook(titleEvent);
        bookingFormPage.fillAndConfirm("Test Student", "test@gmail.com", "9732324322");

//        page.locator("#nav-events").click();
//        Locator eventCards = page.getByTestId("event-card");
//        System.out.println(eventCards.count());
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
        PlaywrightAssertions.setDefaultAssertionTimeout(7000);



        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef = page.locator(".booking-ref").innerText();
        //Instead of button its tagged to link, so we use AriRole.LINK instead of button
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("View My Bookings")).click();

        //Verify in Booking History
//        Locator bookingCards =  page.locator("#booking-card");
//        Locator targetBookingCard = bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
//        assertThat(targetCard).isVisible();
//        page.locator("#nav-events").click();
//        Locator eventCardsAfterBooking = page.getByTestId("event-card");
//        Locator targetCardsAfterBooking = eventCardsAfterBooking.filter(new Locator.FilterOptions().setHasText("testQA"));
//        String seatsTextAfterBooking = targetCardsAfterBooking.getByText("seats").innerText();
//        System.out.println(seatsTextAfterBooking);
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
