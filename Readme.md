# WebTest[![Build Status](https://travis-ci.org/opensource21/webtest.svg?branch=master)](https://travis-ci.org/opensource21/webtest)

WebTest provides some base-classes which makes Selenium test more comfortable.
It's inspired by [Jtaf-EXTWebdriver](http://finraos.github.io/JTAF-ExtWebDriver)
and [FluentLenium](https://github.com/FluentLenium/FluentLenium).
It used selophane in a special version.

## Advantages you get with WebTest
- EventLog
- Screenshots
- AssertJ-Extensions, which makes a screenshot on assertion-failure.
- JUnit-Rules
    - ProtocolRule: This rule configures the Protocol-class and creates
      for each test a directory (removed it if it is empty). Makes a screenshot
      if the test fails.
    - WebDriverRule: Creates at the beginning a WebBrowser in the SessionManager.
      If selenium loose the connection to the browser or more than 10 test
      use the webbrowser a new instance is created.
    - WebServerRule: Starts at the beginning the webserver.
- SessionManager which handles different session and create webdriver instances.

## Configuration
You can influence the behavior with the following system-properties, which
must be set via `-D<propertyName>=value`.

- `phantomjs.binary.path` - defines the path to phantomjs for example
   `C:\\RegFreeProgs\\phantomjs\\phantomjs.exe`
-  `webtest.maxNrOfBrowserReuse` - defines the number of reuses of the browser, default is 100.


### Old
- `webtest.logBeforeGet` - if `true` a screenshot is created before a new page is requested via get.
- `webtest.logAfterGet` - if `true` a screenshot is created after a new page is requested via get.
- `webtest.protocoldir` - defines the default-dir for webtest-logs.
- `webtest.baseurl` - defines the base-url.


### Browser
The browser is configured by a file `client.properties`. The properties are explained at
[Jtaf-EXTWebdriver](http://finraos.github.io/JTAF-ExtWebDriver/clientproperties.html)
which is the base of my code.
Additional you can define:

- `webdriver.phantomjs.driver` - the path to phantom.js
- `browser.accept_languages` - to set the accepted languages, doesn't work for IE.
   For HTMLUnit it sets the browser language.
- `ffBinaryPath` - the path to FF-executable.
- `chromeBinaryPath` - the path to FF-executable.

## Usage
First add a maven-dependcy

     <dependency>
        <groupId>de.ppi.oss</groupId>
        <artifactId>webtest</artifactId>
        <version>0.4</version>
       <scope>test</scope>
    </dependency>

The Rules are more effectiv if you only instantiate them once. So the best way is
to declare a constant-class, like:

    public interface WebTestConstants {

        TestWebServer WEB_SERVER = new TestWebServer("/fuwesta");

        /**
         * The system to store the events.
         */
        H2EventStorage EVENT_STORAGE =
                new H2EventStorage(
                        "jdbc:h2:./dbs/testlog;MODE=PostgreSQL;"
                                + "AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                        "sa", "");

        /**
         * Standard_Rule for WebTests.
         */
        RuleChain WEBTEST_WITHOUT_AUTHENTICATION = RuleChain
                .outerRule(new WebServerRule(new DelegatingWebServer(WEB_SERVER)))
                .around(new EventLogRule(EVENT_STORAGE, new MarkdownReporter(
                        "weblog", false, Priority.DEBUG)))
                .around(new WebDriverRule());
        /**
         * Standard_Rule for WebTests.
         */
        RuleChain WEBTEST = RuleChain.outerRule(WEBTEST_WITHOUT_AUTHENTICATION)
                .around(new AuthRule());

    }

Then you can use it in your test-class with

    @Rule
    public RuleChain webTest = WebTestConstants.WEBTEST;

You can see a simple example at [FuWeSta-Sample](https://github.com/opensource21/fuwesta).


## TODOs
- The code must be tested and specially the code in `de.ppi.selenium.browser`
  should be refactored.
- Improve the new EventLog-System
