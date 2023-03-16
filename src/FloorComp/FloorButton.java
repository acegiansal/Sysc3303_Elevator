package FloorComp;

public class FloorButton {
    private FloorLamp lamp;
    private String direction;

    public FloorButton(String direction){
        this.direction = direction.equals("u") ? "Up" : "Down";
    }

    public String pressed(){
        return direction + " button pressed, " + lamp.pressed();
    }
}
