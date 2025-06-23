package numbergame;

import java.util.Random;

import numbergame.GuessingGame.GameState;

public class GuessingThread implements Runnable {

	private static final int SLEEP_BETWEEN_CYCLES_MS = 2000;
	private final GameState gameState;
	private int guess = new Random().nextInt(GuessingGame.MAX_TARGET_NUMBER) + 1;
	private int numOfGuesses = 0;
	private final int id;
	private final String name;

	public GuessingThread(GameState gameState, int threadId) {

		this.gameState = gameState;
		this.id = threadId;
		this.name = "Thread " + threadId;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	private void printGuess() {
		System.out.print(this.getName() + " Guessing " + ++numOfGuesses + ". time: " + guess + " ");
	}

	@Override
	public void run() {

		int low = 1, high = GuessingGame.MAX_TARGET_NUMBER;

		while (!gameState.isWinnerFound() && low <= high) {

			synchronized (gameState) {

				while (gameState.getCurrentTurn() != id && gameState.isWinnerFound() == false) {
					try {
						gameState.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						return;
					}
				}

				if (gameState.isWinnerFound())
					break;

				printGuess();
				if (guess == gameState.targetNumber) {
					gameState.setWinnerFound();
					System.out.println(" (Correct! Found in " + numOfGuesses + " attempts by " + this.getName() + ")");
				} else {
					if (guess < gameState.targetNumber) {
						System.out.println("(Too Low)");
						low = guess + 1;
					} else if (guess > gameState.targetNumber) {
						System.out.println("(Too High)");
						high = guess - 1;
					}
				}

				gameState.nextTurn();
				gameState.notifyAll();
			}

			guess = (low + high) / 2;
			try {
				Thread.sleep(SLEEP_BETWEEN_CYCLES_MS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				System.out.println(Thread.currentThread().getName() + " was interrupted while sleeping.");
			}

		} // end of while

		if (!gameState.isWinnerFound())
			System.out.println(this.getName() + " cannot find the target number...");
	}
}
