package de.ppi.selenium.logevent.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Possible Priorities.
 *
 */
public enum Priority {

    DEBUG(10), DOKU(20), FAILURE(30), EXCEPTION(40);

    private final int prio;

    private Priority(int prio) {
        this.prio = prio;
    }

    public boolean isMoreImportantThan(Priority other) {
        return this.prio >= other.prio;
    }
}
