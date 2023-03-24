package FloorComp;

public class DirectionLamp {
    boolean lamp;

    public boolean getLamp() {
        return lamp;
    }

    public void toggleLamp() {
        this.lamp = !this.lamp;
        System.out.println("Lamp is now " + this.lamp);
    }

    public DirectionLamp() {
        this.lamp = false;
    }
}
