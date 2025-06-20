import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class WordProvider {

    public static String getRandomWord(String fileName) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    words.add(line.toLowerCase());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            System.out.println("Using placeholder word: 'default'");
            return "default";
        }
        if (words.isEmpty()) {
            System.out.println("Word list is empty. Using placeholder word: 'default'");
            return "default";
        }
        Random rand = new Random();
        return words.get(rand.nextInt(words.size()));
    }
}