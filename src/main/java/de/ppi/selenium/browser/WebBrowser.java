/**
 *
 */
package de.ppi.selenium.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

/**
 * Access to the web-browser.
 * 
 * @author niels
 *
 */
public interface WebBrowser extends WebDriver, WrapsDriver {

    String getSessionId();

    /**
     * Base url which should be added to each relative-url.
     * 
     * @return the base-url withou trailing slash.
     */
    String getBaseUrl();

    /**
     * Call the relative url.
     *
     * @param relativeUrl the relative url.
     */
    void getRelativeUrl(String relativeUrl);

    /**
     * Get a string representing the current URL that the browser is looking at,
     * without the base-url.
     *
     * @return The URL of the page currently loaded in the browser, without the
     *         base-url.
     */
    String getCurrentRelativeUrl();

}
