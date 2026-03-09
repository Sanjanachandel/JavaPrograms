PORT : 9092

POST
http://localhost:9092/movies/add
{
  "movieId": 101,
  "movieName": "Inception",
  "genre": "Sci-Fi",
  "releaseYear": "2010",
  "rating": 8.8
}
{
  "movieId": 102,
  "movieName": "The Dark Knight",
  "genre": "Action",
  "releaseYear": "2008",
  "rating": 9.0
}
{
  "movieId": 103,
  "movieName": "Interstellar",
  "genre": "Sci-Fi",
  "releaseYear": "2014",
  "rating": 8.6
}

GET
http://localhost:9092/movies/all

GET BY ID
http://localhost:9092/movies/1

PUT
http://localhost:9092/movies/update

{
  "movieId": 102,
  "movieName": "The Dark Knight Rises",
  "genre": "Action",
  "releaseYear": "2012",
  "rating": 8.4
}

DELETE
http://localhost:9092/movies/delete/1