package de.ppi.selenium.logevent.api;

import org.openqa.selenium.WebDriver;

/**
 * Object to log events.
 *
 */
public interface EventLogger {

    /**
     * Log an assertion-error, with a screenshot. Priority is always
     * {@link Priority#FAILURE}.
     *
     * @param assertionError the assertionError.
     */
    void logAssertionError(AssertionError assertionError);

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
     * @param action the action @see {@link EventActions}.
     * @param message the message.
     * @param args argument for the message.
     */
    void log(String action, String message, Object... args);

    /**
     * Return if a message will be logged.
     *
     * @return true if a message will be logged.
     */
    boolean willLogged();

    /**
     * Return if a screenshot will created.
     *
     * @param priority the priority which with the screenshot should be created.
     * @return true a screenshot will created.
     */
    boolean willScreenshotLogged(Priority priority);
}
