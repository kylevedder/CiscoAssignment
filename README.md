# Cisco Assignment
RESTful Web Service for the Cisco Start-Up Programming Assignment

Overview
=======
This is a project handed out to students by Cisco Start-Up in order 
to allow students to demonstrate their technical skills. 

I took this opportunity to use a framework I had never used before: 
[Spring Boot](http://projects.spring.io/spring-boot/). I spent a lot 
of my time on this project familiarizing myself with Spring. I had a 
lot of fun working on this project and had my mind blown several times 
by the power of Spring Boot. I will definitely be using Spring Boot in
future projects!

API
=======
The API allows for GET, POST, PUT, and DELETE.

GET
-------
**/api/objects**: Lists URLs to all the entries in the database in a 
JSON array.

**/api/objects/{uid}**: Returns the entry mapped to the *uid* requested.
Returns error payload if an entity with the given *uid* does not exist.

POST
-------
**/api/objects**: Takes the JSON payload and stores it in the database, 
returning the JSON payload with an added *uid* field. Returns an error 
payload if not POSTed payload is not JSON.

PUT
-------
**/api/objects/{uid}**: Takes the JSON payload and stores it in the database 
with the given *uid* and returns the JSON payload. This operation is idempotent. 
Throws an error the PUTed payload is not JSON.

DELETE
-------
**/api/objects/{uid}**: Deletes the entry associated with the given *uid*. 
Returns an error payload if no entry with the given *uid* exists.

Errors
-------
The API will return an error payload if any of the aformentioned error cases
are met, if unvalid URIs are requested, or invalid or unsupported request methods
are used. Unsupported request methods include PATCH, HEAD, etc.