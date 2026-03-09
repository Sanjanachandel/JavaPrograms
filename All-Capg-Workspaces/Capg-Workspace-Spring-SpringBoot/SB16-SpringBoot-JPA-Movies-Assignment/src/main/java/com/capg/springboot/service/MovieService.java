package com.capg.springboot.service;

import java.util.List;

import com.capg.springboot.enitity.Movie;

public interface MovieService {
	Movie addMovie(Movie movie);
	List<Movie> getAllMovies();
	Movie getMovieById(Integer id);
	Movie updateMovie(Movie movie);
	String deleteMovie(int id);
}
