package org.selophane.elements;

import org.junit.rules.RuleChain;

import de.ppi.selenium.junit.EventLogRule;
import de.ppi.selenium.logevent.backend.H2EventStorage;

/**
 * Constants to init the log-system.
 *
 */
// CSOFFALL:
public interface WebTestConstants {
    /** The on instance of {@link EventLogRule}. */
    RuleChain EVENT_LOG_RULE = RuleChain.outerRule(
            new EventLogRule(new H2EventStorage(
                    "jdbc:h2:mem:test;MODE=PostgreSQL;DB_CLOSE_ON_EXIT=FALSE",
                    "", ""))).around(
            new de.ppi.selenium.junit.ScreenshotAtErrorRule());
}
