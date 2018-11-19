package br.edu.utfpr.cm.pasteanalyzer.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;

/**
 * @author felipevr
 *
 *         Class used to log messages.
 */
public class Log {
    private static int maxLength = 2 * 1024 * 1024;
    private static String fileName = "log.txt";
    private static boolean logToFile;

    /**
     * @return the logToFile
     */
    public static boolean isLogToFile() {
        return Log.logToFile;
    }

    /**
     * @param logToFile
     *            the logToFile to set
     */
    public static void setLogToFile(boolean logToFile) {
        Log.logToFile = logToFile;
    }

    /**
     * @return the maxLength
     */
    public static int getMaxLength() {
        return maxLength;
    }

    /**
     * @param maxLength
     *            the maxLength to set
     */
    public static void setMaxLength(int maxLength) {
        Log.maxLength = maxLength * 1024;
    }

    /**
     * @return the fileName
     */
    public static String getFileName() {
        return fileName;
    }

    /**
     * @param fileName
     *            the fileName to set
     */
    public static void setFileName(String fileName) {
        Log.fileName = fileName;
    }

    public static void log(String message, String name) {
        log(message, name, true);
    }

    public static void log(String message) {
        log(message, null, true);
    }

    public static void log(String message, boolean showDate) {
        log(message, null, showDate);
    }

    public static void log(String message, String name, boolean showDate) {
        boolean hasName = name != null && !name.isEmpty();
        StringBuilder sb = new StringBuilder();
        if (showDate) {
            sb.append(Util.dateFormat.format(Calendar.getInstance().getTime()));
        }
        if (hasName) {
            if (showDate) {
                sb.append(" at ");
            }
            sb.append(name);
            sb.append(":");
        }
        if (hasName || showDate) {
            sb.append("\r\n");
        }
        sb.append(message);
        if (logToFile) {
            saveToFile(sb.toString());
        } else {
            System.out.println(sb.toString());
        }
    }

    private static void saveToFile(String text) {
        File file = new File(fileName);
        if (file.exists() && file.length() > maxLength) {
            file.delete();
        }
        File file2 = new File(fileName);
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating log: " + fileName + ".\r\n" + e.toString());
            }
        }
        if (file2.exists()) {
            try {
                Files.write(Paths.get(fileName), (text + "\r\n").getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println("Error writing to " + fileName + ":\n" + text + "\n" + e.toString());
            }
        } else {
            System.out.println(text);
        }
    }

    public static void clearLogFile() {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        
    }
}