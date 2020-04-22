package com.techtter.blog.scrum.controller;

import com.techtter.blog.scrum.model.Scrum;
import com.techtter.blog.scrum.model.ScrumDTO;
import com.techtter.blog.scrum.model.Task;
import com.techtter.blog.scrum.model.TaskDTO;
import com.techtter.blog.scrum.model.TaskStatus;
import com.techtter.blog.scrum.repository.ScrumRepository;
import com.techtter.blog.scrum.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Optional;

@TestPropertySource( properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect"
})
public class CommonITCase {

    @Autowired
    private ScrumRepository scrumRepository;

    @Autowired
    private TaskRepository taskRepository;

    protected Scrum createSingleScrum(){
        Scrum scrum = new Scrum();
        int random = (int)(Math.random() * 100 + 1);
        scrum.setTitle("Test Scrum " + random);
        scrum.setTasks(new ArrayList<>());
        return scrum;
    }

    protected Task createSingleTask(){
        Task task = new Task();
        int random = (int)(Math.random() * 100 + 1);
        task.setTitle("Test Task " + random);
        task.setDescription("Description " + random);
        task.setColor("Color " + random);
        task.setStatus(TaskStatus.TODO);
        return task;
    }

    protected ScrumDTO convertScrumToDTO(Scrum scrum) {
        return new ScrumDTO().builder()
                .title(scrum.getTitle())
                .build();
    }

    protected TaskDTO convertTaskToDTO(Task task) {
        return new TaskDTO().builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .color(task.getColor())
                .status(task.getStatus())
                .build();
    }

    protected Scrum saveSingleRandomScrum(){
        return scrumRepository.save(createSingleScrum());
    }

    protected Scrum saveSingleScrumWithOneTask(){
        Scrum scrum = createSingleScrum();
        Task task = createSingleTask();
        scrum.addTask(task);
        return scrumRepository.save(scrum);
    }

    protected Task saveSingleTask(){
        return taskRepository.save(createSingleTask());
    }

    protected Optional<Scrum> findScrumInDbById(Long id) {
        return scrumRepository.findById(id);
    }

    protected Optional<Task> findTaskInDbById(Long id) {
        return taskRepository.findById(id);
    }
}
