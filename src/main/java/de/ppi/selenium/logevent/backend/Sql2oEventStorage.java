package de.ppi.selenium.logevent.backend;

import java.util.Iterator;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.ResultSetIterable;
import org.sql2o.Sql2o;

import de.ppi.selenium.logevent.api.ClosableIterable;
import de.ppi.selenium.logevent.api.EventData;
import de.ppi.selenium.logevent.api.EventStorage;

/**
 * Abstract backend using Sql2o for storing the events.
 */
public abstract class Sql2oEventStorage implements EventStorage {

    /** The insert-statement. */
    public static final String INSERT_SQL =
            "INSERT INTO EVENTS(ts, testrunId, threadId, source, groupid, item, "
                    + "action, priority, description, argument1, argument2, "
                    + "argument3, argument4, screenShotType, screenshot)"
                    + "VALUES(:ts, :testrunId, :threadId, :source, :groupid, :item, "
                    + ":action, :priority, :description, :argument1, :argument2, "
                    + ":argument3, :argument4, :screenShotType, :screenshot)";

    /** The {@link Sql2o}-instance. */
    private final Sql2o sql2o;

    /** The connection. */
    private Connection connection = null;

    /**
     * The insert-query.
     */
    private Query insertQuery = null;

    /** Current batch-size. */
    private volatile int batchSize = 0;

    /** Maximal size of batch-data. */
    private static final int MAX_BATCH_SIZE = 100;

    /**
     * Initiates an object of type Sql2oEventStorage.
     *
     * @param connectURL the connection url.
     * @param user the user.
     * @param password the password.
     */
    public Sql2oEventStorage(String connectURL, String user, String password) {
        sql2o = new Sql2o(connectURL, user, password);
        connection = sql2o.beginTransaction();
        createTable(connection);
        insertQuery = connection.createQuery(INSERT_SQL);
    }

    /**
     * Create a table, where the table-name must be "EVENTS" and the columns
     * must be have the same name as the attributes!.
     *
     * @param connection a connection.
     */
    protected abstract void createTable(Connection connection);

    @Override
    public void insert(EventData event) {
        batchSize++;

        insertQuery.addParameter("testrunId", event.getTestrunId())
                .addParameter("ts", event.getTs())
                .addParameter("threadId", event.getThreadId())
                .addParameter("source", event.getSource())
                .addParameter("groupid", event.getGroupId())
                .addParameter("item", event.getItem())
                .addParameter("action", event.getAction())
                .addParameter("priority", event.getPriority())
                .addParameter("description", event.getDescription())
                .addParameter("argument1", event.getArgument1())
                .addParameter("argument2", event.getArgument2())
                .addParameter("argument3", event.getArgument3())
                .addParameter("argument4", event.getArgument4())
                .addParameter("screenShotType", event.getScreenShotType())
                .addParameter("screenshot", event.getScreenshot()).addToBatch();
        if (batchSize > MAX_BATCH_SIZE) {
            batchSize = 0;
            insertQuery.executeBatch();
        }
    }

    @Override
    public void write() {
        batchSize = 0;
        insertQuery.executeBatch();
        connection.commit(false);
    }

    @Override
    public void close() {
        connection.commit();
        if (insertQuery != null) {
            insertQuery.close();
            insertQuery = null;
        }
        connection.close();
        connection = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        if (connection == null) {
            connection = sql2o.beginTransaction();
            insertQuery = connection.createQuery(INSERT_SQL);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClosableIterable<EventData> getAllEvents(String testrunId) {
        final ResultSetIterable<EventData> result =
                sql2o.open()
                        .createQuery(
                                "select * from event_data where testrunId = :testrunId")
                        .addParameter("testrunId", testrunId)
                        .executeAndFetchLazy(EventData.class);
        result.setAutoCloseConnection(true);
        return new ClosableIterable<EventData>() {

            @Override
            public Iterator<EventData> iterator() {
                return result.iterator();
            }

            @Override
            public void close() throws Exception {
                result.close();
            }

        };
    }
}
