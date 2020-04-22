package com.techtter.blog.scrum.controller;

import com.techtter.blog.scrum.model.Scrum;
import com.techtter.blog.scrum.model.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScrumControllerITCase extends CommonITCase {

    private String baseURL;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setUp(){
        baseURL = "http://localhost:" + port;
    }

    @Test
    public void whenGetAllscrums_thenReceiveSingleScrum(){

        //given
        saveSingleRandomScrum();

        //when
        ResponseEntity<List<Scrum>> response = this.restTemplate.exchange(
                                                baseURL + "scrums/",
                                                    HttpMethod.GET,
                                                    new HttpEntity<>(new HttpHeaders()),
                                                    new ParameterizedTypeReference<List<Scrum>>() {});

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() >= 1);
    }

    @Test
    public void whenGetSingleScrumById_thenReceiveSingleScrum(){

        //given
        Scrum scrum = saveSingleRandomScrum();

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "scrums/" + scrum.getId(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Scrum.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scrum.getId(), response.getBody().getId());
        assertEquals(scrum.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void whenGetAllTasksForScrumById_thenReceiveTasksList(){

        //given
        Scrum scrum = saveSingleScrumWithOneTask();

        //when
        ResponseEntity<List<Task>> response = this.restTemplate.exchange(
                baseURL + "scrums/" + scrum.getId() + "/tasks/",
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                new ParameterizedTypeReference<List<Task>>() {});

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scrum.getTasks().get(0).getId(), response.getBody().get(0).getId());
        assertEquals(scrum.getTasks().get(0).getTitle(), response.getBody().get(0).getTitle());
        assertEquals(scrum.getTasks().get(0).getDescription(), response.getBody().get(0).getDescription());
        assertEquals(scrum.getTasks().get(0).getColor(), response.getBody().get(0).getColor());
    }

    @Test
    public void whenGetSingleScrumByTitle_thenReceiveSingleScrum(){

        //given
        Scrum scrum = saveSingleRandomScrum();

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "scrums?title=" + scrum.getTitle(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                Scrum.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scrum.getId(), response.getBody().getId());
        assertEquals(scrum.getTitle(), response.getBody().getTitle());
    }

    @Test
    public void whenPostSingleScrum_thenItIsStoredInDb(){

        //given
        Scrum scrum = createSingleScrum();

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "scrums/",
                HttpMethod.POST,
                new HttpEntity<>(convertScrumToDTO(scrum), new HttpHeaders()),
                Scrum.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

            // check response Scrum
        Scrum responseScrum = response.getBody();
        assertNotNull(responseScrum.getId());
        assertEquals(scrum.getTitle(), responseScrum.getTitle());

            // check Scrum saved in db
        Scrum savedScrum = findScrumInDbById(responseScrum.getId()).get();
        assertEquals(scrum.getTitle(), savedScrum.getTitle());
    }

    @Test
    public void whenPostSingleTaskToAlreadyCreatedScrum_thenItIsStoredInDbAndAssignedToScrum(){

        //given
        Scrum scrum = saveSingleRandomScrum();
        Task task = createSingleTask();

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "scrums/" + scrum.getId() + "/tasks/",
                HttpMethod.POST,
                new HttpEntity<>(convertTaskToDTO(task), new HttpHeaders()),
                Scrum.class);

        //then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // check response Scrum
        Scrum responseScrum = response.getBody();
        assertNotNull(responseScrum.getId());
        assertEquals(scrum.getTitle(), responseScrum.getTitle());
        assertTrue(responseScrum.getTasks().size() == 1);

        Task responseTask = responseScrum.getTasks().get(0);
        // check response Task
        assertNotNull(responseTask.getId());
        assertEquals(task.getTitle(), responseTask.getTitle());
        assertEquals(task.getDescription(), responseTask.getDescription());
        assertEquals(task.getColor(), responseTask.getColor());
        assertEquals(task.getStatus(), responseTask.getStatus());

        // check saved Task in db
        Task savedTask = findTaskInDbById(responseTask.getId()).get();
        assertEquals(responseTask.getId(), savedTask.getId());
        assertEquals(task.getTitle(), savedTask.getTitle());
        assertEquals(task.getDescription(), savedTask.getDescription());
        assertEquals(task.getColor(), savedTask.getColor());
        assertEquals(task.getStatus(), savedTask.getStatus());
    }


    @Test
    public void whenPutSingleScrum_thenItIsUpdated(){

        //given
        Scrum scrum = saveSingleRandomScrum();
        scrum.setTitle(scrum.getTitle() + " Updated");

        //when
        ResponseEntity<Scrum> response = this.restTemplate.exchange(
                baseURL + "scrums/" + scrum.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(convertScrumToDTO(scrum), new HttpHeaders()),
                Scrum.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(scrum.getTitle(), findScrumInDbById(scrum.getId()).get().getTitle());
    }

    @Test
    public void whenDeleteSingleScrumById_thenItIsDeletedFromDb(){

        //given
        Scrum scrum = saveSingleRandomScrum();

        //when
        ResponseEntity<String> response = this.restTemplate.exchange(
                baseURL + "scrums/" + scrum.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(new HttpHeaders()),
                String.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(String.format("Scrum with id: %d was deleted", scrum.getId()), response.getBody());
        assertFalse(findScrumInDbById(scrum.getId()).isPresent());
    }
}
