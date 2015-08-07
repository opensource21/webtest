package de.ppi.selenium.logevent.api;

import org.openqa.selenium.WebElement;

/**
 * Source of the event.
 *
 */
public enum EventSource {
    /**
     * Event comes form a {@link WebElement}.
     */
    WEBELEMENT,
    /** Event comes from a widget. */
    WIDGET,
    /** Event comes from a fragment. */
    FRAGMENT,
    /** Event comes from a page. */
    PAGE,
    /** Event comes from a webdriver or browser. */
    WEBDRIVER,
    /** Event comes from a assertion. */
    ASSERTION,
    /** Event comes from a test. */
    TEST;

}
