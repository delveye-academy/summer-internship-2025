import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Game game = new Game(scan);
        while (true) {
            String word = WordProvider.getRandomWord("words.txt");
            game.play(word);
            System.out.println("\nDo you want to play again? (yes/no)");
            String answer = scan.nextLine().trim().toLowerCase();
            if (!answer.equals("yes") && !answer.equals("y")) {
                System.out.println("Thanks for playing! Goodbye.");
                break;
            }
        }
        scan.close();
    }
}
