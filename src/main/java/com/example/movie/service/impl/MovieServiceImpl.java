package com.example.movie.service.impl;

import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.MovieRepository;
import com.example.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    @Override
    public List<Movie> getAllMovies(Integer page, Integer size) {
        int offset = page * size;

        var movies = movieRepository.findMoviesWithPagination(size, offset);
        return movies;
    }

    @Override
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        if (movieDetails.getTitle() != null) {
            movie.setTitle(movieDetails.getTitle());
        }
        if (movieDetails.getReleaseYear() != 0) {
            movie.setReleaseYear(movieDetails.getReleaseYear());
        }
        if (movieDetails.getDuration() != 0) {
            movie.setDuration(movieDetails.getDuration());
        }
        if (movieDetails.getGenre() != null) {
            movie.setGenre(movieDetails.getGenre());
        }
        if (movieDetails.getActors() != null) {
            movie.setActors(movieDetails.getActors());
        }
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(Long id, boolean cascade) {
        Movie movie = getMovieById(id);
        if (!cascade && (movie.getActors() != null && !movie.getActors().isEmpty())) {
            throw new RuntimeException("Cannot delete movie '" + movie.getTitle() + "' because it has "
                    + movie.getActors().size() + " associated actors.");
        }
        movieRepository.delete(movie);
    }

    @Override
    public Page<Movie> getMoviesByGenre(Long genreId, Pageable pageable) {
        return movieRepository.findByGenreId(genreId, pageable);
    }

    @Override
    public Page<Movie> getMoviesByReleaseYear(int releaseYear, Pageable pageable) {
        return movieRepository.findByReleaseYear(releaseYear, pageable);
    }

    @Override
    public Page<Movie> getMoviesByActor(Long actorId, Pageable pageable) {
        return movieRepository.findByActorsId(actorId, pageable);
    }

    @Override
    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
}
