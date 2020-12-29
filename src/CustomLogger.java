import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

class CustomLogger {

    private Logger customLogger = null;
    private String customLoggerPath = null;


    private void init(String filePath) {
        try {
            Logger logger = Logger.getLogger(CustomLogger.class.getName());
            FileHandler fileHandler = new FileHandler(filePath);
            SimpleFormatter formatter = new SimpleFormatter();

            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);

            String headerMsg = "******************************Creating new log (" + filePath + ")******************************";

            this.customLogger = logger;
            this.customLoggerPath = filePath;

            logger.warning(headerMsg);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void validateLogger(String filePath) {
        if (this.customLogger == null || !this.customLoggerPath.equals(filePath)) {
            this.init(filePath);
        }
    }

    void addStringToLog(String filePath, String msg) {
        this.validateLogger(filePath);
        this.customLogger.info(msg);
    }

    void addArrayToLog(String filePath, int[] array) {
        this.validateLogger(filePath);
        StringBuilder strArray = new StringBuilder();
        for (int item : array) {
            strArray.append(item).append(" ");
        }
        this.customLogger.info(strArray.toString());
    }

}
