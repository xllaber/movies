package com.llacerximo.movies.mapper;

import com.llacerximo.movies.controller.model.movie.MovieDetailWeb;
import com.llacerximo.movies.controller.model.movie.MovieListWeb;
import com.llacerximo.movies.domain.entity.Movie;
import com.llacerximo.movies.persistence.model.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieMapper mapper = Mappers.getMapper(MovieMapper.class);
    MovieListWeb toMovieListWeb(Movie movie);
    MovieDetailWeb toMovieDetailWeb(Movie movie);

    @Mapping(target = "id", expression = "java(resultSet.getInt(\"id\"))")
    @Mapping(target = "title", expression = "java(resultSet.getString(\"title\"))")
    @Mapping(target = "year", expression = "java(resultSet.getInt(\"year\"))")
    @Mapping(target = "runtime", expression = "java(resultSet.getInt(\"runtime\"))")
    MovieEntity toMovieEntity(ResultSet resultSet);
    Movie toMovie(MovieEntity movieEntity);
}
