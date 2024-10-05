package com.example.movie.DTO;

import com.example.movie.entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenreResponseDto {
    private Long id;
    private String name;
    private List<GenreResponseMovieDto> movies;
}
