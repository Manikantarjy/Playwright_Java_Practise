package pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventsPage {

    Page page;
    private static final String EVENT_TITLE_INPUT = "#event-title-input";
    private static final String DESCRIPTION_TEXTAREA = "#admin-event-form textarea";
    private static final String CATEGORY_LABEL="#category";
    private static final String CITY_LABEL="#city";
    private static final String VENUE_LABEL="#venue";
    private static final String DATE_TIME_LABEL="Event Date & Time";
    private static final String PRICE_LABEL = "Price ($)";
    private static final String TOTAL_SEATS_LABEL = "Total Seats";
    private static final String ADD_EVENT_BTN = "#add-event-btn";
    private static final String SUCCESS_TOAST = "Event Created!";

    public AdminEventsPage(Page page){
        this.page = page;
    }

    public void goTo(){
        page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
    }

    public void createEvent(String title, String description,String category, String city, String venue, String dateTime, String price, String totalSeats){
        //Validation to check the browse events locator
        page.locator(EVENT_TITLE_INPUT).fill(title);
        page.locator(DESCRIPTION_TEXTAREA).fill(description);
        page.locator(CATEGORY_LABEL).selectOption(category);
        page.locator(CITY_LABEL).fill(city);
        page.locator(VENUE_LABEL).fill(venue);
        page.getByLabel(DATE_TIME_LABEL).fill(dateTime);
        page.getByLabel(PRICE_LABEL).fill(price);
        page.getByLabel(TOTAL_SEATS_LABEL).fill(totalSeats);
        page.locator(ADD_EVENT_BTN).click();
        assertThat(page.getByText(SUCCESS_TOAST)).isVisible();

    }
}
