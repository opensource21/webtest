package de.ppi.selenium.logevent.api;

import org.openqa.selenium.WebDriver;

/**
 * Object to log events.
 *
 */
public interface EventLogger {

    /**
     * Define that the log should be done with screenshot if priority higher
     * then the threshold.
     *
     * @param prio priority for the screenshot.
     * @param webDriver the webdriver.
     * @return the EventLogger.
     */
    EventLogger withScreenshot(Priority prio, WebDriver webDriver);

    /**
     * Logs the event.
     *
     * @param action the action.
     * @param description the description.
     */
    void log(String action, String description);

}
