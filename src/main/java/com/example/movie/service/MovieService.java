package com.example.movie.service;

import com.example.movie.DTO.MovieRequestDto;
import com.example.movie.DTO.MovieResponseDto;
import com.example.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    MovieResponseDto createMovie(MovieRequestDto movieRequestDto);
    MovieResponseDto getMovieById(Long id);
    List<MovieResponseDto> getAllMovies(Integer pageNo, Integer pageSize);
    MovieResponseDto updateMovie(Long id, MovieRequestDto movieDetails);
    void deleteMovie(Long id, boolean cascade);
    Page<Movie> getMoviesByGenre(Long genreId, Pageable pageable);
    Page<Movie> getMoviesByReleaseYear(int releaseYear, Pageable pageable);
    Page<Movie> getMoviesByActor(Long actorId, Pageable pageable);
    Page<MovieResponseDto> searchMoviesByTitle(String title, Pageable pageable);
}
