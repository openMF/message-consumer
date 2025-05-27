package org.test.consumer.config;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

@Configuration
@Profile("jms")
public class MessageConsumerJMSBrokerConfiguration {

    @Value("${brokerurl}")
    private String brokerUrl;

    @Value("${brokerusername}")
    private String brokerUserName;

    @Value("${brokerpassword}")
    private String brokerPassword;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(brokerUrl);
        if (StringUtils.hasText(brokerUserName)) {
            connectionFactory.setUserName(brokerUserName);
        }
        if (StringUtils.hasText(brokerPassword)) {
            connectionFactory.setPassword(brokerPassword);
        }
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }
}
