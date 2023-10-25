package com.llacerximo.movies.controller;

import com.llacerximo.movies.controller.model.director.DirectorListWeb;
import com.llacerximo.movies.domain.entity.Director;
import com.llacerximo.movies.domain.service.DirectorService;
import com.llacerximo.movies.http_response.Response;
import com.llacerximo.movies.mapper.DirectorMapper;
import com.llacerximo.movies.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directors")
public class DirectorController {

    @Autowired
    DirectorService directorService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public Response getAll(@RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        List<DirectorListWeb> directorListWeb = directorService.getAllPaginated(page, pageSize)
                .stream()
                .map(DirectorMapper.mapper::toDirectorListWeb)
                .toList();
        return new Response(directorListWeb, new PaginationUtils(directorService.getTotalRecords(), pageSize, page));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Response getById(@PathVariable("id") Integer id){
        return new Response(DirectorMapper.mapper.toDirectorDetailWeb(directorService.getById(id)));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Response insert(@RequestBody Director director){
//        return new Response(DirectorMapper.mapper.toDirectorDetailWeb(directorService.getById(dire)))
        Integer id = directorService.insert(director);
        director.setId(id);
        return new Response(director);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@PathVariable("id") Integer id, @RequestBody Director director) {
        director.setId(id);
        directorService.update(director);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        directorService.delete(id);
    }

}
