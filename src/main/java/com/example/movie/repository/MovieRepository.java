package com.example.movie.repository;

import com.example.movie.DTO.MovieResponseDto;
import com.example.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Кастомный запрос для пагинации с LIMIT и OFFSET для SQLite
    @Query(value = "SELECT * FROM movie LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Movie> findMoviesWithPagination(@Param("limit") int limit, @Param("offset") int offset);
    Page<Movie> findByGenreId(Long genreId, Pageable pageable);
    Page<Movie> findByReleaseYear(int releaseYear, Pageable pageable);
    Page<Movie> findByActorsId(Long actorId, Pageable pageable);
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
