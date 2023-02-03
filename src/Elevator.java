public class Elevator implements Runnable{

    private Scheduler scheduler;

    public Elevator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private ElevatorInfo receiveFromSched(){
        ElevatorInfo info = scheduler.getFloorMessages();
        System.out.println("Elevator Receiving " + info);
        return info;
    }

    private void send(ElevatorInfo info){
        System.out.println("Elevator Sending " + info);
        scheduler.addElevatorMessage(info);
    }

    void testReceiveFromSched(){
        ElevatorInfo info = scheduler.getFloorMessages();
        System.out.println("Elevator Receiving " + info);
        send(info);
    }

    void testSend(ElevatorInfo info){
        System.out.println("Elevator Sending " + info);
        scheduler.addElevatorMessage(info);
    }

    @Override
    public void run() {
        while (true) {
            send(this.receiveFromSched());
        }
    }
}
