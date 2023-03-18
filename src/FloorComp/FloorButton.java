package FloorComp;

public class FloorButton {
    private FloorLamp lamp;
    private String direction;

    public FloorButton(String direction){
        this.lamp = new FloorLamp();
        this.direction = direction.equals("Up") ? "Up" : "Down";
    }

    public void setDirection(String direction) {
        this.direction = direction.equals("Up") ? "Up" : "Down";
    }

    public String pressed(String direction){
        setDirection(direction);
        return this.direction + " button pressed, " + this.lamp.pressed();
    }
}
