package com.employee.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import com.employee.util.Constants;
import com.employee.util.DateUtil;

public class LogUtil {

	private LogUtil() {
		
	}
	
	static{
		new File(Constants.LOGS_DIR).mkdir();
	}
	
	
	public static void info(String message) {
        write("INFO", message);
	}
	
	public static void warn(String message) {
		write("warn",message);
	}
	
	public static void Error(String message) {
		write("error",message);
	}
	
    private static synchronized void write(String level, String message) {
        File file = new File(Constants.LOG_FILE);
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String line = "[" + DateUtil.formatDateTime(LocalDateTime.now()) + "] [" + level + "] " + message;
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log entry: " + e.getMessage());
        }
    }
}
