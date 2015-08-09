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
    public void logAssertionError(AssertionError assertionError) {
        // DO NOTHING
    }

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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willLogged() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willScreenshotLogged(Priority priority) {
        return false;
    }

}
