first create a new database in mysql
create database onlineshopping;

then use
use onlineshopping;

then in postman api

POST: localhost:9090/users/addUser
{
  "userId": "101",
  "password": "12345",
  "role": "CUSTOMER"
}
Again do post: localhost:9090/users/addUser
{
  "userId": "102",
  "password": "13345",
  "role": "CUSTOMER"
}

select * from user; (for checking)

GET: localhost:9090/users/getUser/101

Delete: localhost:9090/users/removeUser

{
  "userId": "102",
  "password": "13345",
  "role": "CUSTOMER"
}