package org.pollanz.samples.api.core.spring;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("org.pollanz.samples.api")
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
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("sample.core.hibernate.hbm2ddl.auto", StringUtils.EMPTY));
        properties.setProperty("hibernate.show_sql", env.getProperty("sample.core.hibernate.show_sql", Boolean.FALSE.toString()));
        properties.setProperty("hibernate.dialect", env.getProperty("sample.core.hibernate.dialect"));
        properties.setProperty("hibernate.transaction.manager_lookup_class", env.getProperty("sample.core.hibernate.transaction.manager_lookup_class"));
        properties.setProperty("hibernate.id.new_generator_mappings", env.getProperty("sample.core.hibernate.id.new_generator_mappings", Boolean.FALSE.toString()));
        return properties;
    }
}
