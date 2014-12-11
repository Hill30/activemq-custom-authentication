package com.hill30.activemq.auth;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class RestAuthenticationBroker extends BrokerFilter {

    private final String authServiceUrl;

    public RestAuthenticationBroker(Broker next, String authServiceUrl) {
        super(next);
        this.authServiceUrl = authServiceUrl;
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {

        String userName = info.getUserName();
        String password = info.getPassword();

        HttpClient httpClient = new DefaultHttpClient();

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("username", userName));
        nameValuePairs.add(new BasicNameValuePair("password", password));


        HttpPost httpRequest = new HttpPost(authServiceUrl);
        httpRequest.setHeader("userName", userName);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
        httpRequest.setEntity(entity);

        HttpResponse response = httpClient.execute(httpRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200) {
            throw new SecurityException("Cannot login user " + userName + "\n" +
                    "authentication service returned status : " + statusCode + "\n" +
                    EntityUtils.toString(response.getEntity()));
        }

        super.addConnection(context, info);
    }
}
