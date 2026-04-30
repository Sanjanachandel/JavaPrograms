first drop the table users if already exists in the oracle database
drop table users;

then create sequence in oracle
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;


in the postman

post : http://localhost:8082/users/register

{
    "name": "Sanjana",
    "email": "sanjana@gmail.com",
    "password": "sanjana789",
    "phoneNumber": "7973297123"
}

post : http://localhost:8082/users/login

{
    "email": "sanjana@gmail.com",
    "password": "sanjana789"
}

now go to postman, go to header, then write 
key= Authorization
value = Bearer <your_token_generated>
get: http://localhost:8082/users/1

Swagger :
http://localhost:8989/swagger-ui.html