package com.example.movie.service.impl;

import com.example.movie.DTO.*;
import com.example.movie.entity.Actor;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.ActorRepository;
import com.example.movie.repository.GenreRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorServiceImpl implements ActorService {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Override
    public ActorResponseDto createActor(ActorRequestDto actorRequestDto) {
        // Создание объекта Actor из DTO
        Actor actor = Actor.builder()
                .name(actorRequestDto.getName())
                .birthDate(actorRequestDto.getBirthDate())
                .build();

        // Проверка и установка фильмов
        List<Movie> movies = new ArrayList<>();
        if (actorRequestDto.getMovieIds() != null && !actorRequestDto.getMovieIds().isEmpty()) {
            movies = movieRepository.findAllById(actorRequestDto.getMovieIds());
            if (movies.size() != actorRequestDto.getMovieIds().size()) {
                throw new ResourceNotFoundException("One or more movies not found");
            }
        }

        actor.setMovies(movies);

        // Сохранение актёра и преобразование в DTO
        return convertToDto(actorRepository.save(actor));
    }

    @Override
    public ActorResponseDto getActorById(Long id) {
        Actor actor = getActorByIdFromDb(id);
        return convertToDto(actor);
    }

    @Override
    public List<ActorResponseDto> getAllActors(Integer page, Integer size) {
        List<Actor> actors = new ArrayList<>();
        if(page!=null && size != null){
            int offset = (page - 1) * size;
            actors = actorRepository.findAllWithPagination(offset, size);}
        else {
            actors = actorRepository.findAll();}
        return actors.stream().map(actor -> convertToDto(actor)).collect(Collectors.toList());
    }
    @Override
    public ActorResponseDto updateActor(Long id, ActorRequestDto actorDetails) {
        Actor actor = getActorByIdFromDb(id);
        if (actorDetails.getName() != null) {
            actor.setName(actorDetails.getName());
        }
        if (actorDetails.getBirthDate() != null) {
            actor.setBirthDate(actorDetails.getBirthDate());
        }
        List<Movie> movies = movieRepository.findAllById(actorDetails.getMovieIds());
        if(movies.isEmpty()){
            throw new ResourceNotFoundException("Movie not found");
        }
        actor.setMovies(movies);
        return convertToDto(actorRepository.save(actor));
    }

    @Override
    public void deleteActor(Long id, boolean cascade) {
        Actor actor = getActorByIdFromDb(id);
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

    private Actor getActorByIdFromDb(Long id) {
        return actorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Actor not found"));
    }



    private ActorResponseDto convertToDto(Actor actor) {
        return ActorResponseDto.builder()
                .id(actor.getId())
                .name(actor.getName())
                .birthDate(actor.getBirthDate())
                .build();
    }
}
