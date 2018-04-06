package org.pollanz.samples.api.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Slf4j
@DependsOn({"liquibase", "cacheManager"})
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("org.pollanz.samples.api")
@PropertySource(value = "file:///${core_properties_file}", ignoreResourceNotFound = true)
public class PersistenceConfig {

    @Autowired
    private Environment env;

    @Autowired
    private DataSourceConfig dataSourceConfig;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(Properties additionalProperties) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourceConfig.dataSource());
        em.setPackagesToScan("org.pollanz.samples.api");
        em.setPersistenceUnitName("corePU");
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties);
        return em;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean(name = "additionalProperties")
    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("sample_core_hibernate_hbm2ddl_auto", StringUtils.EMPTY));
        properties.setProperty("hibernate.show_sql", env.getProperty("sample_core_hibernate_show_sql", Boolean.FALSE.toString()));
        properties.setProperty("hibernate.dialect", env.getProperty("sample_core_hibernate_dialect"));
        properties.setProperty("hibernate.transaction.manager_lookup_class", env.getProperty("sample_core_hibernate_transaction_manager_lookup_class"));
        properties.setProperty("hibernate.id.new_generator_mappings", env.getProperty("sample_core_hibernate_id_new_generator_mappings", Boolean.TRUE.toString()));
        properties.setProperty("hibernate.generate_statistics", env.getProperty("sample_core_hibernate_generate_statistics", Boolean.FALSE.toString()));

        if (env.getProperty("sample_core_hibernate_use_second_level_cache", Boolean.class, Boolean.FALSE)) {
            log.info("second level cache is enabled");
            properties.setProperty("hibernate.cache.use_second_level_cache", env.getProperty("sample_core_hibernate_use_second_level_cache"));
            properties.setProperty("hibernate.cache.use_query_cache", env.getProperty("sample_core_hibernate_use_query_cache", Boolean.FALSE.toString()));
            properties.setProperty("hibernate.cache.region.factory_class", env.getProperty("sample_core_hibernate_cache_region_factory_class", "com.hazelcast.hibernate.HazelcastCacheRegionFactory"));
            properties.setProperty("hibernate.cache.hazelcast.instance_name", env.getProperty("sample_core_hibernate_cache_hazelcast_instance_name", "sample-cxf-rs-spring-be-core"));
            properties.setProperty("hibernate.cache.use_minimal_puts", env.getProperty("sample_core_hibernate_cache_use_minimal_puts", Boolean.TRUE.toString()));
            properties.setProperty("hibernate.cache.hazelcast.use_lite_member", env.getProperty("sample_core_hibernate_cache_hazelcast_use_lite_member", Boolean.TRUE.toString()));
        }

        return properties;
    }
}
