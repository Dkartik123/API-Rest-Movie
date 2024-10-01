package com.example.movie.service.impl;

import com.example.movie.entity.Actor;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.ActorRepository;
import com.example.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorRepository actorRepository;

    @Override
    public Actor createActor(Actor actor) {
        return actorRepository.save(actor);
    }

    @Override
    public Actor getActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
    }

    @Override
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }
    @Override
    public Actor updateActor(Long id, Actor actorDetails) {
        Actor actor = getActorById(id);
        if (actorDetails.getName() != null) {
            actor.setName(actorDetails.getName());
        }
        if (actorDetails.getBirthDate() != null) {
            actor.setBirthDate(actorDetails.getBirthDate());
        }
        if (actorDetails.getMovies() != null) {
            actor.setMovies(actorDetails.getMovies());
        }
        return actorRepository.save(actor);
    }

    @Override
    public void deleteActor(Long id, boolean cascade) {
        Actor actor = getActorById(id);
        if (!cascade && (actor.getMovies() != null && !actor.getMovies().isEmpty())) {
            throw new RuntimeException("Cannot delete actor '" + actor.getName() + "' because they are associated with "
                    + actor.getMovies().size() + " movies.");
        }
        actorRepository.delete(actor);
    }

    @Override
    public Page<Actor> searchActorsByName(String name, Pageable pageable) {
        return actorRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
