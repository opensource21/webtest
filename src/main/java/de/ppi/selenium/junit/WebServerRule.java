package de.ppi.selenium.junit;

import org.junit.rules.ExternalResource;

import de.ppi.selenium.browser.SessionManager;

public class WebServerRule extends ExternalResource {

	private final WebServer webServer;

	public WebServerRule(WebServer webServer) {
		this.webServer = webServer;
		SessionManager.getInstance().setDefaultBaseUrl(webServer.getBaseUrl());
	}



	@Override
	protected void before() throws Throwable {
		if (!webServer.isRunning()) {
			webServer.start();
		}
	}



}
