package com.example.movie.controller;


import com.example.movie.DTO.ActorRequestDto;
import com.example.movie.DTO.ActorResponseDto;
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
    public ActorResponseDto createActor(@Valid @RequestBody ActorRequestDto actorRequestDto) {
        return actorService.createActor(actorRequestDto);
    }

    @GetMapping("/{id}")
    public ActorResponseDto getActorById(@PathVariable Long id) {
        return actorService.getActorById(id);
    }

    @GetMapping
    public List<ActorResponseDto> getAllActors(
            @RequestParam(required = false)Integer page,
            @RequestParam(required = false)Integer size)
    {
            return actorService.getAllActors(page, size);
    }

    @PatchMapping("/{id}")
    public ActorResponseDto updateActor(@PathVariable Long id, @RequestBody ActorRequestDto actorDetails) {
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
