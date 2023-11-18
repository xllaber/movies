package com.llacerximo.movies.domain.service.impl;

import com.llacerximo.movies.domain.entity.Actor;
import com.llacerximo.movies.domain.entity.Director;
import com.llacerximo.movies.domain.entity.Movie;
import com.llacerximo.movies.domain.entity.MovieCharacter;
import com.llacerximo.movies.domain.repository.ActorRepository;
import com.llacerximo.movies.domain.repository.DirectorRepository;
import com.llacerximo.movies.domain.service.MovieService;
import com.llacerximo.movies.exceptions.ResourceNotFoundException;
import com.llacerximo.movies.domain.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;
    @Autowired
    DirectorRepository directorRepository;
    @Autowired
    ActorRepository actorRepository;



    @Override
    public List<Movie> getAllPaginated(Integer page, Integer pageSize) {
        List<Movie> movies = movieRepository.getAllPaginated(page, pageSize);
        return movies;
    }

    @Override
    public Movie findById(Integer id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la pelicula con id: " + id));
        return movie;
    }

    public Integer getTotalRecords(){
        return movieRepository.getTotalRecords();
    }

    @Override
    public Integer insert(Movie movie, Integer directorId, Map<Integer, String> characters) {
        Director director = directorRepository.getById(directorId)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el director con id " + directorId));
        List<MovieCharacter> movieCharacters = new ArrayList<>();
        characters.forEach((actorId, characterName) -> {
            MovieCharacter movieCharacter = new MovieCharacter();
            movieCharacter.setCharacter(characterName);
            movieCharacter.setActor(
                    actorRepository.getById(actorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Actor con id " + actorId + " no encontrado"))
            );
            movieCharacters.add(movieCharacter);
        });
        movie.setDirector(director);
        movie.setCharacters(movieCharacters);
        return movieRepository.insert(movie);
    }

    @Override
    public void update(Movie movie) {
        movieRepository.findById(movie.getId()).orElseThrow(() -> new ResourceNotFoundException("no se ha encontrado la pelicula con id " + movie.getId()));
        movieRepository.update(movie);
    }

    @Override
    public void delete(Integer id) {
        movieRepository.findById(id).orElseThrow(()  -> new ResourceNotFoundException("No se ha encontrado la pelicula con id: " + id));
        movieRepository.delete(id);
    }

}
