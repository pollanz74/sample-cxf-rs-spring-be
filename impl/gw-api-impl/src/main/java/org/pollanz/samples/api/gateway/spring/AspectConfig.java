package org.pollanz.samples.api.gateway.spring;

import org.pollanz.samples.api.gateway.aspect.LoggingAspect;
import org.pollanz.samples.api.gateway.aspect.ResponseAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect();
    }

    @Bean
    public ResponseAspect responseAspect() {
        return new ResponseAspect();
    }

}
