package com.capg.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capg.springboot.dao.MovieDAO;
import com.capg.springboot.enitity.Movie;

@Service
public class MovieServiceImpl implements MovieService{
	
	@Autowired
	MovieDAO dao;

	@Override
	public Movie addMovie(Movie movie) {
		return dao.addMovie(movie);
	}

	@Override
	public List<Movie> getAllMovies() {
		return dao.getAllMovies();
	}

	@Override
	public Movie getMovieById(Integer id) {
		return dao.getMovieById(id);
	}

	@Override
	public Movie updateMovie(Movie movie) {
		return dao.updateMovie(movie);
	}

	@Override
	public String deleteMovie(int id) {
		return dao.deleteMovie(id);
	}

}
