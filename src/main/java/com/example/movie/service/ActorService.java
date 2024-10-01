package com.example.movie.service;

import com.example.movie.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ActorService {

    Actor createActor(Actor actor);
    Actor getActorById(Long id);
    List<Actor> getAllActors();
    Actor updateActor(Long id, Actor actorDetails);
    void deleteActor(Long id, boolean cascade);
    Page<Actor> searchActorsByName(String name, Pageable pageable);
}
