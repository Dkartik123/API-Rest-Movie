package com.example.movie.service.impl;

import com.example.movie.DTO.GenreRequestDto;
import com.example.movie.DTO.GenreResponseDto;
import com.example.movie.DTO.GenreResponseMovieDto;
import com.example.movie.entity.Genre;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.GenreRepository;
import com.example.movie.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public GenreResponseDto createGenre(GenreRequestDto genreRequestDto) {
        // Создание жанра из данных DTO
        Genre genre = new Genre();
        genre.setName(genreRequestDto.getName());

        // Сохранение жанра и преобразование в GenreResponseDto
        return convertToDto(genreRepository.save(genre));
    }

    @Override
    public GenreResponseDto getGenreById(Long id) {
        // Поиск жанра по ID и преобразование в DTO
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
        return convertToDto(genre);
    }

    @Override
    public List<GenreResponseDto> getAllGenres(Integer page, Integer size) {
        // Получение всех жанров с опциональной пагинацией
        List<Genre> genres;
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Genre> genrePage = genreRepository.findAll(pageable);
            genres = genrePage.getContent();
        } else {
            genres = genreRepository.findAll();
        }

        // Преобразование списка жанров в список GenreResponseDto
        return genres.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public GenreResponseDto updateGenre(Long id, GenreRequestDto genreRequestDto) {
        // Поиск жанра по ID и обновление его данных из DTO
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));

        if (genreRequestDto.getName() != null) {
            genre.setName(genreRequestDto.getName());
        }

        // Сохранение обновленного жанра и преобразование в GenreResponseDto
        return convertToDto(genreRepository.save(genre));
    }

    @Override
    public void deleteGenre(Long id, boolean cascade) {
        // Поиск жанра по ID и проверка на наличие связанных фильмов
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));

        if (!cascade && (genre.getMovies() != null && !genre.getMovies().isEmpty())) {
            throw new RuntimeException("Cannot delete genre '" + genre.getName() + "' because it has "
                    + genre.getMovies().size() + " associated movies.");
        }

        // Удаление жанра
        genreRepository.delete(genre);
    }

    // Метод для конвертации Genre в GenreResponseDto
    private GenreResponseDto convertToDto(Genre genre) {
        return GenreResponseDto.builder()
                .id(genre.getId())
                .name(genre.getName())
                .movies(genre.getMovies() != null ? genre.getMovies().stream()
                        .map(this::convertToMovieDto)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    // Метод для конвертации Movie в GenreResponseMovieDto
    private GenreResponseMovieDto convertToMovieDto(Movie movie) {
        return GenreResponseMovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .releaseYear(movie.getReleaseYear())
                .duration(movie.getDuration())
                .build();
    }
}
