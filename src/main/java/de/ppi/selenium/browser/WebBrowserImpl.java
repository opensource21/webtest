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

import de.ppi.selenium.util.Protocol;

/**
 * Concrete web-browser
 * @author niels
 *
 */
public class WebBrowserImpl implements WebBrowser {

	public static final Logger LOG = LoggerFactory.getLogger(WebBrowserImpl.class);

	private static final List<WebBrowser> ALL_INSTANCES = new ArrayList<>();

    private final WebDriver webdriver;

    private final String sessionId;

    private String baseUrl;

    static {
    	Runtime.getRuntime().addShutdownHook(new Thread(){

			@Override
			public void run() {
				final List<WebBrowser> allBrowsers = new ArrayList<>(ALL_INSTANCES);
				for (WebBrowser webBrowser : allBrowsers) {
					try {
						webBrowser.quit();
					} catch (UnreachableBrowserException ube) {
						//no problem.
					} catch (Exception e) {
						LOG.warn("Problem to shutdown a browser (" +
								webBrowser.getSessionId() + ")", e);
					}
				}
				super.run();
			}

    	});
    }

    private final boolean logBeforeGet = Boolean.getBoolean("webtest.logBeforeGet");
    private final boolean logAfterGet = Boolean.getBoolean("webtest.logAfterGet");

    /**
     * Creates a new browser-session.
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
    public String getSessionId() {
        return sessionId;
    }

    public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		if (baseUrl.endsWith("/")) {
        	this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
        	this.baseUrl = baseUrl;
        }
	}

	/**
     * @param url
     * @see org.openqa.selenium.WebDriver#get(java.lang.String)
     */
    public void get(String url) {
        if (logBeforeGet) {
        	Protocol.log(getTitle(), "Goto " + url, webdriver);
        }
        webdriver.get(url);
        if (logAfterGet) {
        	Protocol.log(getTitle(), "Opened " + url, webdriver);
        }
    }

    /**
     * Call the relative url.
     *
     * @param relativeUrl the relative url.
     */
    public void getRelativeUrl(String relativeUrl) {
    	get(getBaseUrl() + relativeUrl);
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getCurrentUrl()
     */
    public String getCurrentUrl() {
        return webdriver.getCurrentUrl();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getTitle()
     */
    public String getTitle() {
        return webdriver.getTitle();
    }

    /**
     * @param by
     * @return
     * @see org.openqa.selenium.WebDriver#findElements(org.openqa.selenium.By)
     */
    public List<WebElement> findElements(By by) {
        return webdriver.findElements(by);
    }

    /**
     * @param by
     * @return
     * @see org.openqa.selenium.WebDriver#findElement(org.openqa.selenium.By)
     */
    public WebElement findElement(By by) {
        return webdriver.findElement(by);
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getPageSource()
     */
    public String getPageSource() {
        return webdriver.getPageSource();
    }

    /**
     *
     * @see org.openqa.selenium.WebDriver#close()
     */
    public void close() {
        webdriver.close();
    }

    /**
     *
     * @see org.openqa.selenium.WebDriver#quit()
     */
    public void quit() {
        webdriver.quit();
        ALL_INSTANCES.remove(this);
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getWindowHandles()
     */
    public Set<String> getWindowHandles() {
        return webdriver.getWindowHandles();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#getWindowHandle()
     */
    public String getWindowHandle() {
        return webdriver.getWindowHandle();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#switchTo()
     */
    public TargetLocator switchTo() {
        return webdriver.switchTo();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#navigate()
     */
    public Navigation navigate() {
        return webdriver.navigate();
    }

    /**
     * @return
     * @see org.openqa.selenium.WebDriver#manage()
     */
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
