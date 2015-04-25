/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.ppi.selenium.browser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SessionManager for the testing framework. Uses a {@link ThreadLocal} so each
 * thread of test execution has its own manager instance. Based on
 * https://github.com/FINRAOS/JTAF-ExtWebDriver for the properties see
 * http://finraos.github.io/JTAF-ExtWebDriver/clientproperties.html.
 */
// TODO Exceptionhandling!
public class SessionManager {

    private final static Logger LOG = LoggerFactory
            .getLogger(SessionManager.class);

    /**
     * Key for the options to define a base-url.
     */
    public static final String BASE_URL_KEY = "BASE_URL_KEY";

    private Map<String, WebBrowser> sessions =
            new HashMap<String, WebBrowser>();

    private final static String DEFAULT_SESSION = "default";

    private final static int MAX_RETRIES = 5;

    private String defaultBaseUrl = System.getProperty(BASE_URL_KEY);

    private String currentSessionId = DEFAULT_SESSION;
    private int nextCustomSessionId = 1;

    private boolean doCleanup = true;

    private SessionManager() {

    }

    private static ThreadLocal<SessionManager> sessionManager =
            new ThreadLocal<SessionManager>() {
                @Override
                protected synchronized SessionManager initialValue() {
                    return new SessionManager();
                }
            };

    private static ThreadLocal<WebDriverFactory> WebDriverFactory =
            new ThreadLocal<WebDriverFactory>() {
                @Override
                protected synchronized WebDriverFactory initialValue() {
                    return new DefaultWebDriverFactory();
                }
            };

    /**
     * Obtain the ThreadLocal instance of SessionManager. Configures the
     * instance to use DefaultWebDriverFactory()
     *
     * @return SessionManager, the ThreadLocal instance of SessionManager
     *
     * @see setWebDriverFactory
     */

    public static SessionManager getInstance() {
        return sessionManager.get();
    }

    /**
     * Get the default base-url.
     * 
     * @return the default base-url.
     */
    public String getDefaultBaseUrl() {
        return defaultBaseUrl;
    }

    /**
     * Possibility to set the default base-url.
     * 
     * @param defaultBaseUrl the default base-url.
     */
    public void setDefaultBaseUrl(String defaultBaseUrl) {
        if (this.defaultBaseUrl == null) {
            for (WebBrowser webBrowser : getSessions().values()) {
                ((WebBrowserImpl) webBrowser).setBaseUrl(defaultBaseUrl);
            }
        }
        this.defaultBaseUrl = defaultBaseUrl;
    }

    /**
     * Configure the current instance of SessionManager to use the given
     * WebDriverFactory instance as its WebDriverFactory, returning the newly
     * configured instance. With this method, obtaining a SessionManager
     * configured to use a custom WebDriverFactory can be done with
     * SessionManager.getInstance().setWebDriverFactory(new
     * ustomWebDriverFactory()).
     *
     * @param impl a WebDriverFactory instance to be associated with this
     *            manager
     * @return SessionManager
     * @see getInstance
     */

    public SessionManager setWebDriverFactory(WebDriverFactory impl) {
        WebDriverFactory.set(impl);
        return this;
    }

    /**
     * Get the current session associated with this thread. Because a
     * SessionManager instance is thread-local, the notion of current is also
     * specific to a thread.
     *
     *
     * @param createIfNotFound set to true if a session should be created if no
     *            session is associated with the current sessionId
     * @return WebBrowser an WebBrowser instance
     * @see getCurrentSession(), getSession(String), switchToDefaultSession(),
     *      switchToSession(String)
     */

    public WebBrowser getCurrentSession(boolean createIfNotFound) {

        for (int i = 0; i < MAX_RETRIES; i++) {
            WebBrowser sel = sessions.get(currentSessionId);
            try {
                if ((sel == null) && (createIfNotFound)) {
                    sel = getNewSession();
                }
                return sel;
            } catch (UnreachableBrowserException e) {
                LOG.info("Couldn't reach Browser", e);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Problem to create instance: "
                                + e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    /**
     * Convenience method for getting the current WebBrowser session associated
     * with this SessionManager, creating a new session if the session does not
     * exist.
     *
     * @return WebBrowser an instance of WebBrowser
     */

    public WebBrowser getCurrentSession() {
        return getCurrentSession(true);
    }

    /**
     * Convenience method to get teh current session.
     * 
     * @return the current session.
     */
    public static WebBrowser getSession() {
        return getInstance().getCurrentSession();
    }

    /**
     * Get an existing WebBrowser session with the given ID.
     *
     * @param sessionId
     * @return
     */

    public WebBrowser getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * Get the Map of all WebBrowser sessions associated with this
     * SessionManager instance.
     *
     * @return
     */

    public Map<String, WebBrowser> getSessions() {
        return sessions;
    }

    /**
     * Switch the current session to be the WebBrowser session with the given
     * ID. A session with this ID should have already been previously created.
     *
     * @param sessionId
     */

    public void switchToSession(String sessionId) {
        currentSessionId = sessionId;
    }

    /**
     * Switch the current session to be the provided WebBrowser instance. This
     * method assumes that the provided session was created within the scope of
     * the current thread (since session IDs are only required to be
     * thread-local, as are sessions themselves).
     *
     * @param sessionId
     */

    public void switchToSession(WebBrowser ewd) {
        currentSessionId = ewd.getSessionId();
    }

    /**
     * Get the ID of the current WebBrowser session associated with this
     * SessionManager
     *
     * @return
     */
    public String getCurrentSessionId() {
        return currentSessionId;
    }

    /**
     * Remove a session with the given ID from this SessionManager
     *
     * @param sessionId the ID of the session to be removed
     */

    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * Remove the given session from SessionManager based on its stored ID. Note
     * that the session must have been created with this same thread as sessions
     * and unique IDs are only required to be thread-local and not global.
     *
     * @param session the WebBrowser session to be removed
     */

    public void removeSession(WebBrowser session) {
        sessions.remove(session.getSessionId());
    }

    /**
     * Create and return a new WebBrowser session with default options, and set
     * it as the current session for this SessionManager.
     *
     * @return A new WebBrowser instance with auto-generated ID. You can obtain
     *         the Session ID with WebBrowser.getSessionId().
     * @throws Exception
     */

    public WebBrowser getNewSession() throws Exception {
        return getNewSession(true);
    }

    /**
     * Create and return new WebBrowser instance with default options. If
     * setAsCurrent is true, set the new session as the current session for this
     * SessionManager.
     *
     * @param setAsCurrent set to true if the new session should become the
     *            current session for this SessionManager
     * @return A new WebBrowser session
     * @throws Exception
     */

    public WebBrowser getNewSession(boolean setAsCurrent) throws Exception {
        Map<String, String> options =
                WebDriverFactory.get().createDefaultOptions();
        return getNewSessionDo(options, setAsCurrent);
    }

    /**
     * Create and return a new WebBrowser instance. The instance is constructed
     * with default options, with the provided key/value pair overriding the
     * corresponding key and value in the options, and will become the current
     * session. This is a convenience method for use when only a single option
     * needs to be overridden. If overriding multiple options, you must use
     * getNewSession(Map<String, String>, boolean) instead.
     *
     * @param key The key whose default value will be overridden
     * @param value The value to be associated with the provided key
     * @return A new WebBrowser instance which is now the current session
     * @throws Exception
     */

    public WebBrowser getNewSession(String key, String value) throws Exception {
        return getNewSession(key, value, true);
    }

    /**
     * Create and return a new WebBrowser instance. The instance is constructed
     * with default options, with the provided key/value pair overriding the
     * corresponding key and value in the options. This is a convenience method
     * for use when only a single option needs to be overridden. If overriding
     * multiple options, you must use getNewSession(Map<String, String>,
     * boolean) instead.
     *
     * @param key The key whose default value will be overridden
     * @param value The value to be associated with the provided key
     * @param setAsCurrent set to true if the new session should become the
     *            current session for this SessionManager
     * @return A new WebBrowser instance
     * @throws Exception
     */

    public WebBrowser getNewSession(String key, String value,
            boolean setAsCurrent) throws Exception {

        /**
         * This is where the clientPropertiesFile is parsed and key-value pairs
         * are added into the options map
         */
        Map<String, String> options =
                WebDriverFactory.get().createDefaultOptions();
        options.put(key, value);

        return getNewSessionDo(options, setAsCurrent);
    }

    /**
     * Create and return a new WebBrowser instance. The instance is constructed
     * with default options, with the provided Map of key/value pairs overriding
     * the corresponding pairs in the options. This new WebBrowser instance will
     * then become the current session.
     *
     * @param override A Map of options to be overridden
     * @return A new WebBrowser instance which is now the current session
     * @throws Exception
     */

    public WebBrowser getNewSession(Map<String, String> override)
            throws Exception {
        return getNewSession(override, true);
    }

    /**
     * Create and return a new WebBrowser instance. The instance is constructed
     * with default options, with the provided Map of key/value pairs overriding
     * the corresponding pairs in the options.
     *
     * @param override A Map of options to be overridden
     * @param setAsCurrent set to true if the new session should become the
     *            current session for this SessionManager
     * @return A new WebBrowser instance
     * @throws Exception
     */

    public WebBrowser getNewSession(Map<String, String> override,
            boolean setAsCurrent) throws Exception {

        Map<String, String> options =
                WebDriverFactory.get().createDefaultOptions();

        for (Entry<String, String> opt : override.entrySet()) {
            options.put(opt.getKey(), opt.getValue());
        }

        return getNewSessionDo(options, setAsCurrent);
    }

    private WebBrowser getNewSessionDo(Map<String, String> options,
            boolean setAsCurrent) throws Exception {

        final Map<String, String> localOptions = new HashMap<>(options);

        final String baseUrl;
        if (localOptions.containsKey(BASE_URL_KEY)) {
            baseUrl = localOptions.get(BASE_URL_KEY);
            localOptions.remove(BASE_URL_KEY);
        } else {
            baseUrl = defaultBaseUrl;
        }

        if (doCleanup) {
            WebDriverFactory.get().cleanup(localOptions);
            doCleanup = false;
        }

        // Get capabilities
        DesiredCapabilities dc =
                WebDriverFactory.get().createCapabilities(localOptions);

        // Get driver instance
        WebDriver innerDriver =
                WebDriverFactory.get().createWebDriver(localOptions, dc);

        String sessionId = getNextCustomSessionId();
        if (setAsCurrent) {
            currentSessionId = sessionId;
        }

        final WebBrowser webBrowser =
                new WebBrowserImpl(innerDriver, sessionId, baseUrl);
        // Store the session in sessions Map
        sessions.put(sessionId, webBrowser);

        return webBrowser;
    }

    /**
     *
     * @return String of the next session Id
     */
    private String getNextCustomSessionId() {
        String id = "custom_" + nextCustomSessionId;
        nextCustomSessionId++;
        return id;
    }

    public void quitAllSessions() {
        for (WebBrowser webBrowser : getSessions().values()) {
            webBrowser.quit();
        }
    }

}
