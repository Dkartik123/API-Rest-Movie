package com.example.movie.controller;

import com.example.movie.entity.Movie;
import com.example.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public Movie createMovie(@Valid @RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @GetMapping
    public List<Movie> getAllMovies(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer page_size
    ) {
            return movieService.getAllMovies(page, page_size);
        }


    @PatchMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
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
