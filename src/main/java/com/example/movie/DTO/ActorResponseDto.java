package com.example.movie.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActorResponseDto {
    private Long id;
    private String name;
    private LocalDate birthDate;


}
