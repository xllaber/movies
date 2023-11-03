package com.llacerximo.movies.persistence.DAO;

import com.llacerximo.movies.db.DBUtil;
import com.llacerximo.movies.exceptions.DBConnectionException;
import com.llacerximo.movies.exceptions.SQLStatmentException;
import com.llacerximo.movies.mapper.MovieMapper;
import com.llacerximo.movies.persistence.model.MovieEntity;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MovieDAO {

    public List<MovieEntity> getAllPaginated(Connection connection, Integer page, Integer pageSize) {
        String sql = "SELECT * FROM movies";
        int offset = (page - 1) * pageSize;
        sql += String.format(" LIMIT %d, %d", offset, pageSize);
        List<MovieEntity> movieEntities = new ArrayList<>();
        try {
            ResultSet resultSet = DBUtil.select(connection, sql, null);
            while (resultSet.next()) {
                movieEntities.add(MovieMapper.mapper.toMovieEntity(resultSet));
            }
            DBUtil.close(connection);
            return movieEntities;
        } catch (DBConnectionException e) {
            throw e;
        } catch (SQLException e) {
            throw new SQLStatmentException("SQL: " + sql + e.getMessage());
        }
    }

    public Optional<MovieEntity> findById(Connection connection, Integer id) {
        final String SQL = "SELECT * FROM movies WHERE id = ? LIMIT 1";
        try{
            ResultSet resultSet = DBUtil.select(connection, SQL, List.of(id));
            return Optional.ofNullable(resultSet.next() ? MovieMapper.mapper.toMovieEntity(resultSet) : null);
        } catch (SQLException e) {
            throw new SQLStatmentException("SQL: " + SQL + e.getMessage());
        }
    }

    public Integer getTotalRecords(Connection connection) {
        final String SQL = "SELECT COUNT(*) FROM movies";
        try {
            ResultSet resultSet = DBUtil.select(connection, SQL, null);
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("SQL: " + SQL + e.getMessage());
        }
    }

    public Integer insert(Connection connection, MovieEntity movieEntity) throws SQLException {
        try {
            final String SQL = "INSERT INTO movies (title, year, runtime, director_id) VALUES (?, ?, ?, ?)";
            List<Object> params = new ArrayList<>();
            params.add(movieEntity.getTitle());
            params.add(movieEntity.getYear());
            params.add(movieEntity.getRuntime());
            params.add(movieEntity.getDirectorId());
            Integer id =  DBUtil.insert(connection, SQL, params);
            movieEntity.getActorIds().stream()
                    .forEach(actorId -> addActor(connection, id, actorId));
            connection.commit();
            return id;
        } catch (Exception e){
            connection.rollback();
            throw new RuntimeException(e.getMessage());
        }
    }

    public void addActor(Connection connection, Integer movieId, Integer actorId){
        final String SQL = "insert into actors_movies (actor_id, movie_id) values (?, ?)";
        DBUtil.insert(connection, SQL, List.of(actorId, movieId));
    }

    public void update(Connection connection, MovieEntity movieEntity) {
        String sql = "update movies set title = ?, year = ?, runtime = ?, director_id = ?";
        List<Object> params = new ArrayList<>();
        params.add(movieEntity.getTitle());
        params.add(movieEntity.getYear());
        params.add(movieEntity.getRuntime());
        params.add(movieEntity.getDirectorId());
        DBUtil.update(connection, sql, params);
        DBUtil.close(connection);
    }

    public void delete(Connection connection, Integer id) {
        String sql = "delete from movies where id = ?";
        String sqlActors = "delete form actors_movies where movie_id = ?";
        List<Object> params = List.of(id);
        DBUtil.delete(connection, sql, params);
        DBUtil.delete(connection, sqlActors, params);
        DBUtil.close(connection);
    }
}