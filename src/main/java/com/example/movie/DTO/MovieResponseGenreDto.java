package com.example.movie.DTO;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponseGenreDto {
    private Long id;
    private String name;

}
