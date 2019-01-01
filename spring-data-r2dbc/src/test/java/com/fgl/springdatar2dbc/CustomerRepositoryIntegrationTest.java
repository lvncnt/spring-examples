package com.fgl.springdatar2dbc;

import com.fgl.springdatar2dbc.config.PostgreConfiguration;
import com.fgl.springdatar2dbc.model.Customer;
import com.fgl.springdatar2dbc.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryIntegrationTest {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired DatabaseClient databaseClient;

    @Before
    public void setup() {
        List<String> statements = Arrays.asList(//
                "DROP TABLE IF EXISTS customer;",
                "CREATE TABLE customer ( id SERIAL PRIMARY KEY, firstname VARCHAR(100) NOT NULL, lastname VARCHAR(100) NOT NULL);");
        statements.forEach(sql -> databaseClient.execute()
                .sql(sql)
                .fetch().all().subscribe());
    }

    @Test
    public void executesFindAll() {
        Customer dave = new Customer(null, "Dave", "Matthews");
        Customer carter = new Customer(null, "Carter", "Beauford");
        Customer dan = new Customer(null, "Dan", "Matt");

        insertCustomer(dave, dan, carter);

        customerRepository.findAll()
                .as(StepVerifier::create)
                .assertNext(dave::equals)
                .assertNext(carter::equals)
                .assertNext(dan::equals)
                .verifyComplete();
    }

    @Test
    public void executesFindByFirstnameAndLastname() {
        Customer dave = new Customer(null, "Dave", "Matthews");
        Customer carter = new Customer(null, "Carter", "Beauford");
        Customer dan = new Customer(null, "Dan", "Matt");

        insertCustomer(dave, dan, carter);

        customerRepository.findByFirstnameAndLastname("Dan", "Matt")
                .as(StepVerifier::create)
                .assertNext(dan::equals)
                .verifyComplete();
    }

    @Test
    public void executesFindByLastnameLike() {
        Customer dave = new Customer(null, "Dave", "Matthews");
        Customer carter = new Customer(null, "Carter", "Beauford");
        Customer dan = new Customer(null, "Dan", "Matt");

        insertCustomer(dave, dan, carter);

        customerRepository.findByLastnameLike("Matt")
                .as(StepVerifier::create)
                .assertNext(dave::equals)
                .assertNext(dan::equals)
                .verifyComplete();
    }

    private void insertCustomer(Customer... customers) {
        this.customerRepository.saveAll(Arrays.asList(customers))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }
}
