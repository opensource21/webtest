/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package de.ppi.selenium.assertj;

import org.assertj.core.api.AbstractAssert;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

import de.ppi.selenium.browser.SessionManager;

/**
 *
 * Assert for selenium {@link Alert}.
 *
 */
public class AlertAssert extends AbstractAssert<AlertAssert, Alert> {

    /**
     * Initiates an object of type AlertAssert.
     *
     * @param actual the current object.
     */
    public AlertAssert(Alert actual) {
        super(actual, AlertAssert.class);
    }

    /**
     * Check that the alert box contains the given text.
     *
     * @param textToFind the text which should be find.
     * @return self
     */
    public AlertAssert hasText(String textToFind) {
        try {
            if (!actual.getText().contains(textToFind)) {
                super.failWithMessage("The alert box does not contain the text: "
                        + textToFind
                        + " . Actual text found : "
                        + actual.getText());
            }
        } catch (NoAlertPresentException e) {
            super.failWithMessage("There is no alert box");
        }

        return this;
    }

    /**
     * Check that an alert box is present.
     *
     * @return self
     */
    public AlertAssert isPresent() {
        try {
            SessionManager.getSession().switchTo().alert();
        } catch (NoAlertPresentException e) {
            super.failWithMessage("There is no alert box");
        }

        return this;
    }

}
