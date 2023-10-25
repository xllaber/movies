package com.llacerximo.movies.domain.repository;

import com.llacerximo.movies.domain.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    List<Movie> getAll();
    List<Movie> getAllPaginated(Integer page, Integer pageSize);
    Integer getTotalRecords();
    Movie findById(Integer id);
}