package com.techtter.blog.scrum.controller;

import com.techtter.blog.scrum.model.Scrum;
import com.techtter.blog.scrum.model.ScrumDTO;
import com.techtter.blog.scrum.model.Task;
import com.techtter.blog.scrum.model.TaskDTO;
import com.techtter.blog.scrum.service.ScrumService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/scrums")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ScrumController {

    private final ScrumService scrumService;

    @GetMapping("/")
    @ApiOperation(value="View a list of all Scrum boards", response = Scrum.class, responseContainer = "List")
    public ResponseEntity<?> getAllScrums(){
        try {
            return new ResponseEntity<>(
                    scrumService.getAllScrumBoards(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Find a Scrum board info by its id", response = Scrum.class)
    public ResponseEntity<?> getScrum(@PathVariable Long id){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumById(id);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        optScrum.get(),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("")
    @ApiOperation(value="Find a Scrum board info by its title", response = Scrum.class)
    public ResponseEntity<?> getScrumByTitle(@RequestParam String title){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumByTitle(title);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        optScrum.get(),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No scrum found with a title: " + title, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/")
    @ApiOperation(value="Save new Scrum board", response = Scrum.class)
    public ResponseEntity<?> createScrum(@RequestBody ScrumDTO scrumDTO){
        try {
            return new ResponseEntity<>(
                    scrumService.saveNewScrum(scrumDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value="Update a Scrum board with specific id", response = Scrum.class)
    public ResponseEntity<?> updateScrum(@PathVariable Long id, @RequestBody ScrumDTO scrumDTO){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumById(id);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        scrumService.updateScrum(optScrum.get(), scrumDTO),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value="Delete Scrum board with specific id", response = String.class)
    public ResponseEntity<?> deleteScrum(@PathVariable Long id){
        try {
            Optional<Scrum> optScrum = scrumService.getScrumById(id);
            if (optScrum.isPresent()) {
                scrumService.deleteScrum(optScrum.get());
                return new ResponseEntity<>(
                        String.format("Scrum with id: %d was deleted", id),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(id);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @GetMapping("/{scrumId}/tasks/")
    @ApiOperation(value="View a list of all tasks for a Scrum with provided id", response = Task.class, responseContainer = "List")
    public ResponseEntity<?> getAllTasksInScrum(@PathVariable Long scrumId){
         try {
            Optional<Scrum> optScrum = scrumService.getScrumById(scrumId);
            if (optScrum.isPresent()) {
                return new ResponseEntity<>(
                        optScrum.get().getTasks(),
                        HttpStatus.OK);
            } else {
                return noScrumFoundResponse(scrumId);
            }
        } catch (Exception e) {
            return errorResponse();
        }
    }

    @PostMapping("/{scrumId}/tasks/")
    @ApiOperation(value="Save new Task and assign it to Scrum board", response = Scrum.class)
    public ResponseEntity<?> createTaskAssignedToScrum(@PathVariable Long scrumId, @RequestBody TaskDTO taskDTO){
        try {
            return new ResponseEntity<>(
                    scrumService.addNewTaskToScrum(scrumId, taskDTO),
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return errorResponse();
        }
    }

    private ResponseEntity<String> errorResponse(){
        return new ResponseEntity<>("Something went wrong :(", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<String> noScrumFoundResponse(Long id){
        return new ResponseEntity<>("No scrum found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
