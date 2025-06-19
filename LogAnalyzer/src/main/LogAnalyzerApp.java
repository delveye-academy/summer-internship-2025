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

public class LogAnalyzerApp {
	
	private static ConcurrentLinkedQueue<Log> logQueue = new ConcurrentLinkedQueue<>();
	private static ConcurrentLinkedQueue<Log> errorLogs = new ConcurrentLinkedQueue<>();
	private static final int NUM_OF_THREADS = 4;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		List<String> logs = LogRepository.getLogs();
		ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
		List<Future<String>> futures = new ArrayList<>();
		
		for (String log: logs) {
			Callable<String> task = () -> {
				try {
					String[] logStrings = log.split(" ", 4);				
					if (logStrings.length < 4) throw new LogParseException("Invalid input of log");
					Log newLog = new Log(logStrings);
					
					logQueue.add(newLog);
					if (newLog.getLogType().equals("ERROR")) errorLogs.add(newLog);	
					return "Processed: " + newLog + " by " + Thread.currentThread().getName();
				} catch (LogParseException e) {
					return "Invalid input format: " + log;
				} catch (DateTimeParseException e) {
					return "Invalid input format: " + log;
				} catch (Exception e) {
		            e.printStackTrace(); // unexpected error
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
			Thread.sleep(1000);
		} catch (InterruptedException e) {			
			Thread.currentThread().interrupt();
		    System.out.println("Main thread was interrupted while sleeping.");
		}
		
		System.out.println("________________________\n Result: ");
		System.out.println("Error logs: ");
		for (Log log: errorLogs) {
			System.out.println(log);
		}
		
	}

}
