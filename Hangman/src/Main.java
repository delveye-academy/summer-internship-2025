import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Main gameObj = new Main();
        Scanner scan = new Scanner(System.in);
        System.out.println("Guess a letter of the word: ");
        String word = getRandomWordFromFile("words.txt");
        String hidden = "_".repeat(word.length());
        StringBuilder underline = new StringBuilder(hidden);
        gameObj.game(scan, word, underline);
        scan.close();
    }

    private static String getRandomWordFromFile(String words) {
        List<String> randomWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(words))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    randomWords.add(line.toLowerCase());
                }
            }
        } catch (IOException e) {
            System.out.println("Cant read this file: " + e.getMessage());
            return "default";
        }
        if (randomWords.isEmpty()) {
            System.out.println("There is no words in the list! ");
            return "default";
        }
        Random random = new Random();
        return randomWords.get(random.nextInt(randomWords.size()));
    }

    public void game(Scanner scan, String word, StringBuilder underline) {
        String guess;
        int numOfTries = 0;
        String hidden = "";
        HashSet<Character> guessedLetters = new HashSet<>();
        while (numOfTries < 7) {
            guess = scan.nextLine();
            if (guess.length() != 1) {
                System.out.println("Write just one letter");
                continue;
            }
            System.out.println();
            if (guess.isEmpty()) continue;
            char letter = guess.charAt(0);
            if (guessedLetters.contains(letter)) {
                System.out.println("You already guessed this one choose something else! Your next letter is?");
                continue;
            }
            guessedLetters.add(letter);
            boolean correctGuess = false;
            for (int k = 0; k < word.length(); k++) {
                if (letter == word.charAt(k) && underline.charAt(k) == '_') {
                    underline.setCharAt(k, letter);

                    correctGuess = true;
                }
            }
            hidden = underline.toString();
            if (hidden.equals(word)) {
                System.out.println("You guessed it good job! The word was " + word.toUpperCase());
                break;
            }
            if (!correctGuess) {
                numOfTries++;
            }
            this.drawing(numOfTries, word);
            System.out.println("\n" + hidden);
        }
    }

    private void drawing(int numOfTries, String word) {
        System.out.println("Man so far:");

        switch (numOfTries) {
            case 7:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                System.out.println("  |  ");
                System.out.println(" / \\");
                System.out.println("The man has been hanged :(.\n The word was: " + word.toUpperCase());
                break;
            case 6:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                System.out.println("  |  ");
                System.out.print(" /");
                break;
            case 5:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                System.out.println("  |  ");
                break;
            case 4:
                System.out.println("  0  ");
                System.out.println(" /|\\");
                break;
            case 3:
                System.out.println("  0  ");
                System.out.print(" /|");
                break;
            case 2:
                System.out.println("  0  ");
                System.out.print(" /");
                break;
            case 1:
                System.out.println("  0  ");
                break;
            default:
                break;
        }
    }
}