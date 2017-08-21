In the Elevator interface I added the method executeProcedure for splitting the request of moving the elevator and then actually executing the movement of the elevator.
I wanted to do as few changes as possibly, but think the method currentFloor should be getCurrentFloor to keep it consistent.

With the ElevatorController interface I have the following thoughts. 
With the current methods I think the interface is somewhat limited to make good logic for operating an elevator.
I think there should either be an method whit from and to floor as inputs or a method with from floor and direction and then a method for requesting to which floor.
The method releaseElevator I think is not that needed as with good logic even none completely free elevators could be planned to be used on multiple requests.
The thought of that requestElevator should return an elevator only if one is available I think should be done instead in a way with a queue managed by ElevatorController instead of each caller doing potentially retries.

On both interfaces I made them public as I wanted to separate the interfaces and the implementation. 

In ElevatorControllerEndPoints I added an endpoint, requestElevator, that comes with an attribute, toFloor, to be able to trigger requests of elevators.

The unit test simulateAnElevatorShaft simulates 6 elevators and 10 floors. There are then several requests to different floors.
The "result" can be viewed in the console and as well in an event file in the events sub folder to the project.

I added properties for bottom and top floor to make that configurable.