/**
 *
 */
package de.ppi.selenium.junit;

/**
 * WebServer which delegates to a real webserver or use a running.
 *
 * @author niels
 *
 */
public class DelegatingWebServer implements WebServer {

    /**
     * The base-url.
     */
    private static final String BASE_URL = System
            .getProperty("webtest.baseurl");

    /**
     * the webserver which do the real job.
     */
    private final WebServer realWebServer;

    /**
     * Creates a new delegating webserver.
     * 
     * @param realWebServer webserver which do the real job.
     */
    public DelegatingWebServer(WebServer realWebServer) {
        super();
        this.realWebServer = realWebServer;
    }

    @Override
    public void start() throws Exception {
        if (BASE_URL == null) {
            realWebServer.start();
        }
    }

    @Override
    public boolean isRunning() {
        if (BASE_URL == null) {
            return realWebServer.isRunning();
        } else {
            return true;
        }
    }

    @Override
    public void stop() throws Exception {
        if (BASE_URL == null) {
            realWebServer.stop();
        }
    }

    @Override
    public String getBaseUrl() {
        if (BASE_URL == null) {
            return realWebServer.getBaseUrl();
        } else {
            return BASE_URL;
        }

    }

}
