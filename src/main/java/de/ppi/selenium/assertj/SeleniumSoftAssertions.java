package de.ppi.selenium.assertj;

import static org.assertj.core.util.Arrays.array;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.assertj.core.api.JUnitSoftAssertions;
import org.assertj.core.api.Java6AbstractStandardSoftAssertions;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;
import org.openqa.selenium.Alert;
import org.selophane.elements.base.Element;

import de.ppi.selenium.browser.WebBrowser;
import de.ppi.selenium.logevent.api.EventLoggerFactory;
import de.ppi.selenium.logevent.api.EventSource;

/**
 * Selenium specific assertions in the soft-variant.
 *
 * @see JUnitSoftAssertions
 *
 */
public class SeleniumSoftAssertions extends Java6AbstractStandardSoftAssertions
        implements TestRule {
    /**
     * Collector for errors.
     */
    private final ErrorCollectorWithScreenshots collector =
            new ErrorCollectorWithScreenshots();

    /**
     *
     * Initiates an object of type SeleniumSoftAssertions.
     */
    public SeleniumSoftAssertions() {
        super();
    }

    /**
     * Creates a new instance of <code>{@link ElementAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public ElementAssert assertThat(Element actual) {
        return proxy(ElementAssert.class, Element.class, actual);
    }

    /**
     * Creates a new instance of <code>{@link AlertAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public AlertAssert assertThat(Alert actual) {
        return proxy(AlertAssert.class, Alert.class, actual);
    }

    /**
     * Creates a new instance of <code>{@link WebbrowserAssert}</code>.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    public WebbrowserAssert assertThat(WebBrowser actual) {
        return proxy(WebbrowserAssert.class, WebBrowser.class, actual);
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                base.evaluate();
                MultipleFailureException.assertEmpty(collector.errors());
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, V> V proxy(Class<V> assertClass, Class<T> actualClass, T actual) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(assertClass);
        enhancer.setCallback(collector);
        return (V) enhancer.create(array(actualClass), array(actual));
    }

    /**
     *
     * Class which collects errors and create screenshots.
     *
     */
    static class ErrorCollectorWithScreenshots implements MethodInterceptor {

        /** Eventlogger-Factory. */
        private static final EventLoggerFactory EVENT_LOGGER =
                EventLoggerFactory.getInstance(EventSource.ASSERTION);

        /** List of errors. */
        private final List<Throwable> errors = new ArrayList<Throwable>();

        @Override
        public Object intercept(Object obj, Method method, Object[] args,
                MethodProxy proxy) throws Throwable {
            try {
                proxy.invokeSuper(obj, args);
            } catch (AssertionError e) {
                errors.add(e);
                EVENT_LOGGER.onFailure(obj.getClass().getName(),
                        method.getName()).logAssertionError(e);
            }
            return obj;
        }

        /**
         * Returns the list of errors.
         *
         * @return the list of errors.
         */
        public List<Throwable> errors() {
            return Collections.unmodifiableList(errors);
        }

    }

}
