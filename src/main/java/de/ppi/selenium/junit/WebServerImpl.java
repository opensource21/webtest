/**
 *
 */
package de.ppi.selenium.junit;

/**
 * WebServer which delegates to a real webserver or use a running.
 * @author niels
 *
 */
public class WebServerImpl implements WebServer {

	private static final String baseUrl = System.getProperty("webtest.baseurl");


	private final WebServer realWebServer;

	public WebServerImpl(WebServer realWebServer) {
		super();
		this.realWebServer = realWebServer;
	}



	@Override
	public void start() throws Exception {
		if (baseUrl == null) {
			realWebServer.start();
		}
	}


	@Override
	public boolean isRunning() {
		if (baseUrl == null) {
			return realWebServer.isRunning();
		} else {
			return true;
		}
	}


	@Override
	public void stop() throws Exception {
		if (baseUrl == null) {
			realWebServer.stop();
		}
	}


	@Override
	public String getBaseUrl() {
		if (baseUrl == null) {
			return realWebServer.getBaseUrl();
		} else {
			return baseUrl;
		}

	}

}
