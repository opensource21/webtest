package de.ppi.selenium.junit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
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
     * Maximalnumber of costs before the webdriver should newly created.
     */
    private static final long MAX_NR_OF_REUSE = Long.parseLong(
            System.getProperty("webtest.maxNrOfBrowserReuse", "100"));

    @Override
    protected void failed(Throwable e, Description description) {
        final List<Throwable> failures = new ArrayList<>();
        if (e instanceof MultipleFailureException) {
            final MultipleFailureException mfe = (MultipleFailureException) e;
            failures.addAll(mfe.getFailures());
        } else {
            failures.add(e);
        }
        for (Throwable throwable : failures) {
            if (throwable instanceof UnreachableBrowserException) {
                final SessionManager manager = SessionManager.getInstance();
                quitBrowser(manager);
                return;
            }
        }
    }

    @Override
    protected void starting(Description description) {
        final SessionManager manager = SessionManager.getInstance();
        final Browser browserInfo = description.getAnnotation(Browser.class);
        final long cost;
        final boolean forceRestart;
        if (browserInfo == null) {
            cost = 1;
            forceRestart = false;
        } else {
            cost = browserInfo.cost();
            forceRestart = browserInfo.forceRestart();

        }
        if (forceRestart || nrOfTests > MAX_NR_OF_REUSE) {
            quitBrowser(manager);
        }
        final WebBrowser browser = manager.getCurrentSession(false);
        if (browser == null) {
            manager.getCurrentSession(true);
        }
        nrOfTests = nrOfTests + cost;
    }

    /**
     * Quits the browser.
     *
     * @param manager the {@link SessionManager}.
     */
    private void quitBrowser(final SessionManager manager) {
        final WebBrowser browser = manager.getCurrentSession(false);
        if (browser != null) {
            nrOfTests = 0;
            manager.removeSession(browser);
            browser.quit();
        }
    }

    /**
     * Annotation to define how often a browser must be restarted.
     *
     */
    @Target({ ElementType.METHOD })
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Browser {

        /**
         * How much the browser is stressed by the test. Default: 1.
         */
        long cost() default 1;

        /**
         * Force browser restart. Default: false;
         */
        boolean forceRestart() default false;

    }

}
