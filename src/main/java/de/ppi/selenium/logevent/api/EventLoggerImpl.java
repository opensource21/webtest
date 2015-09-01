package de.ppi.selenium.logevent.api;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ppi.selenium.browser.SessionManager;

/**
 * Real implementation of a {@link EventLogger}.
 *
 */
public class EventLoggerImpl implements EventLogger {

    /**
     * The Logger.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(EventLoggerImpl.class);

    /**
     * Set of Assertions which are logged.
     */
    private static final Set<AssertionError> LOGGED_ASSERTION_ERRORS =
            Collections.synchronizedSet(new HashSet<AssertionError>());

    /**
     * System to store the events.
     */
    private final EventStorage eventStorage;

    /**
     * Container for the event-data.
     */
    private final EventData eventData = new EventData();

    /**
     * The priority for the screenshot if it lower than the normal priority.
     */
    private final Priority screenshotPriorityLevel;

    /**
     * Initiates an object of type EventLoggerImpl.
     *
     * @param eventStorage the storage-system.
     * @param testrunId Id of the test run.
     * @param priority priority which with it should be logged.
     * @param screenshotPriorityLevel priority at which level a screenshot
     *            should be made.
     * @param source the source of the event.
     * @param group the group
     * @param item the item.
     */
    public EventLoggerImpl(EventStorage eventStorage, String testrunId,
            Priority priority, Priority screenshotPriorityLevel,
            EventSource source, String group, String item) {
        super();
        if (eventStorage == null) {
            throw new IllegalStateException("EventStorage can't be null.");
        }
        this.eventStorage = eventStorage;
        eventData.setTestrunId(testrunId);
        eventData.setSource(source);
        eventData.setGroupId(group);
        eventData.setItem(item);
        eventData.setPriority(priority);
        this.screenshotPriorityLevel = screenshotPriorityLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventLogger withScreenshot(Priority prio, WebDriver webDriver) {
        // TODO assertion nicht doppelt loggen. Eigene Funktion bauen.
        if (prio.isMoreImportantThan(screenshotPriorityLevel)) {
            try {
                WebDriver wrappedDriver = webDriver;
                while (wrappedDriver instanceof WrapsDriver) {
                    wrappedDriver =
                            ((WrapsDriver) wrappedDriver).getWrappedDriver();
                }
                if (wrappedDriver instanceof TakesScreenshot) {
                    eventData.setScreenshot(((TakesScreenshot) wrappedDriver)
                            .getScreenshotAs(OutputType.BYTES));
                    eventData.setScreenShotType("png");
                } else if (wrappedDriver instanceof HtmlUnitDriver) {
                    eventData.setScreenShotType("html");
                    eventData.setScreenshot(wrappedDriver.getPageSource()
                            .getBytes("UTF-8"));
                } else {
                    LOG.warn("The current driver doesn't make screenshots");
                }
            } catch (IOException e) {
                LOG.error("IO-Error during creating of the screenshot ", e);
            }

        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String action, String message) {
        log(action, message, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String action, String message, Object argument1) {
        log(action, message, argument1, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String action, String message, Object argument1,
            Object argument2) {
        log(action, message, argument1, argument2, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String action, String message, Object argument1,
            Object argument2, Object argument3) {
        log(action, message, argument1, argument2, argument3, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(String action, String message, Object argument1,
            Object argument2, Object argument3, Object argument4) {
        eventData.setTs(new Timestamp(System.currentTimeMillis()));
        eventData.setThreadId(Thread.currentThread().getId());
        eventData.setAction(action);
        eventData.setDescription(message);
        eventData.setArgument1(argument1);
        eventData.setArgument2(argument2);
        eventData.setArgument3(argument3);
        eventData.setArgument4(argument4);
        eventStorage.insert(eventData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logAssertionError(AssertionError assertionError) {
        if (Priority.FAILURE.isMoreImportantThan(screenshotPriorityLevel)) {
            if (LOGGED_ASSERTION_ERRORS.add(assertionError)) {
                this.withScreenshot(Priority.FAILURE,
                        SessionManager.getSession()).log(
                        EventActions.ASSERTION_FAILED, "assertion.failed",
                        assertionError.getLocalizedMessage());
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willLogged() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willScreenshotLogged(Priority priority) {
        return priority.isMoreImportantThan(screenshotPriorityLevel);
    }
}
