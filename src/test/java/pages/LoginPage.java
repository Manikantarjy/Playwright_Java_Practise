package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class LoginPage {
    Page page;
    String base_url;
    private static final String email_placeholder = "learnautomationqa93@gmail.com";
    private static final String password_label = "Admin@123";

    //page object should stay independent
    //We need to create constructor with same type of arguments as in page class
    public LoginPage(Page page, String baseurl){
        this.page = page;
        this.base_url = baseurl;
    }

    public DashboardPage loginToApplication() {
        page.navigate(base_url);
        page.getByLabel("Email").fill(email_placeholder);
        page.getByLabel("Password").fill(password_label);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign in")).click();
        return new DashboardPage(page);
    }
}
