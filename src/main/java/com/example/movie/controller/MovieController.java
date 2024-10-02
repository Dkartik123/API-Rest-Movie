package com.example.movie.controller;

import com.example.movie.DTO.MovieRequestDto;
import com.example.movie.DTO.MovieResponseDto;
import com.example.movie.entity.Movie;
import com.example.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public MovieResponseDto createMovie(@Valid @RequestBody MovieRequestDto movieRequestDto) {
        return movieService.createMovie(movieRequestDto);
    }

    @GetMapping("/{id}")
    public MovieResponseDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @GetMapping
    public List<MovieResponseDto> getAllMovies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
            return movieService.getAllMovies(page, size);
        }


    @PatchMapping("/{id}")
    public MovieResponseDto updateMovie(@PathVariable Long id, @RequestBody MovieRequestDto movieDetails) {
        return movieService.updateMovie(id, movieDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade) {
        movieService.deleteMovie(id, cascade);
        if (cascade) {
            return "Movie and its associated data have been deleted.";
        } else {
            return "Movie has been deleted.";
        }
    }
}
