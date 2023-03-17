package ElevatorComp.ElevatorStates;

import ElevatorComp.ElevatorCar;

abstract public class ElevatorState {

        protected ElevatorCar elevator;

        public ElevatorState(ElevatorCar elevator){
                this.elevator = elevator;
        }

        public abstract void performAction();

        public abstract void updateStatus();

}
