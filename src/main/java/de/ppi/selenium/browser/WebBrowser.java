/**
 * 
 */
package de.ppi.selenium.browser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.internal.WrapsDriver;

/**
 * Access to the web-browser.
 * @author niels
 *
 */
public interface WebBrowser extends WebDriver, WrapsDriver {
    
    String getSessionId();

    
}
