package de.ppi.selenium.junit;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import de.ppi.selenium.browser.SessionManager;
import de.ppi.selenium.logevent.api.EventActions;
import de.ppi.selenium.logevent.api.EventLogger;
import de.ppi.selenium.logevent.api.EventLoggerFactory;
import de.ppi.selenium.logevent.api.EventSource;
import de.ppi.selenium.logevent.api.EventStorage;
import de.ppi.selenium.logevent.api.Priority;

/**
 * Junit-Rule with put information about the test to the eventlog.
 *
 */
public class EventLogRule implements TestRule {

    /**
     * The factory for {@link EventLogger}.
     */
    private static final EventLoggerFactory EVENT_LOGGER_FACTORY =
            EventLoggerFactory.getInstance(EventSource.TEST);

    /** The storage system. */
    private final EventStorage eventStorage;

    /**
     * Initiates an object of type EventLogRule.
     *
     * @param storage a {@link EventStorage}.
     */
    public EventLogRule(EventStorage storage) {
        EventLoggerFactory.setStorage(storage);
        this.eventStorage = storage;
        // TODO start mit Testrun und close ermöglichen.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final String group = description.getClassName();
                final String item = description.getMethodName();
                final String displayName = description.getDisplayName();

                eventStorage.startBatch();
                EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                        EventActions.TEST_START, "test.start", displayName);

                try {
                    base.evaluate();
                    EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                            EventActions.TEST_FINISHED, "test.success",
                            displayName);
                } catch (AssumptionViolatedException e) {
                    EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                            EventActions.TEST_SKIPPED, "test.skipped ",
                            displayName);
                } catch (Throwable e) {
                    if (e instanceof AssertionError) {
                        EVENT_LOGGER_FACTORY.onFailure(group, item)
                                .logAssertionError((AssertionError) e);
                        EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                                EventActions.TEST_FINISHED_WITH_FAILURES,
                                "test.failures", Integer.valueOf(1),
                                displayName);
                    } else if (e instanceof MultipleFailureException) {
                        final int nrOfAssertions =
                                ((MultipleFailureException) e).getFailures()
                                        .size();
                        EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                                EventActions.TEST_FINISHED_WITH_FAILURES,
                                "test.failures", displayName,
                                Integer.valueOf(nrOfAssertions));
                    } else {
                        EVENT_LOGGER_FACTORY
                                .onException(group, item)
                                .withScreenshot(Priority.EXCEPTION,
                                        SessionManager.getSession())
                                .log(EventActions.EXCEPTION_OCCURS,
                                        "test.exception_occurs", displayName,
                                        e.getLocalizedMessage());
                        EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                                EventActions.TEST_FINISHED_WITH_EXCEPTION,
                                "test.exception", displayName,
                                e.getLocalizedMessage());

                    }
                } finally {
                    eventStorage.write();
                }

            }
        };
    }
}