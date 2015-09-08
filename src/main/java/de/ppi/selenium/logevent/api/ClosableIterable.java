package de.ppi.selenium.logevent.api;

/**
 * Iterable which must to be closeable, because at the end a ResultSet and
 * Connection must be close..
 * 
 * @param <T> the Type of Iterable.
 */
public interface ClosableIterable<T> extends Iterable<T>, AutoCloseable {
    // Collect 2 Interfaces.
}
