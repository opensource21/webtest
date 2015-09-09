package de.ppi.selenium.logevent.backend;

import org.sql2o.Connection;

import de.ppi.selenium.logevent.api.EventStorage;

/**
 * {@link EventStorage} which use H2-Database.
 *
 */
// TODO PriorityConverter so that the int value can used.
public class H2EventStorage extends Sql2oEventStorage {

    /**
     * Initiates an object of type H2EventStorage.
     *
     * @param connectURL connectURL
     * @param user the user
     * @param password the password.
     */
    public H2EventStorage(String connectURL, String user, String password) {
        super(connectURL, user, password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void createTable(Connection connection) {
        connection
                .createQuery(
                        "CREATE TABLE IF NOT EXISTS EVENTS (id IDENTITY,  ts TIMESTAMP NOT NULL, "
                                + "testrunid VARCHAR(100) NOT NULL, "
                                + "threadid BIGINT NOT NULL, source VARCHAR(30) NOT NULL, "
                                + "groupid VARCHAR(500) NOT NULL, item VARCHAR(500) NOT NULL, "
                                + "action VARCHAR(500) NOT NULL, priority VARCHAR(20) NOT NULL, "
                                + "description VARCHAR(5000) NOT NULL, argument1 VARCHAR(5000), "
                                + "argument2 VARCHAR(5000), argument3 VARCHAR(5000), "
                                + "argument4 VARCHAR(5000), screenShotType VARCHAR(20), "
                                + "screenshot BINARY)").executeUpdate();
    }
}
