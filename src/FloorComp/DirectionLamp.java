package FloorComp;

import ControlComp.Logging;

public class DirectionLamp {
    boolean lamp;
    String OnorNot;

    public boolean getLamp() {
        return lamp;
    }

    public void toggleLamp() {
        this.lamp = !this.lamp;
        if (this.lamp == true){
            OnorNot = "On";
        }
        else {
            OnorNot = "OFF";
        }
        //System.out.println("Lamp is now " + OnorNot);
        Logging.info2("DirectionLamp", "Lamp is now " + OnorNot);
    }

    public DirectionLamp() {
        this.lamp = false;
    }
}
