package org.pollanz.samples.api.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.camel.spring.SpringCamelContext;
import org.pollanz.samples.api.core.route.MainRouteBuilder;
import org.pollanz.samples.api.core.route.processor.FooRequestProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import javax.naming.NamingException;
import javax.ws.rs.WebApplicationException;

@Slf4j
@Configuration
public class CamelConfig {

    @Value("${sample_core_jms_provider_url:tcp://localhost:61616}")
    private String providerUrl;

    @Value("${sample_core_jms_username}")
    private String username;

    @Value("${sample_core_jms_password}")
    private String password;

    @Bean
    public ConnectionFactory jmsConnectionFactory() throws NamingException {
        return new ActiveMQConnectionFactory(username, password, providerUrl);
    }

    @Bean
    public JmsComponent jms() throws NamingException {
        JmsComponent jmsComponent = new JmsComponent();
        jmsComponent.setConnectionFactory(jmsConnectionFactory());
        return jmsComponent;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public CamelContext camelContext() {
        try {
            final CamelContext ctx = new SpringCamelContext();
            ctx.addRoutes(routeBuilder());
            return ctx;
        } catch (Exception e) {
            log.error("Error creating Camel context", e);
            throw new WebApplicationException(e);
        }
    }

    @Bean
    public RouteBuilder routeBuilder() {
        return new MainRouteBuilder();
    }

    @Bean
    public ProducerTemplate producerTemplate() throws Exception {
        ProducerTemplate producer;
        producer = new DefaultProducerTemplate(camelContext());
        producer.start();
        return producer;
    }

    @Bean
    public FooRequestProcessor fooRequestProcessor() {
        return new FooRequestProcessor();
    }

}
