package com.capg.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capg.springboot.enitity.Movie;
import com.capg.springboot.service.MovieService;

@RestController
@RequestMapping("/movies")
public class MovieController {

	@Autowired
	MovieService services;
	
	@PostMapping("/add")
	public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
		Movie m = services.addMovie(movie);
		return new ResponseEntity<Movie>(m, new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Movie>> getAllMovie(){
		return new ResponseEntity<List<Movie>>(services.getAllMovies(), new HttpHeaders(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Movie> getMovieById(@PathVariable Integer id) {
		return new ResponseEntity<Movie>(new HttpHeaders(),HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Movie> updateMovie(@RequestBody Movie movie) {
		Movie m = services.updateMovie(movie);
		return new ResponseEntity<Movie>(m, new HttpHeaders(), HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteMovie(@PathVariable int id) {
		services.deleteMovie(id);
		return new ResponseEntity<String>("Movie Deleted", new HttpHeaders(), HttpStatus.OK);
	}
}
