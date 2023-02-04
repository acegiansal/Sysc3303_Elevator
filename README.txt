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

- Elevator Class
This class implemts the runnable interface. It simulates an elevator and recieves information from the other class to "move" the elevator

- ElevatorInfo Class
This class gathers all of the info of the elevator including direction, time, and floor. It is synchronized with other threads 
to recieve and send information on the elevator

-Floor Class
This class implemets the runnable interface. It reads the text file and updates the schedular to tell the elevator where to go.

-Schedular Class
The schedular class implemets the runnable interface. It uses synchronziation between the floormessages and elevator messages list. 
It has methods to add and get messages from these lists and updates this information across the other threads.

-LogConfigurator Class
The LogConfigurator class implemets java's logging class and filehandler to configure the logging to output all of the information on to 
a text file.

- TestingElevator Class
This class contains all of the unit tests for the class

#Setup Instructions
- Import Project into IDE of choice
- Select the Building class and click run
- Once the code is stopped a log file called SYSC3303_ITER1_LOG.txt will be created with information 

#Testing
- Import Project into IDE of choice
- Select TestingElevator Class and click
- watch as all the tests are passed in the console :)
