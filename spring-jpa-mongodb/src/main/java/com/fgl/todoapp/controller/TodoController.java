package com.fgl.todoapp.controller;

import com.fgl.todoapp.model.Todo;
import com.fgl.todoapp.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
@AllArgsConstructor
public class TodoController {

    private TodoRepository todoRepository;
 
    @GetMapping("")
    public String about() {
        return "welcome to todoapp api";
    }

    @GetMapping("/todo")
    public List<Todo> getAll() {
        Sort sortByCreatedDesc = new Sort(Sort.Direction.DESC, "created");
        return todoRepository.findAll(sortByCreatedDesc);
    }

    @PostMapping("/todo")
    public Todo createTodo(@Valid @RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    @GetMapping(value = "/todo/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") String id) {
        return todoRepository.findById(id)
                .map(todo -> ResponseEntity.ok().body(todo))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/todo/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id,
                                           @Valid @RequestBody Todo todo) {
        return todoRepository.findById(id)
                .map(todoExisting -> {
                    todoExisting.setName(todo.getName());
                    todoExisting.setCompleted(todo.getCompleted());
                    return ResponseEntity.ok().body(todoRepository.save(todoExisting));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/todo/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
    }
}
