package com.fgl.springdatar2dbc.config;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@Profile("h2")
@EnableR2dbcRepositories(basePackages = "com.fgl.springdatar2dbc.repository")
public class H2Configuration extends AbstractR2dbcConfiguration {

    private H2Container h2Container;

    public H2Configuration(H2Container h2Container) {
        this.h2Container = h2Container;
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        H2ConnectionConfiguration config = H2ConnectionConfiguration.builder()
                .url(h2Container.getDatabase())
                .username(h2Container.getUsername())
                .password(h2Container.getPassword())
                .build();
        return new H2ConnectionFactory(config);
    }

    @Bean
    public DatabaseClient databaseClient() {
        return DatabaseClient.builder().connectionFactory(connectionFactory()).build();
    }
}
