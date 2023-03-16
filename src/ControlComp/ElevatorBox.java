package ControlComp;
import DataComp.ElevatorPacket;
import DataComp.ElevatorStatus;
import Config.ConfigInfo;

import java.util.ArrayList;
import java.util.Arrays;


public class ElevatorBox {

   private ArrayList<ElevatorStatus> statuses;
   private ArrayList<byte[]> requests;
   private ArrayList<Boolean> hasRequest;
   private byte[] noRequest;

   public ElevatorBox(int elevatorNum){
       // Set up the noRequest
       noRequest = new byte[ConfigInfo.PACKET_SIZE];

       statuses = new ArrayList<>();
       requests = new ArrayList<>();
       hasRequest = new ArrayList<>();

       //Fill with empty info
       for(int i = 0; i<elevatorNum; i++){
           statuses.add(i, new ElevatorStatus(i));
           requests.add(i, noRequest);
           hasRequest.add(i, false);
       }
   }

   public synchronized ElevatorStatus getStatus(int index){
       return statuses.get(index);
   }

   public synchronized void setStatus(int index, ElevatorStatus status){
       statuses.set(index, status);
   }

   public synchronized byte[] getRequest(int index){
       byte[] toSend = requests.get(index);
       // Remove the request
       requests.set(index, noRequest);
       hasRequest.set(index, false);
       notifyAll();
       return toSend;
   }

   public synchronized void setRequest(int index, byte[] request){
       while(!ElevatorPacket.isEmptyRequest(requests.get(index))){
           try {
               wait();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }

       hasRequest.set(index, true);
       requests.set(index, request);
       notifyAll();
   }

   public static void main(String[] args){
       new ElevatorBox(4);
   }

}
