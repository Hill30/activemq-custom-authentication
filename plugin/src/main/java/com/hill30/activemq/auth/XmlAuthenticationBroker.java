package com.hill30.activemq.auth;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;

import java.util.Map;

public class XmlAuthenticationBroker extends BrokerFilter {

    private Map<String, String> users;

    public XmlAuthenticationBroker(Broker next, Map<String, String> users) {
        super(next);
        this.users = users;
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {

        String userName = info.getUserName();
        String password = info.getPassword();

        if (!users.containsKey(userName)) {
            throw new SecurityException("There is no user " + userName);
        }
        if (!users.get(userName).equals(password)) {
            throw new SecurityException("Wrong password for user " + userName);
        }
        super.addConnection(context, info);
    }
}
