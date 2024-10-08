package com.example.movie.controller;

import com.example.movie.DTO.ActorResponseDto;
import com.example.movie.DTO.MovieRequestDto;
import com.example.movie.DTO.MovieResponseDto;
import com.example.movie.service.ActorService;
import com.example.movie.service.MovieService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ActorService actorService;

    // POST method to create a new movie and return HTTP 201 (Created)
    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@Valid @RequestBody MovieRequestDto movieRequestDto) {
        MovieResponseDto newMovie = movieService.createMovie(movieRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMovie); // Return 201 Created
    }

    // GET method to retrieve a specific movie by its ID and return HTTP 200 (OK)
    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long id) {
        MovieResponseDto movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie); // Return 200 OK
    }

    // GET method to retrieve all movies with pagination and return HTTP 200 (OK)
    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies(
            @RequestParam(required = false) @Min(0) Integer page,  // Page number, minimum value 0
            @RequestParam(required = false) @Min(0) @Max(20) Integer size  // Page size, maximum 20 items per page
    ) {
        List<MovieResponseDto> movies = movieService.getAllMovies(page, size);
        return ResponseEntity.ok(movies); // Return 200 OK
    }

    // GET method to search for movies by title (case-insensitive) with pagination and return HTTP 200 (OK)
    @GetMapping("/search/by-title")
    public ResponseEntity<List<MovieResponseDto>> findByTitleContainingIgnoreCase(
            @RequestParam String title,                          // Title for the search query
            @RequestParam(defaultValue = "0") Integer page,      // Default page number (0)
            @RequestParam(defaultValue = "10") Integer size      // Default page size (10)
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieResponseDto> moviePage = movieService.searchMoviesByTitle(title, pageable);
        return ResponseEntity.ok(moviePage.getContent()); // Return 200 OK
    }

    // PATCH method to update movie details by ID and return HTTP 200 (OK)
    @PatchMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(@PathVariable Long id, @RequestBody MovieRequestDto movieDetails) {
        MovieResponseDto updatedMovie = movieService.updateMovie(id, movieDetails);
        return ResponseEntity.ok(updatedMovie); // Return 200 OK
    }

    // GET method to search for movies by release year with pagination and return HTTP 200 (OK)
    @GetMapping("/search/by-year")
    public ResponseEntity<Page<MovieResponseDto>> getMoviesByYear(
            @RequestParam(required = false) Integer year,       // Release year to filter by
            @RequestParam(defaultValue = "0") int page,         // Default page number (0)
            @RequestParam(defaultValue = "10") int size         // Default page size (10)
    ) {
        Pageable pageable = PageRequest.of(page, size);

        if (year != null) {
            Page<MovieResponseDto> movies = movieService.getMoviesByReleaseYear(year, pageable);
            return ResponseEntity.ok(movies); // Return 200 OK with movies filtered by release year
        } else {
            return ResponseEntity.badRequest().body(null); // Return 400 Bad Request if year is missing
        }
    }
    @GetMapping("/{movieId}/actors")
    public ResponseEntity<List<ActorResponseDto>> getAllActorsInMovie(@PathVariable Long movieId) {
        List<ActorResponseDto> actors = movieService.getAllActorsInMovie(movieId);
        return ResponseEntity.ok(actors); // Return 200 OK status with the list of actors
    }
    // DELETE method to delete a movie by ID and return HTTP 204 (No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade) {
        movieService.deleteMovie(id, cascade);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
    @GetMapping("/by-genre")
    public ResponseEntity<Page<MovieResponseDto>> getMoviesByGenreId(
            @RequestParam Long genreId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieResponseDto> movies = movieService.getMoviesByGenreId(genreId, pageable);
        return ResponseEntity.ok(movies);  // Return HTTP 200 OK with the movies page
    }
    // Search movies by actor ID (distinct URL to avoid ambiguity)
    @GetMapping("/by-actor")
    public ResponseEntity<List<MovieResponseDto>> getMoviesByActorId(
            @RequestParam(value = "actorId") Long actorId
    ) {
        List<MovieResponseDto> movies = movieService.getMoviesByActorId(actorId);
        return ResponseEntity.ok(movies); // Returning HTTP 200 OK
    }
}
