package de.ppi.selenium.junit;

import de.ppi.selenium.logevent.api.EventLogger;
import de.ppi.selenium.logevent.api.EventLoggerFactory;
import de.ppi.selenium.logevent.api.EventSource;
import de.ppi.selenium.logevent.api.Priority;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.WebDriver;

/**
 * Rule, which provides the right Eventlogger on level doku.
 */
public class EventLoggerRule extends TestWatcher implements EventLogger {
    /**
     * Eventlogger-Factory.
     */
    private static final EventLoggerFactory EVENT_LOGGER = EventLoggerFactory
            .getInstance(EventSource.TEST);

    /** Logger on {@link Priority#DEBUG}. */
    private EventLogger debugEventLogger;
    /** Logger on {@link Priority#DOCUMENTATION}. */
    private EventLogger dokuEventLogger;
    /** Logger on {@link Priority#FAILURE}. */
    private EventLogger failureEventLogger;
    /** Logger on {@link Priority#EXCEPTION}. */
    private EventLogger exceptionEventLogger;

    /** The JUnit {@link Description}. */
    private Description description;

    @Override
    protected void starting(Description d) {
        this.description = d;
    }

    @Override
    protected void finished(Description description) {
        super.finished(description);
        debugEventLogger = null;
        dokuEventLogger = null;
        failureEventLogger = null;
        exceptionEventLogger = null;
    }

    /**
     * Deliver a eventlogger with {@link Priority#DEBUG}.
     *
     * @return a eventlogger with {@link Priority#DEBUG}.
     */
    public EventLogger onDebug() {
        if (debugEventLogger == null) {
            debugEventLogger =
                    EVENT_LOGGER.onDebug(description.getClassName(),
                            description.getMethodName());
        }
        return debugEventLogger;
    }

    /**
     * Deliver a eventlogger with {@link Priority#DOCUMENTATION}.
     *
     * @return a eventlogger with {@link Priority#DOCUMENTATION}.
     */
    public EventLogger onDoku() {
        if (dokuEventLogger == null) {
            dokuEventLogger =
                    EVENT_LOGGER.onDoku(description.getClassName(),
                            description.getMethodName());
        }
        return dokuEventLogger;
    }

    /**
     * Deliver a eventlogger with {@link Priority#FAILURE}.
     *
     * @return a eventlogger with {@link Priority#FAILURE}.
     */
    public EventLogger onFailure() {
        if (failureEventLogger == null) {
            failureEventLogger =
                    EVENT_LOGGER.onFailure(description.getClassName(),
                            description.getMethodName());
        }
        return failureEventLogger;
    }

    /**
     * Deliver a eventlogger with {@link Priority#EXCEPTION}.
     *
     * @return a eventlogger with {@link Priority#EXCEPTION}.
     */
    public EventLogger onException() {
        if (exceptionEventLogger == null) {
            exceptionEventLogger =
                    EVENT_LOGGER.onException(description.getClassName(),
                            description.getMethodName());
        }
        return exceptionEventLogger;
    }

    @Override
    public void logAssertionError(AssertionError assertionError) {
        onDoku().logAssertionError(assertionError);
    }

    @Override
    public void logThrowable(Throwable exception) {
        onDoku().logThrowable(exception);
    }

    @Override
    public EventLogger withScreenshot(Priority prio, WebDriver webDriver) {
        return onDoku().withScreenshot(prio, webDriver);
    }

    @Override
    public void log(String action, String message) {
        onDoku().log(action, message);
    }

    @Override
    public void log(String action, String message, Object argument1) {
        onDoku().log(action, message, argument1);
    }

    @Override
    public void log(String action, String message, Object argument1,
            Object argument2) {
        onDoku().log(action, message, argument1, argument2);
    }

    @Override
    public void log(String action, String message, Object argument1,
            Object argument2, Object argument3) {
        onDoku().log(action, message, argument1, argument2, argument3);
    }

    @Override
    public void log(String action, String message, Object argument1,
            Object argument2, Object argument3, Object argument4) {
        onDoku().log(action, message, argument1, argument2, argument3,
                argument4);
    }

    @Override
    public boolean willLogged() {
        return onDoku().willLogged();
    }

    @Override
    public boolean willScreenshotLogged(Priority priority) {
        return onDoku().willScreenshotLogged(priority);
    }
}
