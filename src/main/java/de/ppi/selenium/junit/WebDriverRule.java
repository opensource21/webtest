package de.ppi.selenium.junit;

import org.junit.rules.ExternalResource;

import de.ppi.selenium.browser.SessionManager;
import de.ppi.selenium.browser.WebBrowser;

public class WebDriverRule extends ExternalResource {

	private static volatile long nrOfTests = 0;
	//TODO: This should be configurable via Annotations.
	private static final long MAX_NR_OF_REUSE = 10;

	@Override
	protected void before() throws Throwable {
		nrOfTests++;
		if (nrOfTests > MAX_NR_OF_REUSE) {
			final SessionManager manager = SessionManager.getInstance();
			final WebBrowser browser = manager.getCurrentSession(false);
			if (browser != null) {
				manager.removeSession(browser);
				browser.quit();
			}
		}
	}

}
