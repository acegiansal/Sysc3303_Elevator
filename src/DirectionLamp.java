public class DirectionLamp {
     private String LampDirection;

    public void LightDirection(String Direction) {
        logging.info("DirectionLamp","" + Thread.currentThread().getName(), Direction + " Lamp is On");
    }

}
