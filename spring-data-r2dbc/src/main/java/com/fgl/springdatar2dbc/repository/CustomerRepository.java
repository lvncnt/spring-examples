package com.fgl.springdatar2dbc.repository;

import com.fgl.springdatar2dbc.model.Customer;
import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

    @Query("SELECT * FROM customer c WHERE c.lastname LIKE $1||'%'")
    Flux<Customer> findByLastnameLike(String lastname);

    @Query("SELECT * FROM customer WHERE firstname = $1 AND lastname = $2")
    Mono<Customer> findByFirstnameAndLastname(String firstname, String lastname);
}
