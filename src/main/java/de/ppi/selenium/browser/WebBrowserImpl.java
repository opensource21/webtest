/**
 *
 */
package de.ppi.selenium.browser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ppi.selenium.logevent.api.EventActions;
import de.ppi.selenium.logevent.api.EventLoggerFactory;
import de.ppi.selenium.logevent.api.EventSource;
import de.ppi.selenium.logevent.api.Priority;

/**
 * Concrete web-browser
 *
 * @author niels
 *
 */
// TODO LOG alle Events loggen
public class WebBrowserImpl implements WebBrowser {

    public static final Logger LOG = LoggerFactory
            .getLogger(WebBrowserImpl.class);

    /**
     * EventLogger for after events.
     */
    private static final EventLoggerFactory AFTER_EVENTLOGGER =
            EventLoggerFactory.getInstance(EventSource.WEBDRIVER_AFTER);

    /**
     * Eventlogger for before events.
     */
    private static final EventLoggerFactory BEFORE_EVENTLOGGER =
            EventLoggerFactory.getInstance(EventSource.WEBDRIVER_BEFORE);

    private static final List<WebBrowser> ALL_INSTANCES = new ArrayList<>();

    private final WebDriver webdriver;

    private final String sessionId;

    private String baseUrl;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                final List<WebBrowser> allBrowsers =
                        new ArrayList<>(ALL_INSTANCES);
                for (WebBrowser webBrowser : allBrowsers) {
                    try {
                        webBrowser.quit();
                    } catch (UnreachableBrowserException ube) {
                        // no problem.
                    } catch (Exception e) {
                        LOG.warn(
                                "Problem to shutdown a browser ("
                                        + webBrowser.getSessionId() + ")", e);
                    }
                }
                super.run();
            }

        });
    }

    /**
     * Creates a new browser-session.
     *
     * @param webdriver the native webdriver
     * @param sessionId the id of the session to get the webbrowser.
     * @param baseUrl the basis url, where all other are relative to.
     */
    public WebBrowserImpl(WebDriver webdriver, String sessionId, String baseUrl) {
        super();
        ALL_INSTANCES.add(this);
        this.sessionId = sessionId;
        this.webdriver = webdriver;
        setBaseUrl(baseUrl);
    }

    /**
     * @return the sessionId
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        if (baseUrl != null && baseUrl.endsWith("/")) {
            this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
            this.baseUrl = baseUrl;
        }
    }

    /**
     * @param url
     * @see org.openqa.selenium.WebDriver#get(java.lang.String)
     */
    @Override
    public void get(String url) {
        BEFORE_EVENTLOGGER.onDebug(WebBrowserImpl.class.getSimpleName(), "get")
                .withScreenshot(Priority.DEBUG, webdriver)
                .log(EventActions.WEBDRIVER_GET, "webdriver.get", url);
        try {
            webdriver.get(url);
        } catch (RuntimeException re) {
            AFTER_EVENTLOGGER.onException(WebBrowserImpl.class.getSimpleName(),
                    "get").logThrowable(re);
            throw re;
        }
        AFTER_EVENTLOGGER.onDebug(WebBrowserImpl.class.getSimpleName(), "get")
                .withScreenshot(Priority.DEBUG, webdriver)
                .log(EventActions.WEBDRIVER_GET, "webdriver.get", url);
    }

    /**
     * Call the relative url.
     *
     * @param relativeUrl the relative url.
     */
    @Override
    public void getRelativeUrl(String relativeUrl) {
        get(getBaseUrl() + relativeUrl);
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getCurrentUrl()
     */
    @Override
    public String getCurrentUrl() {
        return webdriver.getCurrentUrl();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getTitle()
     */
    @Override
    public String getTitle() {
        return webdriver.getTitle();
    }

    /**
     * @param by
     * @return
     * @see org.openqa.selenium.WebDriver#findElements(org.openqa.selenium.By)
     */
    @Override
    public List<WebElement> findElements(By by) {
        return webdriver.findElements(by);
    }

    /**
     * @param by
     * @return
     * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
     */
    @Override
    public WebElement findElement(By by) {
        return webdriver.findElement(by);
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getPageSource()
     */
    @Override
    public String getPageSource() {
        return webdriver.getPageSource();
    }

    /**
     *
     * @see org.openqa.selenium.WebDriver#close()
     */
    @Override
    public void close() {
        webdriver.close();
    }

    /**
     *
     * @see org.openqa.selenium.WebDriver#quit()
     */
    @Override
    public void quit() {
        ALL_INSTANCES.remove(this);
        webdriver.quit();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getWindowHandles()
     */
    @Override
    public Set<String> getWindowHandles() {
        return webdriver.getWindowHandles();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getWindowHandle()
     */
    @Override
    public String getWindowHandle() {
        return webdriver.getWindowHandle();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#switchTo()
     */
    @Override
    public TargetLocator switchTo() {
        return webdriver.switchTo();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#navigate()
     */
    @Override
    public Navigation navigate() {
        return webdriver.navigate();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#manage()
     */
    @Override
    public Options manage() {
        return webdriver.manage();
    }

    @Override
    public WebDriver getWrappedDriver() {
        return webdriver;
    }

    @Override
    public String getCurrentRelativeUrl() {
        final String currentUrl = getCurrentUrl();
        if (currentUrl.startsWith(baseUrl)) {
            return currentUrl.substring(baseUrl.length());
        } else {
            return currentUrl;
        }
    }

}
