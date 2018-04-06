package org.pollanz.samples.api.core.spring;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableCaching
@PropertySource(value = "file:///${core_properties_file}", ignoreResourceNotFound = true)
public class CacheConfig {

    @Autowired
    private Environment environment;

    @PreDestroy
    public void destroy() {
        log.info("Closing Cache Manager");
        Hazelcast.shutdownAll();
    }

    @Bean
    public CacheManager cacheManager(HazelcastInstance hazelcastInstance) {
        log.debug("Starting HazelcastCacheManager");
        CacheManager cacheManager = new HazelcastCacheManager(hazelcastInstance);
        return cacheManager;
    }

    @Bean
    public HazelcastInstance hazelcastInstance() {
        log.debug("Configuring Hazelcast");
        HazelcastInstance hazelCastInstance = Hazelcast.getHazelcastInstanceByName("sample-cxf-rs-spring-be-core");
        if (hazelCastInstance != null) {
            log.debug("Hazelcast already initialized");
            return hazelCastInstance;
        }
        Config config = new Config();
        config.setInstanceName("sample-cxf-rs-spring-be-core");
        config.getNetworkConfig().setPort(environment.getProperty("sample_core_cache_network_port", Integer.class, 5702));
        config.getNetworkConfig().setPortAutoIncrement(environment.getProperty("sample_core_cache_network_port_autoIncrement", Boolean.class, Boolean.FALSE));

        // In development, remove multicast auto-configuration
        if (environment.getProperty("sample_core_cache_localNetwork_enabled", Boolean.class, Boolean.FALSE)) {
            System.setProperty("hazelcast.local.localAddress", "127.0.0.1");
            config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        } else {
            config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.getNetworkConfig().getJoin().getTcpIpConfig().setMembers(members());
        }

//        config.getMapConfigs().put("org.pollanz.samples.api.core.domain.*", initializeDomainMapConfig());

        // Full reference is available at: http://docs.hazelcast.org/docs/management-center/3.9/manual/html/Deploying_and_Starting.html
        config.setManagementCenterConfig(initializeDefaultManagementCenterConfig());
        return Hazelcast.newHazelcastInstance(config);
    }

    private ManagementCenterConfig initializeDefaultManagementCenterConfig() {
        ManagementCenterConfig managementCenterConfig = new ManagementCenterConfig();
        managementCenterConfig.setEnabled(environment.getProperty("sample_core_cache_managementCenterConfig_enabled", Boolean.class, Boolean.FALSE));
        managementCenterConfig.setUrl(environment.getProperty("sample_core_cache_managementCenterConfig_url", StringUtils.EMPTY));
        managementCenterConfig.setUpdateInterval(environment.getProperty("sample_core_cache_managementCenterConfig_updateInterval", Integer.class, 3));
        return managementCenterConfig;
    }

    private MapConfig initializeDefaultMapConfig() {
        MapConfig mapConfig = new MapConfig();

    /*
        Number of backups. If 1 is set as the backup-count for example,
        then all entries of the map will be copied to another JVM for
        fail-safety. Valid numbers are 0 (no backup), 1, 2, 3.
     */
        mapConfig.setBackupCount(0);

    /*
        Valid values are:
        NONE (no eviction),
        LRU (Least Recently Used),
        LFU (Least Frequently Used).
        NONE is the default.
     */
        mapConfig.setEvictionPolicy(EvictionPolicy.LRU);

    /*
        Maximum size of the map. When max size is reached,
        map is evicted based on the policy defined.
        Any integer between 0 and Integer.MAX_VALUE. 0 means
        Integer.MAX_VALUE. Default is 0.
     */
        mapConfig.setMaxSizeConfig(new MaxSizeConfig(0, MaxSizeConfig.MaxSizePolicy.USED_HEAP_SIZE));

        mapConfig.setTimeToLiveSeconds(environment.getProperty("sample_core_cache_default_timeToLiveSeconds", Integer.class, 60));

        return mapConfig;
    }

//    private MapConfig initializeDomainMapConfig() {
//        MapConfig mapConfig = new MapConfig();
//        mapConfig.setTimeToLiveSeconds(environment.getProperty("sample_core_cache_domain_timeToLiveSeconds", Integer.class, 300));
//        return mapConfig;
//    }

    private List<String> members() {
        final String members = environment.getProperty("sample_core_cache_members", "127.0.0.1");
        if (members.contains(":")) {
            throw new IllegalArgumentException("Port must not be set in cache members properties");
        }
        final String[] membersArray = members.split(",");
        return Arrays.asList(membersArray);
    }

}
