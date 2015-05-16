/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package de.ppi.selenium.browser;

import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 *
 * Interface for DefaultSessionsFactory
 *
 */
public interface WebDriverFactory {
    /**
     *
     * @return a map of strings
     */
    public Map<String, String> createDefaultOptions();

    /**
     * Cleans up the session - closed all taks
     *
     * @param options the options for cleaning up
     * @throws Exception
     */
    public void cleanup(Map<String, String> options) throws Exception;

    /**
     *
     * @param options the options you want the capabilities instance to have
     * @return the capabilities of webdriver
     * @throws Exception
     */
    public DesiredCapabilities createCapabilities(Map<String, String> options)
            throws Exception;

    /**
     *
     * @param options the options passed on to the webdriver instance
     * @param capabilities the capabilities you want webdriver to have
     * @return the instance of WebDriver not wrapped by our extended version
     * @throws IOException can't access the browser.
     */
    public WebDriver createWebDriver(Map<String, String> options,
            DesiredCapabilities capabilities) throws IOException;

}
