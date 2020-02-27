package com.cybr406.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    TodoJpaRepository todoRepositoryJPA;

    @Autowired
    TodoRepository todoRepository;

    @Autowired
    TaskJpaRepository taskJpaRepository;

    @NotBlank(message = "Name cannot be null")
    String author;
    String details;



    @PostMapping("/todos")
    public ResponseEntity<Todo> createTodo(@Valid @RequestBody Todo todo) {

        Todo created = todoRepositoryJPA.save(todo);
        return new ResponseEntity<>(created, HttpStatus.CREATED);

    }


/** findTodo*/
    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> findTodo(@PathVariable Long id) {

       Optional<Todo> list = todoRepositoryJPA.findById(id);


       if (list.isPresent()) {
           Todo info = list.get();

           return new ResponseEntity<>(info, HttpStatus.OK);
       }
       else {

           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
    }


    @GetMapping("/todos")
    public Page<Todo> findAll(Pageable page) {

        return todoRepositoryJPA.findAll(page);
    }


//    @PostMapping("/authors")
//    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
//        Author created = authorRepository.save(author);
//        return new ResponseEntity<>(created, HttpStatus.CREATED);
//    }

    @PostMapping("/todos/{id}/tasks")
    public ResponseEntity<Todo> addTask(@PathVariable long id,
                                        @RequestBody Task task) {

        Optional<Todo> addingTask = todoRepositoryJPA.findById(id);

        if (addingTask.isPresent()) {
            Todo todo = addingTask.get();
            todo.getTasks().add(task);
            task.setTodo(todo);
            taskJpaRepository.save(task);
            return new ResponseEntity<>(todo, HttpStatus.CREATED);}
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable Long id) {


        if(todoRepositoryJPA.existsById(id)){
            todoRepositoryJPA.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }


//        try {
//            //todoRepositoryJPA.deleteById(id); return 404 remove try catch - check if exists
//            something.delete(id);
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//        catch (NoSuchElementException err) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }

    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable Long id) {

        if(taskJpaRepository.existsById(id)){
            taskJpaRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

//        try {
//            //Optional<Task> task = taskJpaRepository.findById(id);
//            something.deleteTask(id);
//            return new ResponseEntity(HttpStatus.NO_CONTENT);
//        }
//        catch (NoSuchElementException err) {
//            return new ResponseEntity(HttpStatus.NOT_FOUND);
//        }
    }


}
