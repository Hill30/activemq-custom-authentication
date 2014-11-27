Activemq custom authentication
==============================

## 1. Clone repository

## 2. Install service for authentication

### 2.1 If you want to use Rails test service

Be sure, that you have installed rails environment

* cd ruby-auth-service
* bundle install

(run only once, next time is not necessary)
* rails server

Check, that service works
* open in browser http://localhost:3000/auth/auth?username=admin&password=admin

Should be {"notice":"Login OK"}

There are only pairs admin/admin and userName/password are valid.

### 2.2 If you want to use C# test service

Write it by yourself. It should just answer for requests like

    http://localhost:3000/auth/auth?username=admin&password=admin


## 3. Install ActiveMQ

* Download & unzip activemq 5.10.0
* Create backup copy of ${ACTIVEMQ_HOME}/conf/activemq.xml


## 4. Modify config

* Modify ${ACTIVEMQ_HOME}/conf/activemq.xml
Add to tag broker tag plugins with content below


```xml
<plugins>
    <bean id="authenticationPlugin" xmlns="http://www.springframework.org/schema/beans"
            class="com.hill30.activemq.auth.RestAuthenticationPlugin">
        <property name="authServiceUrl">
            <value>http://localhost:3000/auth/auth/</value>
        </property>
    </bean>
</plugins>
```

* Edit url for service appropriate to your service


## 5. Compile & install authentication plugin

* cd plugin
* mvn install
* cp target/activemq-rest-auth.jar ${ACTIVEMQ_HOME}/lib/
* ${ACTIVEMQ_HOME}/bin/activemq start


## 6. Check sending messages

* tail -n 100 -f ${ACTIVEMQ_HOME}/data/activemq.log
Output should looks like

    2014-11-25 08:24:23,646 | INFO  | Connector ws started | org.apache.activemq.broker.TransportConnector | main
    2014-11-25 08:24:23,650 | INFO  | Apache ActiveMQ 5.10.0 (localhost, ID:npakudin-mac.local-62876-1416889463309-0:1) started | org.apache.activemq.broker.BrokerService | main
    2014-11-25 08:24:23,653 | INFO  | For help or more information please see: http://activemq.apache.org | org.apache.activemq.broker.BrokerService | main
    2014-11-25 08:24:23,656 | WARN  | Store limit is 102400 mb (current store usage is 0 mb). The data directory: /Users/npakudin/Portable/activemq/apache-activemq-5.10.0/data/kahadb only has 16083 mb of usable space - resetting to maximum available disk space: 16084 mb | org.apache.activemq.broker.BrokerService | main
    2014-11-25 08:24:23,659 | ERROR | Temporary Store limit is 51200 mb, whilst the temporary data directory: /Users/npakudin/Portable/activemq/apache-activemq-5.10.0/data/localhost/tmp_storage only has 16083 mb of usable space - resetting to maximum available 16083 mb. | org.apache.activemq.broker.BrokerService | main
    2014-11-25 08:24:24,024 | INFO  | ActiveMQ WebConsole available at http://0.0.0.0:8161/ | org.apache.activemq.web.WebConsoleStarter | main
    2014-11-25 08:24:24,092 | INFO  | Initializing Spring FrameworkServlet 'dispatcher' | /admin | main
    2014-11-25 08:24:24,307 | INFO  | jolokia-agent: No access restrictor found at classpath:/jolokia-access.xml, access to all MBeans is allowed | /api | main
    2014-11-25 08:24:37,866 | INFO  | Connector vm://localhost started | org.apache.activemq.broker.TransportConnector | qtp1317336422-44
    2014-11-25 09:12:28,969 | INFO  | Connector vm://localhost stopped | org.apache.activemq.broker.TransportConnector | HashSessionScavenger-0

This command displays the contents of activemq.log and do not stop when end of file is reached, but rather to wait for additional data to be appended to the input.

* Open http://localhost:8161/admin/topics.jsp
* Select topic "ServiceTracker.Inbound.userName" (if not exists, connect Android application - it will create it) and press link "Send To"
* Press Send button
* Check, that counter Messages Enqueued increased


## 7. Check Android application connection

* Run Android app (if it was run before, clear data and run again)
* Enter url of your ActiveMQ listener, e.g. tcp://192.168.0.100:1883
* Open http://localhost:8161/admin/topics.jsp and send message to topic "ServiceTracker.Inbound.userName".
Message body shoud be valid JSON, e.g.

    {}
    
Check, that Android app shows the message
* Press to that message, enter any data to any field and press Save
* Check, that topic ServiceTracker.Outbound appeared in ActiveMQ admin console and it's counter Messages Enqueued increased



