package org.test.consumer.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.jms.dsl.Jms;
import org.test.consumer.handler.JMSMessageConsumerHandler;

@Configuration
@Profile("jms")
@Import(value = {MessageConsumerJMSBrokerConfiguration.class})
public class MessageConsumerJMSConfiguration {
    @Autowired
    private DirectChannel inputChannel;

    @Value("${queuename}")
    private String queueName;

    @Bean
    @ServiceActivator(inputChannel = "inputChannel")
    public JMSMessageConsumerHandler jmsMessageConsumerHandler() {
        return new JMSMessageConsumerHandler();
    }

    @Bean
    public IntegrationFlow jmsMessageConsumerFlow(ActiveMQConnectionFactory connectionFactory) {
        return IntegrationFlow
            .from(Jms.messageDrivenChannelAdapter(connectionFactory)
                .destination(new ActiveMQTopic(queueName)))
            .channel("inputChannel")
            .get();
    }

}
