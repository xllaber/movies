package com.llacerximo.movies.persistence;

import com.llacerximo.movies.domain.entity.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorRepository {
    List<Director> getAll();
    List<Director> getAllPaginated(Optional<Integer> page, Integer pageSize);

    Director getById(Integer id);

    Integer insert(Director director);

    void update(Director director);

    void delete(Integer id);

    Integer getTotalRecords();
}
