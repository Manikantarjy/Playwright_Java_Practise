package tests;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.DataProviderUtil;

import java.io.IOException;
import java.util.HashMap;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FrameworkBuildDataDrivenTest extends TestBase {

    @DataProvider(name="eventBookingData")
    public static Object[][] eventBookingData() throws IOException {
        //we are returning the converted multidimensional object
        return DataProviderUtil.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }

    @Test(groups={"framework"},dataProvider = "eventBookingData", description = "Create Event - Book that event and verify if that booked")
    public void TestBase(HashMap<String,String> data) {
        //Creating object for page class
        LoginPage loginPage = new LoginPage(page, base_url);
        DashboardPage dashboardPage = loginPage.loginToApplication();
        dashboardPage.waitForEventsToLoad();
        AdminEventsPage adminEventsPage = new AdminEventsPage(page);
        adminEventsPage.goTo();
        adminEventsPage.createEvent(data.get("titlePrefix"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("totalSeats"));

        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goTo();
        Locator targetCard = eventsPage.findEventCard(data.get("titlePrefix"));
        int seatsNumBeforeBooking = eventsPage.getSeatsCount(data.get("titlePrefix"));
        BookingFormPage bookingFormPage = eventsPage.proceedToBook(data.get("titlePrefix"));
        bookingFormPage.fillAndConfirm(data.get("fullName"), data.get("email"),data.get("phone"));

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
