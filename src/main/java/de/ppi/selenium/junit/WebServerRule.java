package de.ppi.selenium.junit;

import org.junit.rules.ExternalResource;

import de.ppi.selenium.browser.SessionManager;

/**
 * Junit-Rule to start a webserver.
 *
 */
public class WebServerRule extends ExternalResource {

    /**
     * The webserver.
     */
    private final WebServer webServer;

    /**
     *
     * Initiates an object of type WebServerRule.
     * 
     * @param webServer the webserver.
     */
    public WebServerRule(WebServer webServer) {
        this.webServer = webServer;
        SessionManager.getInstance().setDefaultBaseUrl(webServer.getBaseUrl());
    }

    @Override
    protected void before() throws Throwable {
        if (!webServer.isRunning()) {
            webServer.start();
        }
    }

}
