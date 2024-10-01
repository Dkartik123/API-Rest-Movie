package com.example.movie.controller;

import com.example.movie.entity.Genre;
import com.example.movie.entity.Movie;
import com.example.movie.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping
    public Genre createGenre(@Valid @RequestBody Genre genre) {
        return genreService.createGenre(genre);
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @GetMapping
    public List<Genre> getAllGenre(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer page_size
    ) {
        return genreService.getAllGenres(page, page_size);
    }


    @PatchMapping("/{id}")
    public Genre updateGenre(@PathVariable Long id, @RequestBody Genre genreDetails) {
        return genreService.updateGenre(id, genreDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteGenre(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade) {
        genreService.deleteGenre(id, cascade);
        if (cascade) {
            return "Genre and its associated movies have been deleted.";
        } else {
            return "Genre has been deleted.";
        }
    }
}
