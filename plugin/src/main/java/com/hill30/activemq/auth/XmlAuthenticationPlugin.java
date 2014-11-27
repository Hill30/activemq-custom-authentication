package com.hill30.activemq.auth;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

import java.util.Map;

public class XmlAuthenticationPlugin implements BrokerPlugin {

	private Map<String, String> users;

	public Broker installPlugin(Broker broker) throws Exception {
		return new XmlAuthenticationBroker(broker, users);
	}

	public Map<String, String> getUsers() {
		return users;
	}

	public void setUsers(Map<String, String> users) {
		this.users = users;
	}
}
