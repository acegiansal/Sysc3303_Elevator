public class ElevatorInfo {

    private boolean direction;
    private int floorNumber;
    private String time;
    private int carButton; // destination floor

    @Override
    public String toString() {
        return "ElevatorInfo{" +
                "direction=" + direction +
                ", floorNumber=" + floorNumber +
                ", time='" + time + '\'' +
                ", carButton=" + carButton +
                '}';
    }

    public ElevatorInfo(boolean direction, int floorNumber, String time, int carButton) {
        this.direction = direction;
        this.floorNumber = floorNumber;
        this.time = time;
        this.carButton = carButton;
    }

    public boolean isDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCarButton() {
        return carButton;
    }

    public void setCarButton(int carButton) {
        this.carButton = carButton;
    }

}
