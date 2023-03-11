import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;


    public class logging {

        private static PrintStream printStream;

        static {
            try {
                printStream = new PrintStream(new FileOutputStream("SYSC3303_Elevator_Logging.txt", true));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public static void info(String cls, String ElevatorNum, String notification) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            String log = String.format("[Time:%s] [Class: %s] [Elevator: %s] [Notification] %s", time, cls, ElevatorNum, notification);
            System.out.println(log);
            printStream.println(log);
        }

        public static void info2(String cls, String notification) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            String log = String.format("[Time:%s] [Class: %s] [Notification] %s", time, cls, notification);
            System.out.println(log);
            printStream.println(log);
        }

        public static void warning(String cls, String notification) {
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            String log = String.format("[Time:%s] [Class: %s] [Elevator] [Warning] %s", time,  cls, notification);
            System.out.println(log);
            printStream.println(log);
        }
    }

