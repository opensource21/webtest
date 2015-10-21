package de.ppi.selenium.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2oException;

import de.ppi.selenium.logevent.api.EventActions;
import de.ppi.selenium.logevent.api.EventLogger;
import de.ppi.selenium.logevent.api.EventLoggerFactory;
import de.ppi.selenium.logevent.api.EventSource;
import de.ppi.selenium.logevent.api.EventStorage;
import de.ppi.selenium.logevent.report.LogReporter;

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

    /** Logger instance. */
    private static final Logger LOG = LoggerFactory
            .getLogger(EventLogRule.class);

    /** The storage system. */
    private final EventStorage eventStorage;

    /**
     * Initiates an object of type EventLogRule.
     *
     * @param storage a {@link EventStorage}.
     * @param reporter a list of {@link LogReporter}.
     */
    public EventLogRule(final EventStorage storage,
            final LogReporter... reporter) {
        EventLoggerFactory.setStorage(storage);
        this.eventStorage = storage;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    eventStorage.close();
                } catch (Sql2oException sql2oException) {
                    LOG.error("Error closing the eventstorage.", sql2oException);
                }
                for (LogReporter logReporter : reporter) {
                    logReporter.createReport(storage,
                            EventLoggerFactory.getTestrunId());
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final List<Throwable> errors = new ArrayList<Throwable>();
                final String group = description.getClassName();
                final String item =
                        description.getMethodName() == null ? "no-method"
                                : description.getMethodName();
                final String displayName = description.getDisplayName();

                EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                        EventActions.TEST_START, "test.start", displayName);

                try {
                    base.evaluate();
                    EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                            EventActions.TEST_FINISHED, "test.success",
                            displayName);
                } catch (AssumptionViolatedException e) {
                    errors.add(e);
                    EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                            EventActions.TEST_SKIPPED, "test.skipped ",
                            displayName);
                } catch (Throwable e) {
                    errors.add(e);
                    try {
                        if (e instanceof AssertionError) {
                            EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                                    EventActions.TEST_FINISHED_WITH_FAILURES,
                                    "test.failures", displayName,
                                    Integer.valueOf(1));
                        } else if (e instanceof MultipleFailureException) {
                            final int nrOfAssertions =
                                    ((MultipleFailureException) e)
                                            .getFailures().size();
                            EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                                    EventActions.TEST_FINISHED_WITH_FAILURES,
                                    "test.failures", displayName,
                                    Integer.valueOf(nrOfAssertions));
                        } else {
                            EVENT_LOGGER_FACTORY.onDoku(group, item).log(
                                    EventActions.TEST_FINISHED_WITH_EXCEPTION,
                                    "test.exception", displayName,
                                    e.getLocalizedMessage());
                        }
                    } catch (Exception iE) {
                        errors.add(iE);
                    }
                } finally {
                    eventStorage.write();
                }
                MultipleFailureException.assertEmpty(errors);
            }

        };
    }
}
