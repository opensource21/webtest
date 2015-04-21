/**
 *
 */
package de.ppi.selenium.browser;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.ppi.selenium.util.Protocol;

/**
 * Concrete web-browser
 * @author niels
 *
 */
public class WebBrowserImpl implements WebBrowser {

    private final WebDriver webdriver;

    private final String sessionId;

    private final String baseUrl;

    //TODO this must be set outside.
    private final boolean logEveryPage = true;

    /**
     * Creates a new browser-session.
     * @param webdriver the native webdriver
     * @param sessionId the id of the session to get the webbrowser.
     * @param baseUrl the basis url, where all other are relative to.
     */
    public WebBrowserImpl(WebDriver webdriver, String sessionId, String baseUrl) {
        super();
        this.sessionId = sessionId;
        this.webdriver = webdriver;
        if (baseUrl.endsWith("/")) {
        	this.baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        } else {
        	this.baseUrl = baseUrl;
        }
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

	/**
     * @param url
     * @see org.openqa.selenium.WebDriver#get(java.lang.String)
     */
    public void get(String url) {
        webdriver.get(url);
        if (logEveryPage) {
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


}
