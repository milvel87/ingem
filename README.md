*Environment requirements (Windows/Linux - but not tested on it):*

- Java 8 and Maven 3.6.3 
- PostgreSQL
- Apache Maven 3.6.3


*Setting up database:*

- Make sure you have Postgres localhost server running
- If you are running Windows, start the SQL Shell (psql) command line from   	Start Menu and connect to the localhost server.For running the SQL script use the following command with the path set accordingly:  
> \i 'C:/Ingem/sql/createIngemDb.sql'
- If you are running Linux use the following command with your postgres user and path to the sql directory set accordingly:
  
>   psql -U postgres -f /home/r2schools/createdatabase.sql

*Building the application:*

- Use command prompt or terminal depending on your OS
- Navigate to the main directory that you pulled from Github and that contains the pom.xml file
- Build application by using command:
> mvn install
  
  - If you don't want to run test use command:
>   mvn install -DskipTests

*Starting the application*

- Use command prompt or terminal depending on your OS
- Navigate to the main directory that you pulled from Github and use command:
> java -jar target/interview-0.0.1-SNAPSHOT.jar

The application should start on port 8080. To check that everything is working properly use the following URL in the browser:
> http://localhost:8080/products
Other endpoints can be tested by using Postman:

List of all products

GET http://localhost:8080/products

Select one product by ID

GET http://localhost:8080/products/{id}

Create product

POST http://localhost:8080/products

Update product

PUT http://localhost:8080/products  
 
Delete product
 
DELETE http://localhost:8080/products/{id}

For POST and PUT methods Product should be provided as JSON inside request body.
 

