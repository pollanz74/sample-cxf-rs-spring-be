package org.pollanz.samples.api.core.spring;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationInInterceptor;
import org.apache.cxf.jaxrs.validation.JAXRSBeanValidationOutInterceptor;
import org.apache.cxf.jaxrs.validation.ValidationExceptionMapper;
import org.pollanz.samples.api.core.PetApi;
import org.pollanz.samples.api.core.impl.PetApiServiceImpl;
import org.pollanz.samples.api.core.service.PetService;
import org.pollanz.samples.api.core.service.impl.PetServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan("org.pollanz.samples.api")
@PropertySource(value = "file:///${core_properties_file}", ignoreResourceNotFound = true)
public class ApplicationConfig {

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    @DependsOn("cxf")
    @Bean
    public Server jaxrsServer() {
        JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setBus(cxf());
        jaxrsServerFactoryBean.setAddress(env.getProperty("sample.core.jaxrsServer.address", "/"));
        jaxrsServerFactoryBean.setServiceBeans(Arrays.<Object>asList(
                apiListingResource(),
                petApi()
                )
        );
        jaxrsServerFactoryBean.setExtensionMappings(extensionMappings());
        jaxrsServerFactoryBean.setProviders(providers());
        jaxrsServerFactoryBean.setFeatures(Arrays.asList(loggingFeature(), swagger2Feature()));
        jaxrsServerFactoryBean.setInInterceptors(Arrays.asList(new JAXRSBeanValidationInInterceptor()));
        jaxrsServerFactoryBean.setOutInterceptors(Arrays.asList(new JAXRSBeanValidationOutInterceptor()));
        jaxrsServerFactoryBean.getInInterceptors().add(new LoggingInInterceptor());
        jaxrsServerFactoryBean.getOutInterceptors().add(new LoggingOutInterceptor());
        return jaxrsServerFactoryBean.create();
    }

    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }

    public Swagger2Feature swagger2Feature() {
        Swagger2Feature swagger2Feature = new Swagger2Feature();
        swagger2Feature.setResourcePackage("org.pollanz.samples.api.core");
        swagger2Feature.setBasePath("/sample-core/rest");
        swagger2Feature.setVersion(env.getProperty("sample.core.swagger.version", StringUtils.EMPTY));
        swagger2Feature.setTitle(env.getProperty("sample.core.swagger.title", StringUtils.EMPTY));
        swagger2Feature.setDescription(env.getProperty("sample.core.swagger.description", StringUtils.EMPTY));
        swagger2Feature.setScan(true);
        swagger2Feature.setScanAllResources(true);
        swagger2Feature.setContact(env.getProperty("sample_core_swagger_contact", StringUtils.EMPTY));
        swagger2Feature.setLicense(env.getProperty("sample_core_swagger_license", StringUtils.EMPTY));
        swagger2Feature.setLicenseUrl(env.getProperty("sample_core_swagger_licenseUrl", StringUtils.EMPTY));
        return swagger2Feature;
    }

    private Map<Object, Object> extensionMappings() {
        final Map map = new HashMap();
        map.put(MediaType.APPLICATION_JSON_TYPE.getSubtype(), MediaType.APPLICATION_JSON);
        // map.put(MediaType.APPLICATION_XML_TYPE.getSubtype(), MediaType.APPLICATION_XML);
        return map;
    }

    @Bean
    public List<Object> providers() {
        // providers.add(new JodaTimeParamConverterProvider());
        // providers.add(new ExceptionProvider());
        return Arrays.<Object>asList(
                new JacksonJaxbJsonProvider(),
                new SwaggerSerializers(),
                new ValidationExceptionMapper()
        );
    }

    @Bean
    public ApiListingResource apiListingResource() {
        return new ApiListingResource();
    }

    @Bean
    public PetApi petApi() {
        return new PetApiServiceImpl(petService());
    }

    @Bean
    public PetService petService() {
        return new PetServiceImpl();
    }

    private List<Object> beanproviders() {
        return Arrays.<Object>asList(
                new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        );
    }

}
