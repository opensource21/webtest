package de.ppi.selenium.junit;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;

import de.ppi.selenium.logevent.api.EventLogger;
import de.ppi.selenium.logevent.api.EventLoggerFactory;
import de.ppi.selenium.logevent.api.EventSource;

/**
 * Junit-Rule which creates a screenshot at errors.
 *
 * @author niels
 *
 */
public class ScreenshotAtErrorRule extends TestWatcher {

    /**
     * The factory for {@link EventLogger}.
     */
    private static final EventLoggerFactory EVENT_LOGGER_FACTORY =
            EventLoggerFactory.getInstance(EventSource.TEST);

    @Override
    protected void failed(Throwable e, Description description) {
        if (e instanceof MultipleFailureException) {
            return;
        }
        final String group = description.getClassName();
        final String item = description.getMethodName();
        if (e instanceof AssertionError) {
            EVENT_LOGGER_FACTORY.onFailure(group, item).logAssertionError(
                    (AssertionError) e);
        } else {
            EVENT_LOGGER_FACTORY.onException(group, item).logThrowable(e);
        }
    }
}
