package org.pollanz.samples.api.gateway.spring;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.pollanz.samples.api.core.PetApi;
import org.pollanz.samples.api.gateway.core.proxy.PetProxiedApi;
import org.pollanz.samples.api.gateway.impl.GatewayExceptionProvider;
import org.pollanz.samples.api.gateway.impl.core.proxy.PetProxiedApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ApplicationConfig {

    private final static String DA_CORE_CONTEXT_PATH = "sample-core/v1";

    @Autowired
    private Environment env;

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }

    @Bean
    public Server jaxrsServer() {
        JAXRSServerFactoryBean jaxrsServerFactoryBean = new JAXRSServerFactoryBean();
        jaxrsServerFactoryBean.setBus(cxf());
        jaxrsServerFactoryBean.setAddress(env.getProperty("sample_gateway_jaxrsServer_address", "/"));
        jaxrsServerFactoryBean.setServiceBeans(Arrays.<Object>asList(
                apiListingResource(),
                widgetProxyApi()
        ));
        jaxrsServerFactoryBean.setExtensionMappings(extensionMappings());
        jaxrsServerFactoryBean.setProviders(providers());
        jaxrsServerFactoryBean.setFeatures(Arrays.asList(loggingFeature(), swagger2Feature()));
        return jaxrsServerFactoryBean.create();
    }

    public LoggingFeature loggingFeature() {
        return new LoggingFeature();
    }

    public Swagger2Feature swagger2Feature() {
        Swagger2Feature swagger2Feature = new Swagger2Feature();
        swagger2Feature.setResourcePackage("org.pollanz.samples.api.gateway");
        swagger2Feature.setBasePath("/sample-gateway/rest");
        swagger2Feature.setVersion(env.getProperty("sample_gateway_swagger_version", StringUtils.EMPTY));
        swagger2Feature.setTitle(env.getProperty("sample_gateway_swagger_title", StringUtils.EMPTY));
        swagger2Feature.setDescription(env.getProperty("sample_gateway_swagger_description", StringUtils.EMPTY));
        swagger2Feature.setScan(true);
        swagger2Feature.setScanAllResources(true);
        swagger2Feature.setContact(env.getProperty("sample_gateway_swagger_contact", StringUtils.EMPTY));
        swagger2Feature.setLicense(env.getProperty("sample_gateway_swagger_license", StringUtils.EMPTY));
        swagger2Feature.setLicenseUrl(env.getProperty("sample_gateway_swagger_licenseUrl", StringUtils.EMPTY));
        return swagger2Feature;
    }

    private Map<Object, Object> extensionMappings() {
        final Map map = new HashMap();
        map.put(MediaType.APPLICATION_JSON_TYPE.getSubtype(), MediaType.APPLICATION_JSON);
        map.put(MediaType.APPLICATION_OCTET_STREAM_TYPE.getSubtype(), MediaType.APPLICATION_OCTET_STREAM);
        return map;
    }

    private List<Object> providers() {
        // providers.add(new JodaTimeParamConverterProvider());
        // providers.add(new ExceptionProvider());
        return Arrays.<Object>asList(
                new JacksonJaxbJsonProvider(),
                new SwaggerSerializers(),
                new GatewayExceptionProvider()
        );
    }

    private <T> T create(String contextPath, Class<T> clazz) {
        return JAXRSClientFactory.create(
                env.getProperty("sample_gateway_jaxrsClient_address").concat(contextPath).concat("/rest/"),
                clazz,
                providers()
        );
    }

    @Bean
    public ApiListingResource apiListingResource() {
        return new ApiListingResource();
    }

    @Bean
    @DependsOn("cxf")
    public PetProxiedApi widgetProxyApi() {
        return new PetProxiedApiServiceImpl(proxiedWidgetApi());
    }

    private PetApi proxiedWidgetApi() {
        return create(DA_CORE_CONTEXT_PATH, PetApi.class);
    }

}
