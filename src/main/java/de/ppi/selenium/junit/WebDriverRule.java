package de.ppi.selenium.junit;

import org.junit.rules.ExternalResource;

import de.ppi.selenium.browser.SessionManager;
import de.ppi.selenium.browser.WebBrowser;

/**
 * Junit-Rule which handle the WebDriver.
 *
 */
public class WebDriverRule extends ExternalResource {

    /**
     * The number of test which runs.
     */
    private static volatile long nrOfTests = 0;

    /**
     * Maximalnumber of tests before the webdriver should newly created.
     */
    // TODO: This should be configurable via Annotations.
    private static final long MAX_NR_OF_REUSE = 10;

    @Override
    protected void before() throws Throwable {
        nrOfTests++;
        final SessionManager manager = SessionManager.getInstance();
        if (nrOfTests > MAX_NR_OF_REUSE) {
            final WebBrowser browser = manager.getCurrentSession(false);
            if (browser != null) {
                manager.removeSession(browser);
                browser.quit();
            }
        }
        final WebBrowser browser = manager.getCurrentSession(false);
        if (browser == null) {
            manager.getCurrentSession(true);
        }
    }

}
