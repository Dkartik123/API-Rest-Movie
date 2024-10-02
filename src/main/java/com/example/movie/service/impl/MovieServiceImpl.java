package com.example.movie.service.impl;

import com.example.movie.DTO.MovieRequestDto;
import com.example.movie.DTO.MovieResponseActorDto;
import com.example.movie.DTO.MovieResponseDto;
import com.example.movie.DTO.MovieResponseGenreDto;
import com.example.movie.entity.Actor;
import com.example.movie.entity.Genre;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.ActorRepository;
import com.example.movie.repository.GenreRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.service.MovieService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    @Transactional
    public MovieResponseDto createMovie(MovieRequestDto movieRequestDto) {

        // Step 1: Create a new Movie instance using the builder or constructor
        Movie movie = Movie.builder()
                .title(movieRequestDto.getTitle())
                .releaseYear(movieRequestDto.getReleaseYear())
                .duration(movieRequestDto.getDuration())
                .build();

        // Step 2: Fetch the Genre entity by ID (assumes the genre already exists)
        Genre genre = genreRepository.findById((long) movieRequestDto.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        // Step 3: Set the genre in the movie
        movie.setGenre(genre);


        // Step 4: Fetch the actors by their IDs (assumes actors already exist)
        List<Actor> actors = actorRepository.findAllById(movieRequestDto.getActorIds());

        if (actors.isEmpty()) {
            throw new ResourceNotFoundException("Actors not found");
        }

        movie.setActors(actors);

        return converMovieToDto(movieRepository.save(movie));
    }

    @Override
    public MovieResponseDto getMovieById(Long id) {
        Movie movie = getMovieByIdFromDb(id);
        return converMovieToDto(movie);
    }

    @Override
    public List<MovieResponseDto> getAllMovies(Integer page, Integer size) {
        List<Movie> movies = new ArrayList<>();
        if(page!=null &&size != null) {
            int offset = page * size;
            movies = movieRepository.findMoviesWithPagination(size, offset);
        }
        else {
            movies = movieRepository.findAll();
        }
        return movies.stream().map(movie -> converMovieToDto(movie)).collect(Collectors.toList());
    }

    @Override
    public MovieResponseDto updateMovie(Long id, MovieRequestDto movieDetails) {
        Movie movie = getMovieByIdFromDb(id);
        if (movieDetails.getTitle() != null) {
            movie.setTitle(movieDetails.getTitle());
        }
        if (movieDetails.getReleaseYear() != 0) {
            movie.setReleaseYear(movieDetails.getReleaseYear());
        }
        if (movieDetails.getDuration() != 0) {
            movie.setDuration(movieDetails.getDuration());
        }
        Genre genre = genreRepository.findById((long) movieDetails.getGenreId())
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));

        List<Actor> actors = actorRepository.findAllById(movieDetails.getActorIds());

        if (actors.isEmpty()) {
            throw new ResourceNotFoundException("Actors not found");
        }

        // Step 5: Set the actors in the movie
        movie.setActors(actors);
        return converMovieToDto(movieRepository.save(movie));
    }

    @Override
    public void deleteMovie(Long id, boolean cascade) {

        //Make private method to get movie by id from db
        Movie movie = getMovieByIdFromDb(id);

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


    private Movie getMovieByIdFromDb(Long id) {
        return
                movieRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }
    private MovieResponseDto converMovieToDto(Movie movie) {
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
            .build();}

}
