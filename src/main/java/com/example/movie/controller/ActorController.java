package com.example.movie.controller;


import com.example.movie.entity.Actor;
import com.example.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorService actorService;

    @PostMapping
    public Actor createActor(@Valid @RequestBody Actor actor) {
        return actorService.createActor(actor);
    }

    @GetMapping("/{id}")
    public Actor getActorById(@PathVariable Long id) {
        return actorService.getActorById(id);
    }

    @GetMapping
    public List<Actor> getAllActors(@RequestParam(required = false) String name) {
            return actorService.getAllActors();
    }

    @PatchMapping("/{id}")
    public Actor updateActor(@PathVariable Long id, @RequestBody Actor actorDetails) {
        return actorService.updateActor(id, actorDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteActor(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade) {
        actorService.deleteActor(id, cascade);
        if (cascade) {
            return "Actor and their associated movies have been deleted.";
        } else {
            return "Actor has been deleted.";
        }
    }
}
