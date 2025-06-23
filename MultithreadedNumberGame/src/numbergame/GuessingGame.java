package numbergame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GuessingGame implements Runnable {

	public static final int MAX_TARGET_NUMBER = 1000000;
	private static final int WAIT_TASKS_TO_FINISH_SECONDS = 45;
	private final int numOfGuessThreads;
	private final boolean randomGuessingOrder;
	private List<GuessingThread> guessingThreadList = new ArrayList<>();
	private ExecutorService executorService;
	private final GameState gameState = new GameState();

	public class GameState {
		final int targetNumber = 1 + new Random().nextInt(MAX_TARGET_NUMBER);
		private boolean winnerFound = false;
		private int currentTurn = 0;

		public synchronized boolean isWinnerFound() {
			return winnerFound;
		}

		public synchronized void setWinnerFound() {
			winnerFound = true;
		}

		public synchronized int getCurrentTurn() {
			return guessingThreadList.get(currentTurn).getId();
		}

		public synchronized void nextTurn() {
			currentTurn = (currentTurn + 1) % numOfGuessThreads;
		}
	}

	public GuessingGame(int numOfGuessThreads, boolean guessingOrderChoice) {
		this.numOfGuessThreads = numOfGuessThreads;
		this.randomGuessingOrder = guessingOrderChoice;
	}

	@Override
	public void run() {

		for (int i = 0; i < numOfGuessThreads; i++)
			guessingThreadList.add(new GuessingThread(this.gameState, i));

		executorService = Executors.newFixedThreadPool(this.numOfGuessThreads);
		printGameStart();
		startExecutorService();

		executorService.shutdown(); // Stop accepting new tasks

		try {
			// Wait up to WAIT_TASKS_TO_FINISH_SECONDS seconds for all tasks to finish
			if (!executorService.awaitTermination(WAIT_TASKS_TO_FINISH_SECONDS, TimeUnit.SECONDS)) {
				executorService.shutdownNow(); // Force stop if still running
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow(); // Also force stop on interruption
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
	}

	private void printGameStart() {
		System.out.println("------------------------------");
		System.out.println("Number being guessed:");
		System.out.println(gameState.targetNumber);
		System.out.println("------------------------------");
	}

	private void startExecutorService() {
		if (randomGuessingOrder)
			Collections.shuffle(guessingThreadList);

		for (Runnable task : guessingThreadList)
			executorService.submit(task);
	}

}
