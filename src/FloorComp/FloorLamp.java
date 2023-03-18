package FloorComp;

public class FloorLamp {
    private boolean pressed;

    public FloorLamp() {
        this.pressed = false;
    }

    public String pressed(){
        this.pressed = !pressed;
        return "Lamp light " + this.pressed;
    }

}
