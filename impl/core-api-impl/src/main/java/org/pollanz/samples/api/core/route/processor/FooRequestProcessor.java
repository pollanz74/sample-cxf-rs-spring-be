package org.pollanz.samples.api.core.route.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

@Slf4j
public class FooRequestProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        log.info("Message received: {}", exchange.getIn().getBody());
    }

}
