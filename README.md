# Cisco Assignment
RESTful Web Service for the Cisco Start-Up Programming Assignment

Overview
=======
This is a project handed out to students by Cisco Start-Up in order 
to allow students to demonstrate their technical skills. 

I took this opportunity to use a framework I have never used before: 
[Spring Boot](http://projects.spring.io/spring-boot/). I spent a lot 
of my time on this project familiarizing myself with Spring Boot and 
had a lot of fun along the way. The power of Spring Boot is mind blowing;
I will definitely be using it again in future projects!

Dependencies
============
This project requires JDK 8, Maven, and MongoDB to be installed on the 
build server and it requires JRE 8 and MongoDB to be installed on the 
release server.

Building / Running
============
To build, execute the **buildScript.sh** shell script.

To run, execute the **assignment-\<version>.jar** JAR file in the **/target** directory.

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
