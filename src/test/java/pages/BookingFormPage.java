package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {
    Page page ;

   // private static final String TICKET_COUNT_ID = "#ticket-count";
    private static final String FULL_NAME_LABEL = "Full Name";
    private static final String EMAIL_LABEL = "Email";
    private static final String PHONE = "#phone";

    public BookingFormPage(Page page){
        this.page = page;
    }

    public void fillAndConfirm(String fullName,String email, String phoneNumber){
        page.getByLabel(FULL_NAME_LABEL).fill(fullName);
        page.getByLabel(EMAIL_LABEL).fill(email);
        page.locator(PHONE).fill(phoneNumber);
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved")).isVisible();
    }
}
