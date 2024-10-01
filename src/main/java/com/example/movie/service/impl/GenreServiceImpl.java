package com.example.movie.service.impl;

import com.example.movie.entity.Genre;
import com.example.movie.entity.Movie;
import com.example.movie.exception.ResourceNotFoundException;
import com.example.movie.repository.GenreRepository;
import com.example.movie.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Genre getGenreById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
    }

    @Override
    public List<Genre> getAllGenres(Integer page, Integer size) {
        int offset = page * size;

        var genres = genreRepository.findGenresWithPagination(size, offset);
        return genres;
    }
    @Override
    public Genre updateGenre(Long id, Genre genreDetails) {
        Genre genre = getGenreById(id);
        if (genreDetails.getName() != null) {
            genre.setName(genreDetails.getName());
        }
        return genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id, boolean cascade) {
        Genre genre = getGenreById(id);
        if (!cascade && (genre.getMovies() != null && !genre.getMovies().isEmpty())) {
            throw new RuntimeException("Cannot delete genre '" + genre.getName() + "' because it has "
                    + genre.getMovies().size() + " associated movies.");
        }
        genreRepository.delete(genre);
    }
}
