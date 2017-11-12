
GETTING STARTED

This movie ticketing system will allocate seats based on availability, customer 
satisfaction and theater optimization. 

The application assumes 
1) The best place to view the movie is from seat A through J. 
2) Allocates seats together in the same row to provide excellent customer satisfaction. 
3) Allocates seats together in the same when possible. If seats are not available 
   on the same row, then the next row with best visibility is assigned. 
   The preference starts from Row A through Row J
4) Alternate assignment strategy: If seats are not available in the same row, 
   then seats can also be assigned to rows with maximum seats availability. 
   This is catered towards better customer satisfaction.  
5) If total seats requested in a reservation is not available, then the system does not 
    reserve any partial. That particular reservation is not served. 
6) Print reservation status method can also be enabled to print the current status of the
   reservation system. 
7) No multithreaded requests are supported at this time. All requests are sequential.   


PRE-REQUISITES
JDK 1.8 


CONFIGURATION
A configuration file has been provided so that rows, seats, and allocation mode 
can be changed. 

File name: movie.config

Parameters: 

no_of_rows : Indicates the number of rows in the theatre. Default set to 10

no_of_seats : Indicates the no. of seats in each row. Default set to 20

allocation_based_on_max_seats_in_a_row :
If set to true - Seats will be assigned based on the seat row with maximum availability. 
If set to false - Seats will be assigned from row A through row J 
based on the no. of seats available in each row. 



COMPILE ON COMMAND LINE
> mkdir bin
> find . -name "*.java" -print | xargs javac -classpath lib/junit-4.12.jar -d bin


HOW TO RUN FROM COMMAND LINE

How to verify the booking system: 
> java -classpath bin/ com.movie.Reservation input.txt

Output:
The output file would be generated as "output.txt". This will contain the seat allocation 
for each reservation. However if seats are not available, it will be reported accordingly. 


How to verify using Junit test cases: 
> java -cp bin:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore com.movie.test.MovieTest




Note: You can also use your favorite IDE (Eclipse or IntelliJ) to test this application. 
