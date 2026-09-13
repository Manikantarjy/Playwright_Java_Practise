package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {

    Page page;
    public EventsPage(Page page){
        this.page = page;
    }

    public void goTo(){
        page.locator("#nav-events").click();
    }

     public Locator waitForEventsToLoad(){
         //  assertThat(eventCards.first()).isVisble();
        return page.getByTestId("event-card");
     }

     //dynamic way drive from test
     public Locator findEventCard(String titleCard){
        Locator eventCards =  waitForEventsToLoad();
         Locator targetCard =  eventCards.filter(new Locator.FilterOptions().setHasText(titleCard));
         assertThat(targetCard).isVisible();
         return targetCard;
    }

    public int getSeatsCount(String titleCard){
        Locator targetCard = findEventCard(titleCard);
        String seatsText =  targetCard.getByText("seats").innerText();
        System.out.println(seatsText);
        return Integer.parseInt(seatsText.split(" ")[0]);
    }

    public BookingFormPage proceedToBook(String titleCard){
        Locator targetCard = findEventCard(titleCard);
        targetCard.getByTestId("book-now-btn").click();
        return new BookingFormPage(page);
    }

}
