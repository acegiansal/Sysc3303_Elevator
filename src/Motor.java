public class Motor {
    private boolean isOn;

    public Motor() {
        this.isOn = false;
    }

    public void turnOn() {
        this.isOn = true;
        logging.info("Motor","" + Thread.currentThread().getName(), "Motor is turned on");
    }

    public void turnOff() {
        this.isOn = false;
        logging.info("Motor","" + Thread.currentThread().getName(),"Motor is turned off");
    }

    public boolean isOn() {
        return this.isOn;
    }
}
