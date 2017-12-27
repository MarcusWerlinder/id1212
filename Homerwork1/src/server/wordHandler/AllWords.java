package server.wordHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AllWords {
    private static AllWords instance = new AllWords();
    private static List<String> words;
    private Random ran = new Random();

    private AllWords() {
        String pathToWords = "./words.txt";

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(pathToWords))) {
            words = reader.lines().collect(Collectors.toList());
        } catch (IOException e){
            System.err.println("Failed to get your words soz");
            e.printStackTrace();
        }
    }

    private static AllWords getInstance() {
        return instance;
    }

    private String ranWord() {
        return words.get(ran.nextInt(words.size())).toLowerCase();
    }

    public static String getRanWord() {
        return getInstance().ranWord();
    }
}
