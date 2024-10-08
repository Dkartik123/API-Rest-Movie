package com.example.movie.service.impl;

import com.example.movie.DTO.*;
import com.example.movie.entity.Actor;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.ActorRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.repository.GenreRepository;
import com.example.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    // Method to create a new Movie
    @Override
    public MovieResponseDto createMovie(MovieRequestDto movieRequestDto) {
        // Create and save new movie instance, assuming Genre and Actors already exist
        Movie movie = Movie.builder()
                .title(movieRequestDto.getTitle())
                .releaseYear(movieRequestDto.getReleaseYear())
                .duration(movieRequestDto.getDuration())
                .genre(genreRepository.findById((long) movieRequestDto.getGenreId())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + movieRequestDto.getGenreId())))
                .actors(actorRepository.findAllById(movieRequestDto.getActorIds()))
                .build();

        // Save the movie to the repository and convert it to response DTO
        return convertMovieToDto(movieRepository.save(movie));
    }

    // Method to get a movie by its ID
    @Override
    public MovieResponseDto getMovieById(Long id) {
        Movie movie = getMovieByIdFromDb(id);
        return convertMovieToDto(movie);
    }

    // Method to get all movies with optional pagination
    @Override
    public List<MovieResponseDto> getAllMovies(Integer page, Integer size) {
        // Placeholder for getting all movies - modify as needed
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(this::convertMovieToDto).collect(Collectors.toList());
    }

    // Method to update an existing movie
    @Override
    public MovieResponseDto updateMovie(Long id, MovieRequestDto movieDetails) {
        Movie movie = getMovieByIdFromDb(id);

        // Update movie fields if present in request
        if (movieDetails.getTitle() != null) {
            movie.setTitle(movieDetails.getTitle());
        }
        if (movieDetails.getReleaseYear() != 0) {
            movie.setReleaseYear(movieDetails.getReleaseYear());
        }
        if (movieDetails.getDuration() != 0) {
            movie.setDuration(movieDetails.getDuration());
        }
        if (movieDetails.getGenreId() != 0) {
            movie.setGenre(genreRepository.findById((long) movieDetails.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + movieDetails.getGenreId())));
        }
        if (movieDetails.getActorIds() != null && !movieDetails.getActorIds().isEmpty()) {
            movie.setActors(actorRepository.findAllById(movieDetails.getActorIds()));
        }

        // Save and return updated movie
        return convertMovieToDto(movieRepository.save(movie));
    }

    // Method to delete a movie by ID
    @Override
    public void deleteMovie(Long id, boolean cascade) {
        Movie movie = getMovieByIdFromDb(id);
        if (!cascade && (movie.getActors() != null && !movie.getActors().isEmpty())) {
            throw new RuntimeException("Cannot delete movie with actors associated. Set cascade=true to delete.");
        }
        movieRepository.delete(movie);
    }

    // Method to search movies by title with pagination
    @Override
    public Page<MovieResponseDto> searchMoviesByTitle(String title, Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        // Convert Page<Movie> to Page<MovieResponseDto>
        return moviesPage.map(this::convertMovieToDto);
    }
    @Override
    public List<MovieResponseDto> getMoviesByActorId(Long actorId) {
        Actor actor = getActorByIdFromDb(actorId);  // Fetch actor or throw error if not found

        // Convert List<Movie> to List<MovieResponseDto> and return it
        return actor.getMovies().stream()
                .map(this::convertMovieToDto)
                .collect(Collectors.toList());
    }
    private Actor getActorByIdFromDb(Long id) {
        return actorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Actor not found"));
    }
    // Method to get movies by release year with pagination
    @Override
    public Page<MovieResponseDto> getMoviesByReleaseYear(int releaseYear, Pageable pageable) {
        Page<Movie> moviesPage = movieRepository.findByReleaseYear(releaseYear, pageable);
        // Convert Page<Movie> to Page<MovieResponseDto>
        return moviesPage.map(this::convertMovieToDto);
    }

    // Method to get all actors in a specific movie by its ID
    @Override
    public List<ActorResponseDto> getAllActorsInMovie(Long movieId) {
        Movie movie = getMovieByIdFromDb(movieId);
        return movie.getActors().stream()
                .map(this::convertActorToDto)
                .collect(Collectors.toList());
    }

    // Method to get all movies by a specific genre ID with pagination
    @Override
    public Page<MovieResponseDto> getMoviesByGenreId(Long genreId, Pageable pageable) {
        // Check if the genre exists; if not, throw an exception
        genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + genreId));

        // Find all movies by the specified genre ID with pagination
        Page<Movie> moviesPage = movieRepository.findByGenreId(genreId, pageable);

        // Convert Page<Movie> to Page<MovieResponseDto> and return it
        return moviesPage.map(this::convertMovieToDto);
    }
    // Helper method to fetch movie by ID
    private Movie getMovieByIdFromDb(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    // Helper method to convert Actor entity to ActorResponseDto
    private ActorResponseDto convertActorToDto(Actor actor) {
        return ActorResponseDto.builder()
                .id(actor.getId())
                .name(actor.getName())
                .birthDate(actor.getBirthDate())
                .build();
    }

    // Helper method to convert Movie entity to MovieResponseDto
    private MovieResponseDto convertMovieToDto(Movie movie) {
        return MovieResponseDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .duration(movie.getDuration())
                .genre(MovieResponseGenreDto.builder()
                        .id(movie.getGenre().getId())
                        .name(movie.getGenre().getName())
                        .build())
                .actors(
                        movie.getActors().stream()
                                .map(actor -> MovieResponseActorDto.builder()
                                        .id(actor.getId())
                                        .name(actor.getName())
                                        .birthDate(actor.getBirthDate())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}