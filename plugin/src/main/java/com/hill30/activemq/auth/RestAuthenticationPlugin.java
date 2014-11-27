package com.hill30.activemq.auth;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

public class RestAuthenticationPlugin implements BrokerPlugin {

	private String authServiceUrl;

	public String getAuthServiceUrl() {
		return authServiceUrl;
	}

	public void setAuthServiceUrl(String authServiceUrl) {
		this.authServiceUrl = authServiceUrl;
	}

	public Broker installPlugin(Broker broker) throws Exception {
		return new RestAuthenticationBroker(broker, authServiceUrl);
	}
}
