import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static PrintStream printStream;

    static {
        try {
            printStream = new PrintStream(new FileOutputStream("log.txt", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void info(String elevator, String cls, String notification) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String log = String.format("[Time:%s] [Elevator] %s [Class: %s] [Info] %s", time, elevator, cls, notification);
        System.out.println(log);
        printStream.println(log);
    }

    public static void warning(String elevator, String cls, String notification) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        String log = String.format("[Time:%s] [Elevator] %s [Class: %s] [Warning] %s", time, elevator, cls, notification);
        System.out.println(log);
        printStream.println(log);
    }
}