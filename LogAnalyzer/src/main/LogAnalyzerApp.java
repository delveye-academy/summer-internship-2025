package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class LogAnalyzerApp {

	private static final String FILEPATH_STRING = "src/main/logs.txt";
	private static ConcurrentLinkedQueue<Log> logQueue = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<Log> errorLogs = new ConcurrentLinkedQueue<>();
	private static final int NUM_OF_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
	public static final int NUM_OF_ENTRIES_IN_LOG = 4;		// the first 3 strings are: date, time, type of Log,
															// the rest of the log is the description of the log
	public static void main(String[] args) {
		List<String> logs;
		try {
			logs = LogAnalyzerApp.readLogsFromFile(FILEPATH_STRING);
			LogAnalyzerApp.processLogs(logs);
			LogAnalyzerApp.printErrorLogs();
		} catch (LogParseException e) {
			System.out.println(e.getMessage());
		} catch (DateTimeParseException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {			
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	private static void processLogs(List<String> logs) throws LogParseException, DateTimeParseException {
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
		List<Future<String>> futures = new ArrayList<>();

		for (String log : logs) {
			Callable<String> task = () -> {				
				if (log == null)
					throw new LogParseException("Log entry is null" + ":" + log);
				String[] logStrings = log.split(" ", NUM_OF_ENTRIES_IN_LOG);
				if (logStrings.length < NUM_OF_ENTRIES_IN_LOG)
					throw new LogParseException("Invalid input of log" + ":" + log);
				Log newLog = Log.fromStringArray(logStrings);

				logQueue.add(newLog);
				if (newLog.getLogType().equals("ERROR"))
					errorLogs.add(newLog);
				return "Processed: " + newLog + " (by " + Thread.currentThread().getName() + ")";				
			};

			futures.add(executorService.submit(task));
		}

		// Get the results
		for (Future<String> future : futures) {
			try {
				System.out.println(future.get());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		executorService.shutdown();

		try {
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow(); // force shutdown if not finished
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}		
	}

	private static void printErrorLogs() {
		System.out.println("________________________\n\t Result: ");
		System.out.println("Error logs: ");
		for (Log log : errorLogs) {
			System.out.println(log);
		}		
	}

	public static List<String> readLogsFromFile(String filepath) throws IOException {
        return Files.readAllLines(Path.of(filepath));
    }	
}
