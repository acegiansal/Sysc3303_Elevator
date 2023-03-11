# Sysc3303_Elevator

# Group Members
Justin Yeh
Jackie Zhuang
Gian Salvador 
Robbie Kuhn 
Ziad Saif El Nasr

# Introduction
The goal of this project is to design a realtime elevator control system and simulation. The elevator controller will be multithreaded
and effectively simulate an elevator in realtime. 

# Classes
- Building Class 
This class contains the main, which starts the threads and the logging

-Config Class
This class contains the important information required to configure the code. This includes the port configuration for the messaging.

- Elevator Class
This class implements the runnable interface. It simulates an elevator and recieves information from the other class to "move" the elevator
This class also notifies the user of when requests and current processes that the elevator is going

-ElevatorBox Class
This class receives the information from scheduler and receives other information from the two elevator.

-ElevatorEvent Class
This is an enum class that contains the constants used in the Elevator class

- ElevatorInfo Class
This class gathers all of the info of the elevator including direction, time, and floor. It is synchronized with other threads 
to recieve and send information on the elevator

- ElevatorIntermediate Class
This class acts as an intermediate between both the elevator and the scheduler

-ElevatorState Class
This is an enum class that contains the constants used in the Elevator class

-Floor Class
This class implemets the runnable interface. It reads the text file and updates the scheduler to tell the elevator where to go.

-logging Class
the logging class is a custom built logging class. This logger prints all the data and information received and sent from all the classes and prints it ut in the console
and creates a file that contains all of the logged data

-PacketProcessor
This class has the logic to process all of the different packages used to send and receive messages between all the different classes
-Schedular Class
The schedular class implemets the runnable interface. It uses synchronization between the floor messages and elevator messages list.
It has methods to add and get messages from these lists and updates this information across the other threads. It receives and sends request
basically telling the elevator where to go.



- TestingElevator Class
This class contains all the unit tests for the class

#Setup Instructions
- Import Project into IDE of choice
- First Start by running the schedular class
- Second run the Floor class
- Finally run the Building class
- Once the code is stopped a log file called SYSC3303_ITER1_LOG.txt will be created with information. The file will be located in the Project folder. (If in eclipse, refresh the package explorer to see the files)

#Testing
- Import Project into IDE of choice
- Select TestingElevator Class and click
- watch as all the tests are passed in the console :)

#Known issues
- You may need to clean the project in order to run, if the console is telling you it cant find the classes. Select project -> clean

#Contributions
- Main Code : Robbie Khun, Gian Salvador, Justin Yeh
- Logger Code : Ziad Saif El Nasr
- Unit Testing : Jackie Zhuang
- UML Diagrams : Jackie Zhuang
- Read Me File : Ziad Saif El Nasr
