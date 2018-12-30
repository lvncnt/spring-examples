package com.fgl.todoapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fgl.todoapp.AppConfiguration
import com.fgl.todoapp.model.Todo
import com.fgl.todoapp.repository.TodoRepository
import lombok.extern.log4j.Log4j2
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll
import spock.mock.DetachedMockFactory
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static groovy.json.JsonOutput.toJson

@Log4j2
@WebMvcTest(controllers = [TodoController]) 
@Import([AppConfiguration])
class TodoControllerSpec extends Specification{

    @Autowired protected MockMvc mockMvc
 
    @Autowired TodoRepository todoRepository

    @Autowired ObjectMapper objectMapper

    def "should return welcome message" () {
        when: "a rest api call is performed to about page"
        def response = mockMvc.perform(get("/api"))
                            .andReturn().response

        then: "the correct message is expected"
        response.status == HttpStatus.OK.value()

        and:
        response.contentAsString == "welcome to todoapp api"
    }

    def "should return all todos" () {
        given: "a list of todos"
            def todos = ['todo1', 'todo2']
                        .collect({Todo.builder()
                        .id(UUID.randomUUID().toString())
                        .name(it).build()
            })

            def sortByCreatedDesc = new Sort(Sort.Direction.DESC, "created")

        and:
            todoRepository.findAll(sortByCreatedDesc) >> todos

        when: "get all todos"
            def response = mockMvc.perform(get("/api/todo"))
                        .andReturn().response

        then: "status is 200"
            response.status == HttpStatus.OK.value()

        and: "body contains proper values"
        with(objectMapper.readValue(response.contentAsString, List)) {
            it.size() == 2
            it.find {it.name == 'todo1'} != null
            it.find {it.name == 'todo2'} != null
        }
    }

    def "should return todo by id" () {
        given: "a todo"
        def todo2 = Todo.builder()
                .id(UUID.randomUUID().toString())
                .name('todo2').build()

        and:
        todoRepository.findById(todo2.id) >> Optional.of(todo2)

        when:
        def response = mockMvc.perform(get("/api/todo/${todo2.id}")
                            .accept(MediaType.APPLICATION_JSON))
                            .andReturn().response

        then:
        response.status == HttpStatus.OK.value()

        and:
        with(objectMapper.readValue(response.contentAsString, Map)) {
            it.name == todo2.name
            it.id == todo2.id
            it.completed == todo2.completed
        }
    }

    def "should create todo and return created todo" () {
        given:
            def todo2 = Todo.builder()
                .id(UUID.randomUUID().toString())
                .name('todo2').build()

        and:
            todoRepository.save(todo2) >> todo2

        when:
        def response = mockMvc.perform(post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo2)))
                .andReturn().response

        then:
        response.status == HttpStatus.OK.value()

        and:
        with(objectMapper.readValue(response.contentAsString, Map)) {
            it.name == todo2.name
            it.completed == todo2.completed
        }
    }

    @Unroll
    def "should not allow to create todo with invalid name: #name" () {
        given: "a todo"
        def todo2 = Todo.builder()
                .id(UUID.randomUUID().toString())
                .name(name).build()

        when:
        def response = mockMvc.perform(post("/api/todo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todo2)))
                .andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value()

        and:
        with(objectMapper.readValue(response.contentAsString, List)) {
            it.contains(errorMessage)
        }

        where:
        name | errorMessage
        null | "Name must not be blank"
        ""  | "Name must not be blank"
        " "  | "Name must not be blank"
        "*".multiply(120) | "Name size must be between 0 and 100"
    }
 
    @TestConfiguration
    static class StubConfig {
        protected DetachedMockFactory detachedMockFactory = new DetachedMockFactory();

        @Bean
        TodoRepository todoRepository() {
            detachedMockFactory.Stub(TodoRepository)
        }
    }
}
