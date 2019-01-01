package com.fgl.springdatar2dbc.config;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@Configuration
@Profile("postgre")
@EnableR2dbcRepositories(basePackages = "com.fgl.springdatar2dbc.repository")
public class PostgreConfiguration extends AbstractR2dbcConfiguration {

    private PostgreSQLContainer postgreSQLContainer;

    public PostgreConfiguration(PostgreSQLContainer postgreSQLContainer) {
        this.postgreSQLContainer = postgreSQLContainer;
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        PostgresqlConnectionConfiguration config = PostgresqlConnectionConfiguration.builder()
                .host(postgreSQLContainer.getHost())
                .port(postgreSQLContainer.getPort())
                .database(postgreSQLContainer.getDatabase())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
        return new PostgresqlConnectionFactory(config);
    }

    @Bean
    public DatabaseClient databaseClient() {
        return DatabaseClient.builder().connectionFactory(connectionFactory()).build();
    }

}
