package numbergame;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MultithreadedNumberGameApp {
	
	private static final int MAX_NUMBER_OF_GUESSING_THREADS = 100;

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			int numberGuessingThreads = getThreadCountFromUser(scanner);
			boolean guessingOrderChoice = getGuessingOrderFromUser(scanner);	
			
			GuessingGame game = new GuessingGame(numberGuessingThreads, guessingOrderChoice);
			Thread targetThread = new Thread(game);
			targetThread.start();
			
			try {
	            targetThread.join();  // blocks until a guess is correct
	        } catch (InterruptedException e) {
	            System.out.println("Game interrupted.");
	        }
	
	        System.out.print("Play again? (y/n): ");
	        String again = scanner.next().trim().toLowerCase();
	        if (!again.equals("y")) break;
	        scanner.nextLine();
		}
		scanner.close();
		System.out.println("\n Thank you for playing. Until next time （￣︶￣）↗　");
		return;
	}
	
	

	private static int getThreadCountFromUser(Scanner scanner) {
				
		System.out.println("Select a number of guessing threads (between 1 and "
				+ MAX_NUMBER_OF_GUESSING_THREADS + "): ");
		
		int selectedNumber = -1;
		while(true) {
			try {			
				selectedNumber = scanner.nextInt();
				if(selectedNumber > 0 && selectedNumber <= MAX_NUMBER_OF_GUESSING_THREADS) 
					break;
				
				System.out.println("Please only write one number between 1 and "
						+ MAX_NUMBER_OF_GUESSING_THREADS + ": ");			
			} catch (InputMismatchException e) {
				System.out.println("That's not an integer. Try again. ");
				scanner.nextLine();
			}
		}
		scanner.nextLine();
		return selectedNumber;
	}
	
	// false (0) is for option 1 - ordered, and true (1) is for option 2 - random
	private static boolean getGuessingOrderFromUser(Scanner scanner) {
		
		printGuessingOrderText(scanner);
		boolean randomOrder = false;
		String userInputString;
		
		while(true) {						
			userInputString = scanner.nextLine().toLowerCase();
			if(userInputString.equals("1") || userInputString.equals("option1") 
					|| userInputString.equals("option 1"))
				break;						// if the user types 1, that's the ordered option
			else if (userInputString.equals("2") || userInputString.equals("option2") 
					|| userInputString.equals("option 2")) {
				randomOrder = true;
				break;
			} else	
				System.out.println("Invalid input, try again. Write '1' for option 1 or "
					+ "'2' for option 2");				
		}
		
		System.out.println();
		
		return randomOrder;
	}


	private static void printGuessingOrderText(Scanner scanner) {
		System.out.println("Select the guessing order. These are the 2 options: ");
		System.out.println("Option 1 - ordered option: ");
		System.out.println("\t The threads will guess in the sequence they were created "
				+ "(for example, if there are three threads, they will guess in the order: "
				+ "Thread 1, Thread 2, Thread 3)");
		System.out.println("Option 2 - random option: ");
		System.out.println("\t The threads will guess in a random sequence. For instance, "
				+ "with three threads, they might guess in the order: Thread 2, Thread 1, "
				+ "Thread 3, or any other combination determined by a random generator.");		
	}

}
