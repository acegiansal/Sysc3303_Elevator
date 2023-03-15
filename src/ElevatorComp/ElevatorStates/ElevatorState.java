package ElevatorComp.ElevatorStates;

import ElevatorComp.ElevatorCar;

abstract public class ElevatorState {

        protected ElevatorCar elevator;

        public abstract void performAction();

}
