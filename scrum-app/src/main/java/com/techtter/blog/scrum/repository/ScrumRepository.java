package com.techtter.blog.scrum.repository;

import com.techtter.blog.scrum.model.Scrum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScrumRepository extends CrudRepository<Scrum, Long> {

    Optional<Scrum> findByTitle(String title);
}
