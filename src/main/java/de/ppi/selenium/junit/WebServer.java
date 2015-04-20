package de.ppi.selenium.junit;

/**
 * Implementation of a WebServer or some information about an existing running
 * webserver.
 *
 */
public interface WebServer {

    /**
     * Starts the server.
     *
     * @throws Exception something goes wrong.
     */
    void start() throws Exception;

    /**
     * Checks if the server is running.
     *
     * @return true - if the server is started and not stopped.
     */
    boolean isRunning();

    /**
     * Stop the server.
     *
     * @throws Exception stop the server.
     */
    void stop() throws Exception;

    /**
     * Return the base-url.
     *
     * @return the base-url.
     */
    String getBaseUrl();

}
