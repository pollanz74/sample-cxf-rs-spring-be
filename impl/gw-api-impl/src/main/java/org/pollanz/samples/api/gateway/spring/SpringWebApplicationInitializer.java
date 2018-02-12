package org.pollanz.samples.api.gateway.spring;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SpringWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(
                ApplicationConfig.class,
                AspectConfig.class,
                CacheConfig.class
        );

        servletContext.addListener(new ContextLoaderListener(rootContext));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("CXFServlet", new CXFServlet());
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/rest/*");
        dispatcher.setInitParameter("service-list-path", "/info");
    }

}
