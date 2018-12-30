package com.fgl.todoapp;

import com.fgl.todoapp.model.Todo;
import com.fgl.todoapp.repository.TodoRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Log4j2
@Component
@Profile("demo")
public class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private TodoRepository todoRepository;

    public SampleDataInitializer(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        this.todoRepository.deleteAll();
        Stream.of("Pay car insurance", "Play more Skyrim", "Remove material icon")
                .map(name -> Todo.builder().name(name).build())
                .forEach(todo -> log.info("saving " + todoRepository.save(todo)));
    }
}
