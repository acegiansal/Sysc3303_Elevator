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
- ConfigInfo
This class configures everything for the elevator, this includes the packet sizes, ports and times for the elevator

-ElevatorBox
The ElevatorBox class in this code is responsible for managing elevator statuses and requests. It provides methods for
getting and setting elevator statuses and requests, and ensures thread safety through synchronized methods.

-ElevatorControl
The ElevatorControl class is responsible for controlling the behavior of the elevators in the system.
It creates a Scheduler and ElevatorIntermediate objects for each elevator and listens for status updates from the elevators.

-ElevatorIntermediate
Elevator Intermediate is a mediator class that acts as an intermediary between the ElevatorControl and individual elevators.
It receives updates and requests from the ElevatorControl, and sends requests to the appropriate elevator.

-Logging Class
the logging class is a custom-built logging class. This logger prints all the data and information received and sent from
all the classes and prints it ut in the console and creates a file that contains all the logged data

-Scheduler
This class schedules all the elevators and makes sure the correct elevator is selected to run

-ElevatorStatus
The ElevatorStatus class is a representation of the current status of an elevator in a building.
It contains information such as the current floor the elevator is on, its direction (up, down, idle, or stuck), and its ID.

-RequestPacket
The RequestPacket class is used to create objects that represent requests made by passengers for elevator service

-BrokenDoors
The BrokenDoors class extends the DoorState class and represents the state of the elevator doors when they are stuck open.
It tries to close the doors when it is entered and sets a timer to check if the doors have been fixed.

-CloseDoors
The CloseDoors class represents the state of the elevator doors when they are closing. When the state is entered, a timer
is set for the duration of the door closing time.

-DoorState
This code represents the DoorState class which is used to handle elevator door states.

-ElevatorState
Elevator State defines an abstract class for the different states of an elevator. It includes methods for setting and interrupting
a timer, handling requests, and updating the elevator's status.

-Idle
The Idle class is a subclass of ShaftState representing the idle state of an elevator car. When an elevator car is in an idle state,
 it updates its status and waits for a request to arrive.

-MoveFloor
The MoveFloor class extends ShaftState and handles the movement of the elevator between floors, updating the current floor and
starting an arrival sensor thread while also setting a timer for floor traversal time.

-OpenDoors
The OpenDoors state opens the elevator doors for a set amount of time and then transitions to either CloseDoors or BrokenDoors state
depending on elevator status.

-ShaftState
The ShaftState class is an abstract class that extends ElevatorState and provides some default implementations for its methods.

-Timer
This class is for the timer object

-ArrivalSensor
This class configures and sets the arrival sensor

-ElevatorCar
The ElevatorCar class handles the management of floor queues, elevator status, and communication with the central control system.

-ElevatorCommunications
This class implements the Runnable interface. It handles sending and receiving data packets for an elevator car object

-DirectionLamp
This class configures and turns the direction lamp on or off
-Floor
The Floor class represents a floor in the building, with floor number, type, floor button, up and down direction lamps,
and an array of elevator cars that have arrived at the floor

-FloorButton
Configures and sets the floor button and direction

-FloorLamp
sets if floor lamp is pressed or not
-FloorReceive
This class receives floor info

-FloorSend
This class sends floor info




- TestingElevator Class
This class contains all the unit tests for the class

#Setup Instructions
- Import Project into IDE of choice
- First Start by running the ElevatorControl class
- Second run the ElevatorCar class
- Finally run the FloorSend class
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
