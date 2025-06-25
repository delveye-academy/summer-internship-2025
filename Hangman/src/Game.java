import java.util.HashSet;
import java.util.Scanner;

public class Game {
    final int maxTries = 8;
    private final Scanner scan;
    public Game(Scanner scan) {
        this.scan = scan;
    }

    public void play(String word) {
        StringBuilder underline = new StringBuilder("_".repeat(word.length()));
        int numOfTries = 0;
        HashSet<Character> guessedLetters = new HashSet<>();
        System.out.println("Guess a letter of the word: ");
        while (numOfTries <= maxTries) {
            String guess = scan.nextLine();
            if (guess.length() != 1) {
                System.out.println("Write just one letter");
                continue;
            }
            char letter = guess.charAt(0);
            if (guessedLetters.contains(letter)) {
                System.out.println("You already guessed this one. Try another:");
                continue;
            }
            guessedLetters.add(letter);
            boolean correctGuess = false;
            for (int i = 0; i < word.length(); i++) {
                if (letter == word.charAt(i) && underline.charAt(i) == '_') {
                    underline.setCharAt(i, letter);
                    correctGuess = true;
                }
            }
            String currentState = underline.toString();
            if (currentState.equals(word)) {
                System.out.println("You guessed it! The word was: " + word.toUpperCase());
                break;
            }

            if (!correctGuess) {
                numOfTries++;
                if (outOfGuesses(numOfTries, word)) break;
            }
            drawing(numOfTries);
            System.out.println("\n" + currentState);
        }
    }

    private void drawing(int numberOfTries) {
        System.out.println("Man so far:");
        switch (numberOfTries) {
            case 7:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                System.out.println("  |  ");
                System.out.println(" / \\");
                System.out.println("Final guess what is the word: ");
                return;
            case 6:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                System.out.println("  |  ");
                System.out.print(" /");
                return;
            case 5:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                System.out.println("  |  ");
                return;
            case 4:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                return;
            case 3:
                System.out.println("  0  ");
                System.out.print(" /|");
                return;
            case 2:
                System.out.println("  0  ");
                System.out.print(" /");
                return;
            case 1:
                System.out.println("  0  ");
                return;
            default:
                break;
        }
    }

    private boolean outOfGuesses(int tries, String word) {
        if (tries == maxTries) {
            System.out.println("Man has been hanged, better luck next time :(... The word was " + word.toUpperCase());
            return true;
        }
        return false;
    }
}
