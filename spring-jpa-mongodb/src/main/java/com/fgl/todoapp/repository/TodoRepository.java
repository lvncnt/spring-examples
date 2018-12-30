package com.fgl.todoapp.repository;

import com.fgl.todoapp.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {
}
