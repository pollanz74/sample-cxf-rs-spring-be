package org.pollanz.samples.api.gateway.spring;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.pollanz.samples.api.core.spring.MetricsConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class SpringWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SpringWebApplicationInitializer() {
        super(
                SecurityConfig.class,
                ApplicationConfig.class,
                AspectConfig.class,
                CacheConfig.class,
                MetricsConfig.class
        );
    }

    @Override
    protected void afterSpringSecurityFilterChain(ServletContext servletContext) {
        super.afterSpringSecurityFilterChain(servletContext);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("CXFServlet", new CXFServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/rest/*");
        dispatcher.setInitParameter("service-list-path", "/info");
    }

}
