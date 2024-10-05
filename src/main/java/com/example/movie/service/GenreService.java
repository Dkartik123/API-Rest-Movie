package com.example.movie.service;

import com.example.movie.DTO.GenreRequestDto;
import com.example.movie.DTO.GenreResponseDto;

import java.util.List;

public interface GenreService {

    // Создание жанра, используя данные из GenreRequestDto
    GenreResponseDto createGenre(GenreRequestDto genreRequestDto);

    // Получение жанра по ID, возвращает GenreResponseDto
    GenreResponseDto getGenreById(Long id);

    // Получение всех жанров с опциональной пагинацией
    List<GenreResponseDto> getAllGenres(Integer page, Integer size);

    // Обновление существующего жанра по ID, используя данные из GenreRequestDto
    GenreResponseDto updateGenre(Long id, GenreRequestDto genreRequestDto);

    // Удаление жанра с возможностью каскадного удаления связанных данных
    void deleteGenre(Long id, boolean cascade);
}
