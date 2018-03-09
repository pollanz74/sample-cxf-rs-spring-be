package org.pollanz.samples.api.core.route;

import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spring.SpringRouteBuilder;
import org.pollanz.samples.api.core.route.processor.FooRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class MainRouteBuilder extends SpringRouteBuilder {

    public static final String DIRECT_SAMLE_CORE_FOO_REQUEST_QUEUE_URI = "direct:SAMPLE_CORE.fooRequest";

    public static final String JMS_SAMLE_CORE_FOO_REQUEST_QUEUE_URI = "jms:SAMPLE_CORE.fooRequest";

    @Autowired
    private FooRequestProcessor fooRequestProcessor;

    @Override
    public void configure() throws Exception {

        from(DIRECT_SAMLE_CORE_FOO_REQUEST_QUEUE_URI)
                .marshal().json(JsonLibrary.Jackson, true)
                .to(JMS_SAMLE_CORE_FOO_REQUEST_QUEUE_URI);

        from(JMS_SAMLE_CORE_FOO_REQUEST_QUEUE_URI)
                .unmarshal().json(JsonLibrary.Jackson)
                .bean(fooRequestProcessor);
    }

}
