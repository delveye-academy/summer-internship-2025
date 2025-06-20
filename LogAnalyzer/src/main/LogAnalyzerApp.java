package main;

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

	private static ConcurrentLinkedQueue<Log> logQueue = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<Log> errorLogs = new ConcurrentLinkedQueue<>();
	private static final int NUM_OF_THREADS = Math.max(1, Runtime.getRuntime().availableProcessors() - 1);
	public static final int NUM_OF_STRINGS_IN_LOG = 4;

	public static void main(String[] args) {
		List<String> logs = LogRepository.getLogs();
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
		List<Future<String>> futures = new ArrayList<>();

		for (String log : logs) {
			Callable<String> task = () -> {
				try {
					if (log == null)
						throw new LogParseException("Log entry is null");
					String[] logStrings = log.split(" ", NUM_OF_STRINGS_IN_LOG);
					if (logStrings.length < NUM_OF_STRINGS_IN_LOG)
						throw new LogParseException("Invalid input of log");
					Log newLog = Log.fromStringArray(logStrings);

					logQueue.add(newLog);
					if (newLog.getLogType().equals("ERROR"))
						errorLogs.add(newLog);
					return "Processed: " + newLog + " (by " + Thread.currentThread().getName() + ")";
				} catch (LogParseException e) {
					return e.getMessage() + ": " + log;
				} catch (DateTimeParseException e) {
					return e.getMessage() + ": " + log;
				} catch (Exception e) {
					return "Unexpected error while processing: " + log;
				}
			};

			futures.add(executorService.submit(task));
		}

		// Get the results
		for (Future<String> future : futures) {
			try {
				System.out.println(future.get());
			} catch (Exception e) {
				e.printStackTrace();
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

		System.out.println("________________________\n\t Result: ");
		System.out.println("Error logs: ");
		for (Log log : errorLogs) {
			System.out.println(log);
		}

	}

}
