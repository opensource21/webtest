package de.ppi.selenium.logevent.api;

import static de.ppi.selenium.logevent.api.Priority.DEBUG;
import static de.ppi.selenium.logevent.api.Priority.DOCUMENTATION;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test for {@link Priority}.
 *
 */
@RunWith(Parameterized.class)
public class PriorityTest {

    /**
     * Testdata.
     *
     * @return testdata.
     */
    @Parameters(name = "{0} - {1} = {2}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] { { DEBUG, DEBUG, TRUE },
                { DOCUMENTATION, DEBUG, TRUE }, { DEBUG, DOCUMENTATION, FALSE } });
    }

    /** priority where the function is called. */
    private final Priority thisPriority;
    /** priority with is the parameter. */
    private final Priority otherPriority;
    /** expected result. */
    private final Boolean expectedResult;

    /**
     * Initiates an object of type PriorityTest.
     *
     * @param thisPriority priority where the function is called
     * @param otherPriority priority with is the parameter.
     * @param expectedResult expected result;
     */
    public PriorityTest(Priority thisPriority, Priority otherPriority,
            Boolean expectedResult) {
        super();
        this.thisPriority = thisPriority;
        this.otherPriority = otherPriority;
        this.expectedResult = expectedResult;
    }

    /**
     * Test method for
     * {@link de.ppi.selenium.logevent.api.Priority#isMoreImportantThan(de.ppi.selenium.logevent.api.Priority)}
     * .
     */
    @Test
    public void testIsMoreImportantThan() {
        Assert.assertEquals(expectedResult, Boolean.valueOf(thisPriority
                .isMoreImportantThan(otherPriority)));
    }
}
