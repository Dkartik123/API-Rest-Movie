package com.example.movie.entity;

import com.example.movie.service.ActorService;
import com.example.movie.service.impl.MovieServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "Actors")
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(name = "name", nullable = false)
    @NotBlank(message = "Actor name is required")
    private String name;
    @Column(name = "BirthDate", nullable = false)
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @ManyToMany(mappedBy = "actors")
    @JsonIgnore

    private List<Movie> movies;

}