import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ElevatorBox {

    /** The data of the client */
    private ArrayList<byte[]> requestData;
    /** If there is something in clientData */
    private ArrayList<Boolean> hasRequestData;

    /** The data of the server */
    private ArrayList<byte[]> responseData;
    /** If there is something in serverData */
    private ArrayList<Boolean> hasResponseData;
    /** map of the 2 elevators state and floor */
    private HashMap<String, Integer> elevatorData= new HashMap<>();


    /**

     Retrieves the elevator data stored in a HashMap.
     @return The elevator data stored in a HashMap.
     */
    public HashMap<String, Integer> getElevatorData() {
        return elevatorData;
    }
    /**

     Sets a key-value pair in the elevator data HashMap.
     @param key The key to be added to the HashMap.
     @param val The value to be added to the HashMap.
     */
    public void setElevatorData(String key, int val) {
        this.elevatorData.put(key, val);
    }

    /**
     * Creates basic databox object
     */
    public ElevatorBox(int elevatorNum) {
        this.requestData = new ArrayList<>();
        this.responseData = new ArrayList<>();
        this.hasRequestData = new ArrayList<>();
        this.hasResponseData = new ArrayList<>();

        this.elevatorData.put("el1State", 0);
        this.elevatorData.put("el2State", 0);
        this.elevatorData.put("el1Floor", 0);
        this.elevatorData.put("el2Floor", 0);

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
                System.out.println("SOMETHING INSIDE REQUEST: " +  elevatorID + ": " +  Arrays.toString(requestData.get(elevatorID)));
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Putting request into box: " + Arrays.toString(data));
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
                System.out.println("Waiting for response data to exist");
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
     * Protected getter for server data
     * @return The data the server has sent
     */
    public synchronized byte[] getSomeResponseData() {
        int responseIndex = getResponseIndex();
        while(responseIndex==-1) {
            try {
                System.out.println("Waiting for response to have data");
                wait();
                responseIndex = getResponseIndex();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        hasResponseData.set(responseIndex, false);
        notifyAll();
        return responseData.get(responseIndex);
    }

    /**
     * Gets the index of an elevator that is ready (if it exists)
     * @return
     */
    private int getResponseIndex(){
        for(int i=0; i<hasResponseData.size(); i++){
            if(hasResponseData.get(i)){
                return i;
            }
        }
        return -1;
    }

    /**
     * Protected setter for server data
     * @param data The data that the server has sent
     */
    public synchronized void putResponseData(byte[] data, int elevatorID) {
        while(hasResponseData.get(elevatorID)) {
            try {
                //DEBUG
                System.out.println("SOMETHING INSIDE RESPONSE: " + elevatorID + ": " + Arrays.toString(responseData.get(elevatorID)));
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Putting response into box: " + Arrays.toString(data));
        hasResponseData.set(elevatorID, true);
        responseData.set(elevatorID, data);
        notifyAll();
    }
}
