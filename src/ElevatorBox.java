import java.util.ArrayList;
import java.util.Arrays;

public class ElevatorBox {

    /** The data of the client */
    private ArrayList<byte[]> requestData;
    /** If there is something in clientData */
    private ArrayList<Boolean> hasRequestData;

    /** The data of the server */
    private ArrayList<byte[]> responseData;
    /** If there is something in serverData */
    private ArrayList<Boolean> hasResponseData;

    /**
     * Creates basic databox object
     */
    public ElevatorBox(int elevatorNum) {
        this.requestData = new ArrayList<>();
        this.responseData = new ArrayList<>();
        this.hasRequestData = new ArrayList<>();
        this.hasResponseData = new ArrayList<>();

        for (int i=0; i<elevatorNum; i++){
            this.requestData.add(new byte[1]);
            this.responseData.add(new byte[1]);
            this.hasRequestData.add(false);
            this.hasResponseData.add(false);

        }
    }

    /**
     * Protected getter for client data
     * @return The data the client has sent
     */
    public synchronized byte[] getRequestData(int elevatorID) {
        while(!hasRequestData.get(elevatorID)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hasRequestData.set(elevatorID, false);
        notifyAll();
        return requestData.get(elevatorID);
    }

    /**
     * Protected setter for client data
     * @param data The data that the client has sent
     */
    public synchronized void putRequestData(byte[] data, int elevatorID) {
        while(hasRequestData.get(elevatorID)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Putting request into box: + " + Arrays.toString(data));
        hasRequestData.set(elevatorID, true);
        requestData.set(elevatorID, data);
        notifyAll();
    }

    /**
     * Protected getter for server data
     * @return The data the server has sent
     */
    public synchronized byte[] getResponseData(int elevatorID) {
        while(!hasResponseData.get(elevatorID)) {
            try {
                System.out.println("Waiting");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hasResponseData.set(elevatorID, false);
        notifyAll();
        return responseData.get(elevatorID);
    }

    /**
     * Protected setter for server data
     * @param data The data that the server has sent
     */
    public synchronized void putResponseData(byte[] data, int elevatorID) {
        while(hasResponseData.get(elevatorID)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Putting response into box: + " + Arrays.toString(data));
        hasResponseData.set(elevatorID, true);
        responseData.set(elevatorID, data);
        notifyAll();
    }
}