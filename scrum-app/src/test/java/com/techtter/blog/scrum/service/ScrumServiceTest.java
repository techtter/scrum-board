package com.techtter.blog.scrum.service;

import com.techtter.blog.scrum.model.Scrum;
import com.techtter.blog.scrum.repository.ScrumRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScrumServiceTest {

    ScrumService scrumService;
    @Mock
    ScrumRepository scrumRepository;

    @Before
    public void init() {
        scrumService = new ScrumServiceImpl(scrumRepository);
    }

    @Test
    public void when2ScrumsInDatabase_thenGetListWithAllOfThem() {
        //given
        mockScrumInDatabase(2);

        //when
        List<Scrum> scrums = scrumService.getAllScrumBoards();

        //then
        assertEquals(2, scrums.size());
    }

    private void mockScrumInDatabase(int scrumCount) {
        when(scrumRepository.findAll())
                .thenReturn(createScrumList(scrumCount));
    }

    private List<Scrum> createScrumList(int scrumCount) {
        List<Scrum> scrums = new ArrayList<>();
        IntStream.range(0, scrumCount)
                .forEach(number ->{
                    Scrum scrum = new Scrum();
                    scrum.setId(Long.valueOf(number));
                    scrum.setTitle("Scrum " + number);
                    scrum.setTasks(new ArrayList<>());
                    scrums.add(scrum);
                });
        return scrums;
    }
}
