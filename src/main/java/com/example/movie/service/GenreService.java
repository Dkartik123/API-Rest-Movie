package com.example.movie.service;

import com.example.movie.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenreService {
    Genre createGenre(Genre genre);
    Genre getGenreById(Long id);
    List<Genre> getAllGenres(Integer pageNo1, Integer pageSize2);
    Genre updateGenre(Long id, Genre genreDetails);
    void deleteGenre(Long id, boolean cascade);
}
