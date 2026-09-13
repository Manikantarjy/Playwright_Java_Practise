package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiTest {

    @Test
    public void e2eApiTesting() {
        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", "learnautomationqa93@gmail.com"); // valid email
        loginPayload.put("password", "Admin@123");

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
            APIResponse apiResponse = apiRequest.post(
                    "https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                    RequestOptions.create().setData(loginPayload)
            );

            System.out.println("Status: " + apiResponse.status());
            System.out.println("Response: " + apiResponse.text());
            Assert.assertTrue(
                    apiResponse.ok(),
                    "Login failed. Status: " + apiResponse.status()
            );

            String token = JsonPath.read(apiResponse.text(), "$.token");
            Assert.assertNotNull(token);
            System.out.println("Token: " + token);

            //Event creation API
            String eventTitle = "Playwright API Testing";

            Map<String, Object> createEventPayload = new HashMap<>();
            createEventPayload.put("title", "Playwright API Testing");
            createEventPayload.put("category", "Conference");
            createEventPayload.put("venue", "Main road");
            createEventPayload.put("city", "Hyderabad");
            createEventPayload.put("eventDate", "2026-08-24T09:42:00.000Z");
            createEventPayload.put("price", 100);
            createEventPayload.put("totalSeats", 500);

            APIResponse eventResponse = apiRequest.post(
                    "https://api.eventhub.rahulshettyacademy.com/api/events",
                    RequestOptions.create()
                            .setHeader("Authorization", "Bearer " + token)
                            .setData(createEventPayload)
            );
            System.out.println("Event status: " + eventResponse.status());
            System.out.println("Event response: " + eventResponse.text());
            Assert.assertTrue(
                    eventResponse.ok(),
                    "Create Event API should succeed: " +
                            eventResponse.status() + " - " + eventResponse.text()
            );

            // Read from eventResponse, not apiResponse
            int eventId = JsonPath.read(
                    eventResponse.text(),
                    "$.data.id");
            System.out.println("Event created. ID: " + eventId);

            //Get Event
           APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                    RequestOptions.create().setQueryParam("page", "1").setQueryParam("limit","12").setHeader("Authorization","Bearer " +token));
               Assert.assertTrue(retrieveEvents.ok(), "Event Retrieval API should succeed: "+
                       retrieveEvents.status() + " - " +
                       retrieveEvents.text());
           System.out.println(retrieveEvents.text());

           //Iterate through all events list and find out the required event created!
          List<Integer> allEventIds = JsonPath.read(retrieveEvents.text(), "$.data[*].id");
          Assert.assertTrue(allEventIds.contains(eventId),"Created event should appear in events list");

        //Delete api
        APIResponse deleteEvents = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("Authorization", "Bearer "+token));
        Assert.assertTrue(deleteEvents.ok());

        //Verify deletion is success -> GetEvents and confirm that event id doesn't exist anymore

        APIResponse retrieveEvents1 = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
           RequestOptions.create().setQueryParam("page","1").setQueryParam("limit","12").setHeader("Authorization","Bearer " +token));
        Assert.assertTrue(retrieveEvents1.ok());
        System.out.println(retrieveEvents1.text());

        List<String> titlesAfterDelete = JsonPath.read(retrieveEvents1.text(), "$.data[*].id");
        Assert.assertFalse(titlesAfterDelete.contains(eventTitle),"Deleted event must no longer appeared");
        System.out.println("Deletion verified: event no longer in list");
    }
}
