import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
public class LogConfigurator
{
    private static final Logger LOGGER = Logger.getLogger(LogConfigurator.class.getName());
    private static final String FILE_NAME = "SYSC3303_ITER1_LOG.txt";

    public static void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler(FILE_NAME, true);
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getLogger("").addHandler(fileHandler);
            Logger.getLogger("").setLevel(Level.INFO);
        } catch (IOException e) {
            LOGGER.severe("Unable to configure file handler: " + e.getMessage());
        }
    }
}
