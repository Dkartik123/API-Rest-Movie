package com.example.movie.service;

import com.example.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    Movie createMovie(Movie movie);
    Movie getMovieById(Long id);
    List<Movie> getAllMovies(Integer pageNo, Integer pageSize);
    Movie updateMovie(Long id, Movie movieDetails);
    void deleteMovie(Long id, boolean cascade);
    Page<Movie> getMoviesByGenre(Long genreId, Pageable pageable);
    Page<Movie> getMoviesByReleaseYear(int releaseYear, Pageable pageable);
    Page<Movie> getMoviesByActor(Long actorId, Pageable pageable);
    Page<Movie> searchMoviesByTitle(String title, Pageable pageable);
}
