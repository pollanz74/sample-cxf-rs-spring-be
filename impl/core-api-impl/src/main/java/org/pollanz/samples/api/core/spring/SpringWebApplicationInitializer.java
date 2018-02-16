package org.pollanz.samples.api.core.spring;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class SpringWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    public SpringWebApplicationInitializer() {
        super(
                ApplicationConfig.class,
                DataSourceConfig.class,
                PersistenceConfig.class,
                LoggingAspectConfig.class,
                SecurityConfig.class
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
