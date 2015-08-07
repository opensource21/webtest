package de.ppi.selenium.logevent.api;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Real implementation of a {@link EventLogger}.
 *
 */
public class EventLoggerImpl extends EventData implements EventLogger {

    /**
     * The Logger.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(EventLoggerImpl.class);

    /**
     * System to store the events.
     */
    private final EventStorage eventStorage;

    /**
     * The priority for the screenshot if it lower than the normal priority.
     */
    private final Priority screenshotPriorityLevel;

    /**
     * Initiates an object of type EventLoggerImpl.
     *
     * @param eventStorage the storage-system.
     * @param priority priority which with it should be logged.
     * @param screenshotPriorityLevel priority at which level a screenshot
     *            should be made.
     * @param source the source of the event.
     * @param group the group
     * @param item the item.
     */
    public EventLoggerImpl(EventStorage eventStorage, Priority priority,
            Priority screenshotPriorityLevel, EventSource source, String group,
            String item) {
        super();
        this.eventStorage = eventStorage;
        this.setSource(source);
        this.setGroup(group);
        this.setItem(item);
        this.setPriority(priority);
        this.screenshotPriorityLevel = screenshotPriorityLevel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventLogger withScreenshot(Priority prio, WebDriver webDriver) {
        if (prio.isMoreImportantThan(screenshotPriorityLevel)) {
            try {
                WebDriver wrappedDriver = webDriver;
                while (wrappedDriver instanceof WrapsDriver) {
                    wrappedDriver =
                            ((WrapsDriver) wrappedDriver).getWrappedDriver();
                }
                if (wrappedDriver instanceof TakesScreenshot) {
                    setScreenshot(((TakesScreenshot) wrappedDriver)
                            .getScreenshotAs(OutputType.BYTES));
                    setScreenShotType("png");
                } else if (wrappedDriver instanceof HtmlUnitDriver) {
                    setScreenShotType("html");
                    setScreenshot(wrappedDriver.getPageSource().getBytes(
                            "UTF-8"));
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
    public void log(String action, String description) {
        this.setAction(action);
        this.setDescription(description);
        eventStorage.insert(this);

    }

}
