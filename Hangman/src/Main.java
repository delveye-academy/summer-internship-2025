import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);


        System.out.println("Guess a letter of the word: ");
        String word = getRandomWordFromFile("words.txt");
        StringBuilder underline = new StringBuilder(word);
        getHidden(word, underline);

        game(scan, word, underline);
        scan.close();
    }




    private static String getRandomWordFromFile(String words) {
        List<String> randomWords = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(words))){
            String line;
            while((line = br.readLine()) !=null){
                line = line.trim();
                if(!line.isEmpty()){
                    randomWords.add(line.toLowerCase());
                }

            }

        } catch (IOException e) {
            System.out.println("Cant read this file: " + e.getMessage());
            return "default";
        }
        if(randomWords.isEmpty()){
            System.out.println("There is no words in the list! ");
            return "default";
        }
        Random random = new Random();

        return randomWords.get(random.nextInt(randomWords.size()));
    }


    public static void game(Scanner scan, String word, StringBuilder underline) {
        String guess;
        int tries = 0;
        String hidden = "";
        HashSet<Character> guessedLetters = new HashSet<>();

        while(tries < 8) {
            guess = scan.nextLine();
            isOneLetter(guess);
            System.out.println();
            if (outOfGuesses(tries, word)) break;

            if(guess.isEmpty() ) continue;
               char letter = guess.charAt(0);
               if(guessedLetters.contains(letter)){
                   System.out.println("You already guessed this one choose something else! Your next letter is?");
                   continue;
               }
               guessedLetters.add(letter);
               boolean correctGuess = false;

            for (int k = 0; k < word.length(); k++) {
                if(letter == word.charAt(k) && underline.charAt(k)=='_') {
                    underline.setCharAt(k ,letter);

                    correctGuess = true;
                }
            }
            hidden = underline.toString();

            if(hidden.equals(word)){
                System.out.println("You guessed it good job! The word was "+ word.toUpperCase());
                break;

            }
            if(!correctGuess) {
                tries++;
                correctGuess = false;
            }
            Drawing(tries);
            System.out.println();
            System.out.println(hidden);
        }
    }

    private static void Drawing(int tries) {
        System.out.println("Man so far:");

        if (tries >= 1) System.out.println("  0  ");
        if (tries >= 2) System.out.print(" /");
        if (tries >= 3) System.out.print("|");
        if (tries >= 4) System.out.println("\\");
        if (tries >= 5) System.out.println("  |  ");
        if (tries >= 6) System.out.print(" /");
        if (tries >= 7) System.out.println(" \\\n  The man has been hanged");

    }

/* was giving me some trouble with writting everything out the way I wanted
    private static void drawing(int i) {
        switch (i){
            case(7):
                System.out.println("The men is hanged");
                break;
            case (6):
                System.out.println(" 0 ");
                break;
            case(5):
                System.out.print("/");
                break;
            case(4):
                System.out.print("|");
                break;
            case(3):
                System.out.println("\\");
                break;
            case(2):
                System.out.println(" | ");
                break;
            case(1):
                System.out.print("/");
                break;
            case (0):
                System.out.println(" \\");
                break;
        }
            }
*/



    private static boolean outOfGuesses(int tries, String word) {
        if(tries == 7){
            System.out.println("Better luck next time :(... The word was "+ word.toUpperCase());
            return true;
        }
        return false;
    }



    private static void isOneLetter(String guess) {
        if(guess.length() != 1){
            System.out.println("Write just one letter");
        }
    }


    private static String getHidden(String word, StringBuilder underline) {
        for (char j = 0; j < word.length(); j++) {
            underline.setCharAt(j, '_');
            
            if (j == word.length() - 1) {
                break;
            }
        }
        return  underline.toString();
    }
}