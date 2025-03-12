# Flight Application

This is a Spring Boot project written in Java. The project uses Gradle as a build tool. The project is for internship trial work at CGI Estonia.

## Project Description

The project is a simple flight booking application in full-stack Java and Vue.js. 
The application allows user to browse flights, filter flights, and book seats in selected flights.

The user can navigate between two pages:

1. Main page
2. Single flight page

### Main page logic:

Main page displays all flights. The flights are divided into separate pages.
The user can filter flights by origin, destination, flight date and time, and the maximum price.
The flights will be filtered based on the exact search.
In case no direct flight is found, the system will search for two connecting flights, and display them as together with the layover.

The requirement for the connecting flights are the following:
* The arrival of the first flight and the departure time of the second flight must be on the same or following days.
* The destination of the first flight must be the same as the origin of the second flight.
* The arrival time of the first flight must be before the departure time of the second flight.
* The departure date of the second flight must be the selected date (if filter was used).
* The selected time will not be considered in the search.
The user is able to browse through the pages of all displayed flights and select a flight to view more details.

### Single flight page logic:

The single flight page displays the details of the selected flight.
The user will see a seat plan for the flight, where the user can select a seat to book.
By default, the first Economy class seat will be selected.
The user can use a seat search function to find a set of seats based on their preferences.

The seat filters are:
* Seat class (Economy, Business, First)
* Checkbox for a window seat
* Checkbox for extra legroom
* Checkbox for a seat near the exit
* Checkbox for suggested seats to be near each other
* The number of seats to be found

The seat plan will display the suggested seats and the user can change the selection or add more seats by clicking on the vacant seats.
The page will display all selected seats with their class and price and the total price of the booking.
The user can confirm the booking by clicking on the "Book" button.

### Personal notes:

As there was no requirement for a user authentication, the application does not have a login page, and every user is able to book a flight without any restrictions.
In case of a real application, the user would need to be authenticated before booking a flight. The possible solution would be to use Spring Security and JWT tokens, which I would have implemented if the requirement was given.


## Project Structure

The project is divided into two main parts: backend and frontend.

### Backend

The backend is a Spring Boot application written in Java. The backend is responsible for handling the data and business logic of the application.

The backend is divided into the following packages:
1. Components
    * Contains the Data Initialization component, which is responsible for initializing the data for the application.
    * The data is fetched from AviationStack API and stored in the database.
    * The data is fetched only once when the application is started and when the database is empty.
2. Configurations
    * Contains the security configuration for the application.
    * The security configuration is set to allow all requests to the application from port 5173. If the application was user-based and required authentication, the security configuration would be different.
3. Controllers
    * Contains the controllers for the application.
    * FlightController is responsible for handling the requests related to flights, such as fetching all flights, and fetching a single flight and its seats.
    * SeatController is responsible for handling the requests related to seats, such as booking a seat.
4. DTOs
    * Contains the Data Transfer Objects for the application.
    * There is a single DTO for the combined flight data, which consists of the flight data and the seat plan data. 
5. Entities
    * Contains the entities for the application.
    * There are two entities: Flight and Seat.
    * Flight entity is responsible for storing the flight data.
    * Seat entity is responsible for storing the seat data.
6. Exceptions
    * Contains the custom exceptions for the application.
    * There are custom exceptions for handling the cases when the flight is not found or seat is already booked.
7. Repositories
    * Contains the repositories for the application.
    * There are two repositories: FlightRepository and SeatRepository.
8. Services
    * Contains the services for the application.
    * FlightService is responsible for handling the business logic related to flights.
    * SeatService is responsible for handling the business logic related to seats.

### Frontend

The frontend is a Vue.js application. The frontend is responsible for displaying the data and handling the user interactions of the application.
The original Figma design was used as a reference for the frontend design, and can be found in the following link: https://www.figma.com/design/Ve8xxqr05cgTt0GmB8AB4f/Flight-App?node-id=0-1&t=vDTzkL71wbb9wjjR-1

The frontend is divided into the following parts:
1. Assets
    * Contains ths images used in the application.
2. Components
    * Contains the components for the application.
    * ConnectingFlightComponent is responsible for displaying the connecting flights in a single card.
    * FlightComponent is responsible for displaying the flight data in a single card.
    * HeaderComponent is responsible for displaying the header of the application.
    * SeatPlanComponent is responsible for displaying the seat plan of the flight and handling the seat selection.
3. Pages
    * Contains the pages for the application.
    * FlightPage is responsible for displaying the single flight page.
    * MainPage is responsible for displaying the main page.
4. Routes
    * Contains the routes for the application.
    * The routes are set to navigate between the main page and the single flight page.
5. Store
    * Contains the store for the application.
    * The store is responsible for storing the data of a selected flight and the seat plan.

## Data

The data for the application is fetched from the AviationStack API (https://aviationstack.com/). The data is fetched only once when the application is started and when the database is empty.

The database will ideally (depending on the API response) contain 35 flights (+ two custom-made for connecting flights).
The dates of the departure and arrival are modified to be within the next 7 days. The price of each flight is randomly generated between 100 and 500.
The last two custom-made flights are used to test the connecting flight feature. (JFK -> LAX -> SFO)

There are a total of 60 seats in the plane. Each seat is identified by a row number (1-10) and a letter (A-F).
The first two rows are first class, the next two rows are business class, and the rest are economy class.
The price of each seat is based on the base price of the flight and the class of the seat.
Seats in the first two rows have a 100% price increase, and seats in the next two rows have a 50% price increase.
Seats in the first class, business class, and row 6 (near exit) have extra legroom.
30% of the seats are randomly marked as booked.

## Technologies

The project uses the following technologies:

1. Java 23
2. Spring Boot
3. Vue.js
4. Gradle
5. PostgresSQL
6. Docker
7. AviationStack API

## Tests

The application has unit tests for the backend. The tests are written using JUnit and Mockito.
The frontend tests are implemented using Cypress. The tests are written in JavaScript and can be cypress/e2e folder.

## Port Configuration

The backend is running on port 8080 and the frontend is running on port 5173. The database is running on port 5432.

## Environment Variables

The application uses the following environment variables:
* DB_USERNAME - the username for the database
* DB_PASSWORD - the password for the database
* API_KEY - the API key for the AviationStack API (this will not be published, and I will only send it with the project submission). Please keep in mind that the API key is limited to 100 requests per month, so please use it wisely.

## How to run the application

### Variant 1: Running the application locally

1. Clone the repository
2. Open the project in your favorite IDE
3. Set the environment variables in your IDE
4. Run the FlightApplication class in the backend
5. Run the frontend by navigating to the frontend folder and running the following commands:
    ```
    npm install
    npm run serve
    ```
6. Open the browser and navigate to http://localhost:5173
7. The application should be running

### Variant 2: Running the application using Docker

## Personal notes:

This project was fairly simple to me, as I have experience in both Java and Vue.js. The most challenging part was to implement the connecting flights feature, as I had to come up with a solution to find the connecting flights based on the given requirements. I am happy with the result and I believe that the application is working as expected.
I implemented all the base requirements and additional requirements. I would have implemented the user authentication if the requirement was given, as it sounds like a logical next step for the application. Yet, considering the time limit, I decided to focus on the main requirements.
The whole project took me a few days to complete (perhaps about 10 hours in total), as I believe the backend was really simple. I spent most of my time on the frontend, making sure the flow was logical and the user experience was good. Yet I believe the design could be improved, as I am not a designer and I focused more on the functionality of the application.
The most challenging part was to actually run the project and the tests as I faced some issues with the Data Initializer and the test configurations, yet they were not significant and I was able to solve them quickly.

For code refactoring, ChatGPT was used. The tool was able to provide some suggestions for the code, but most of the suggestions were not relevant to the project. The tool was able to find some typos and suggest some code improvements, but the suggestions were not significant, e.g. creating custom-made exceptions.

The project was a good learning experience for me, as I was able to practice my Java and Vue.js skills. I am happy with the result and I believe the application is working as expected. I am looking forward to hearing your feedback on the project.

## Image references

The images used in the application are from the following sources:
* https://unsplash.com/photos/flying-airplane-iDFumFZ5fFw
* https://unsplash.com/photos/aerial-view-photography-of-boats-on-seashore-2ueUnL4CkV8
* https://unsplash.com/photos/santorini-greece-XGKaRnWjv1c
* https://unsplash.com/photos/rialto-bridge-venice-italy-hFXZ5cNfkOk