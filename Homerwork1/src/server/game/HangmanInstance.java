package server.game;

import java.util.*;

public class HangmanInstance {
    private char[] word;
    private char[] guessOfWord;
    private boolean rightAnsw;
    private HashSet<String> guessAttempts = new HashSet<>();

    private int tries = 0;
    private int totalTries;

    HangmanInstance(String word){
        System.out.println("We have started a new game, the word is: " + word);

        this.word = word.toLowerCase().toCharArray();
        guessOfWord = new char[word.length()];

        for(int i = 0; i < word.length(); i++) {
            guessOfWord[i] = '_';
        }

        totalTries = word.length();
    }

    void guess(String guess) {
        char[] guessInChar = guess.toLowerCase().toCharArray();
        if(guess.length() == 1) tryChar(guessInChar[0]);
        else tryWord(guessInChar);
    }

    private void tryChar(char c) {
        guessAttempts.add(String.valueOf(c));

        boolean guessedRight = false;
        rightAnsw = true;

        for(int i = 0; i < word.length; i++) {
            if(word[i] == c) {
                guessedRight = true;
                guessOfWord[i] = c;
            }

            if(guessOfWord[i] == '_') rightAnsw = false;
        }

        if(!guessedRight) tries++;
    }

    private void tryWord(char[] guess) {
        guessAttempts.add(String.valueOf(guess));

        if(Arrays.equals(word, guess)) {
            rightAnsw = true;
            guessOfWord = word;
        } else {
            tries++;
        }
    }

    boolean alreadyGuesses(String guess) {
        return guessAttempts.contains(guess);
    }

    public boolean canStartNew() {
        return !rightAnsw && tries < totalTries;
    }

    public boolean guessedCorrect() {
        return rightAnsw;
    }

    public String getWord() {
        return new String(guessOfWord);
    }

    public int getTries() {
        return tries;
    }

    public int getTotalTries() {
        return totalTries;
    }
}
