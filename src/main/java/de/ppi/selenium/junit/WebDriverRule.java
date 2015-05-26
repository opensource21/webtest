package de.ppi.selenium.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.remote.UnreachableBrowserException;

import de.ppi.selenium.browser.SessionManager;
import de.ppi.selenium.browser.WebBrowser;

/**
 * Junit-Rule which handle the WebDriver.
 *
 */
@SuppressWarnings("deprecation")
public class WebDriverRule extends TestWatcher {

    /**
     * The number of test which runs.
     */
    private static volatile long nrOfTests = 0;

    /**
     * Maximalnumber of tests before the webdriver should newly created.
     */
    // TODO: This should be configurable via Annotations.
    private static final long MAX_NR_OF_REUSE = 10;

    @Override
    protected void failed(Throwable e, Description description) {
        final List<Throwable> failures = new ArrayList<>();
        if (e instanceof MultipleFailureException) {
            final MultipleFailureException mfe = (MultipleFailureException) e;
            failures.addAll(mfe.getFailures());
        }
        for (Throwable throwable : failures) {
            if (throwable instanceof UnreachableBrowserException) {
                final SessionManager manager = SessionManager.getInstance();
                final WebBrowser browser = manager.getCurrentSession(false);
                if (browser != null) {
                    manager.removeSession(browser);
                    browser.quit();
                }
                return;
            }
        }
    }

    @Override
    protected void starting(Description description) {
        nrOfTests++;
        final SessionManager manager = SessionManager.getInstance();
        if (nrOfTests > MAX_NR_OF_REUSE) {
            final WebBrowser browser = manager.getCurrentSession(false);
            if (browser != null) {
                manager.removeSession(browser);
                browser.quit();
            }
        }
        final WebBrowser browser = manager.getCurrentSession(false);
        if (browser == null) {
            manager.getCurrentSession(true);
        }
    }

}
