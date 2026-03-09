package com.capg.springboot.enitity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Movie {
	@Id
	private int movieId;
	private String movieName;
	private String genre;
	private String releaseYear;
	private double rating;
	
	public Movie() {
		
	}

	public Movie(int movieId, String movieName, String genre, String releaseYear, double rating) {
		super();
		this.movieId = movieId;
		this.movieName = movieName;
		this.genre = genre;
		this.releaseYear = releaseYear;
		this.rating = rating;
	}

	public int getMovieId() {
		return movieId;
	}

	public String getMovieName() {
		return movieName;
	}

	public String getGenre() {
		return genre;
	}

	public String getReleaseYear() {
		return releaseYear;
	}

	public double getRating() {
		return rating;
	}
	
	
}
