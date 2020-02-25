package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class TodoRestController {

    @Autowired
    InMemoryTodoRepository something;

    @NotBlank(message = "Name cannot be null")
    String author;
    String details;


    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {
        something.create(todo);

        if (todo.getAuthor().isEmpty() || todo.getDetails().isEmpty()){
            return new ResponseEntity<>(todo, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }




/** findTodo*/
    @GetMapping("/todos/{todo}")
    public ResponseEntity<Todo> findTodo(@PathVariable long todo) {

       Optional<Todo> list = something.find(todo);


       if (list.isPresent()) {
           Todo info = list.get();

           return new ResponseEntity<>(info, HttpStatus.OK);
       }
       else {

           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }


    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> findAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {

        List<Todo> list = something.findAll(page, size);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }


    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addTask(@PathVariable long id,
                                        @RequestBody Task task) {

        Todo todo = something.addTask(id, task);

        return new ResponseEntity<>(todo, HttpStatus.CREATED);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable long id) {
        try {
            something.delete(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        catch (NoSuchElementException err) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable long id) {
        try {
            something.deleteTask(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        catch (NoSuchElementException err) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }


}
