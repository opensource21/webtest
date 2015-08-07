package de.ppi.selenium.logevent.api;

import org.openqa.selenium.WebDriver;

/**
 * {@link EventLogger} with do nothing.
 *
 */
public class EmptyLogger implements EventLogger {

    /**
     * {@inheritDoc}
     */
    @Override
    public EventLogger withScreenshot(Priority prio, WebDriver webDriver) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String action, String description) {
        // DO NOTHING.
    }

}
