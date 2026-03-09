package com.capg.springboot.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.capg.springboot.enitity.Movie;
import com.capg.springboot.exceptions.MovieNotFoundException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class MovieDAOImpl implements MovieDAO{
	
	@PersistenceContext
	EntityManager em;

	@Override
	public Movie addMovie(Movie movie) {
		em.persist(movie);
		return movie;
	}

	@Override
	public List<Movie> getAllMovies() {
		return em.createQuery("from Movie", Movie.class).getResultList();
	}

	@Override
	public Movie getMovieById(Integer id) {
		 Movie movie = em.find(Movie.class, id);
		if(movie == null) {
	        throw new MovieNotFoundException("Movie with ID " + id + " not found");
	    }

		return em.find(Movie.class, id);
	}

	@Override
	public Movie updateMovie(Movie movie) {
		em.merge(movie);
		return movie;
	}

	@Override
	public String deleteMovie(int id) {
		Movie movie = em.find(Movie.class, id);

	    if(movie == null) {
	        throw new MovieNotFoundException("Movie with ID " + id + " not found");
	    }

	    em.remove(movie);
		return "Movie Deleted Successfully";
	}

}
