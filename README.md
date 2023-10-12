# student-registration

A simple app for students to view and register for and view courses available to them. The server was built with Java Spring Boot, the database uses MySQL, and the client was built with React.js.

## Hosted version:

A hosted version of the app can be viewed at the following location:
https://student-test.pages.dev/login

You may register for a new account on the sign up page and proceed to explore the options from the dashboard.

## Instructions for running the code locally:

### Java Spring Boot API

1. you can run it locally  mvnw clean install  spring-boot:run, then go to http://localhost:8080/course/ to explore the course for example.
2. the api hosted inside a docker on google cloud run service u can access it using this url https://gs-spring-boot-docker-iigfnkziqq-uc.a.run.app/course/ to view all course as json response.

### MySQL Database

1. start mysql server
2. run the script from the database folder to create the database schema locally and replace with remove step in application.properties file and populate with sample data

### React Client

1. cd frontend
2. npm install
3. npm start
4. the frontend will now be served at http://localhost:3000
