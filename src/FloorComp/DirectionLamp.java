package FloorComp;

public class DirectionLamp {
    boolean lamp;

    public boolean getLamp() {
        return lamp;
    }

    public void toggleLamp(boolean lamp) {
        this.lamp = !this.lamp;
    }

    public DirectionLamp() {
        this.lamp = false;
    }
}
