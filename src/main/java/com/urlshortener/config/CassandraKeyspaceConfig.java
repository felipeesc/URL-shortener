package com.urlshortener.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class CassandraKeyspaceConfig {

    @Bean
    public CqlSessionBuilderCustomizer keyspaceCreator(CassandraProperties cassandraProperties) {
        return builder -> {
            String host = cassandraProperties.getContactPoints().get(0);
            int port = cassandraProperties.getPort();
            String localDc = cassandraProperties.getLocalDatacenter();
            String keyspaceName = cassandraProperties.getKeyspaceName();

            try (CqlSession session = CqlSession.builder()
                    .addContactPoint(new InetSocketAddress(host, port))
                    .withLocalDatacenter(localDc)
                    .build()) {
                session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspaceName
                    + " WITH replication = {'class':'SimpleStrategy','replication_factor':1}");
            }
        };
    }
}
