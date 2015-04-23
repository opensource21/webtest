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

import org.openqa.selenium.Alert;
import org.selophane.elements.base.Element;

import de.ppi.selenium.browser.WebBrowser;


public final class SeleniumAssertions {

    private SeleniumAssertions() {
        //only static
    }

    public static ElementAssert assertThat(Element actual) {
        return new ElementAssert(actual);
    }

    public static AlertAssert assertThat(Alert actual) {
        return new AlertAssert(actual);
    }

    public static WebbrowserAssert assertThat(WebBrowser actual) {
    	return new WebbrowserAssert(actual);
    }

}
